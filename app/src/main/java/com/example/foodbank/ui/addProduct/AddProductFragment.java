package com.example.foodbank.ui.addProduct;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.MainActivity;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.example.foodbank.ui.products.Product;
import com.example.foodbank.ui.products.ViewProductActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class AddProductFragment extends Fragment {

    // QR Code Scanner Elements
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    // Activity states for switching layouts
    private static final int INITIAL_STATE = 1001;
    private static final int PRODUCT_NOT_FOUND_STATE = 1002;
    private static final int PRODUCT_FOUND_STATE = 1003;
    private RequestQueue mQueue;
    private int CURRENT_STATE = INITIAL_STATE;
    private Button button_scanProduct;
    private SurfaceView surfaceView_camera;
    private CameraSource cameraSource;
    private TextView textView_barcodeResult;
    private String barcodeData;
    // Layout elements
    private ToneGenerator toneGen1;
    private EditText textInput_enterBarcode;

    // Barcode variables
    private String inputBarcode;
    private String inputBarcodePutExtra;

    // Product attributes
    private String code;
    private String title;
    private String nutriScore;
    private String novaGroup;
    private String ecoScore;
    private String ingredients;
    private String nutriments;
    private String vegan;
    private String vegetarian;
    private String categoriesImported;
    private String imageUrl;

    // Control the surface view/product card visibility, and each scan
    private boolean isScanned;

    private Vector<Product> productsList = new Vector<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.e1_fragment_add_product, container, false);
        // Set product attributes on create
        clearData();

        // Implement an HTTP request using Volley library
        this.mQueue = Volley.newRequestQueue(requireContext());

        // QR Code Scanner
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView_camera = root.findViewById(R.id.surfaceView_camera);
        textView_barcodeResult = root.findViewById(R.id.textView_barcodeResult);
        setIsScanned(false);


        textInput_enterBarcode = root.findViewById(R.id.textInput_enterBarcode);

        // Layout elements
        Button buttonAddProduct = root.findViewById(R.id.button_addProduct);

        button_scanProduct = root.findViewById(R.id.button_scanProduct);
        buttonAddProduct.setOnClickListener(v -> checkBarcodeOnInput(root));
        button_scanProduct.setOnClickListener(v -> scanAgain(root));

        initialiseDetectorsAndSources(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*-------------------------------INPUTS--------------------------------------*/
    public void checkBarcodeOnInput(View view) {
        this.textInput_enterBarcode = view.findViewById(R.id.textInput_enterBarcode);

        if (textInput_enterBarcode.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Please enter barcode", Toast.LENGTH_SHORT).show();
        } else {
            setInputBarcode(textInput_enterBarcode.getText().toString());
            getResponse(view);
        }
    }

    private void initialiseDetectorsAndSources(View view) {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(requireContext(), barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true) //you should add this feature
                .build();
        surfaceView_camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView_camera.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { cameraSource.stop(); }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(@NotNull Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    textView_barcodeResult.post(() -> {
                        // Get Barcode data from Scanner
                        if (!isIsScanned()) {
                            barcodeData = barcodes.valueAt(0).displayValue;
                            setInputBarcode(barcodeData);
                            setIsScanned(true);
                            getResponse(view);
                            barcodeData = "";
                            barcodes.valueAt(0).displayValue = "";
                        }
                    });
                }
            }
        });
    }

    /*-------------------------------RESPONSE-----------------------------------*/
    private void getResponse(View view) {
        hideKeyboard();
        String mainURL = "https://world.openfoodfacts.org/api/v0/product/";

        String barcode = getInputBarcode();
        String apiURL = mainURL + barcode;


        // The user's input is concatenated with the main URL and the program makes a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null, response -> {
            try {
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                // if the response is successful we get useful data from the JSON file
                if (response.getInt("status") == 1) {
                    jsonParse(view);
                } else {
                    // Switch layout elements
                    switchLayout(view, PRODUCT_NOT_FOUND_STATE);
                    // Show alert dialog on if status is 0
                    alertFailDialogBox(view);
                    clearData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Switch layout elements
                switchLayout(view, PRODUCT_NOT_FOUND_STATE);
                // Show alert dialog on if status is 0
                alertFailDialogBox(view);
                clearData();
            }
        }, error -> {
            // If during the request or response an error is occurred, a Snackbar message will pop up
            Snackbar.make(requireView(), "Something went wrong. Please check your connection", BaseTransientBottomBar.LENGTH_LONG).show();
            switchLayout(view, PRODUCT_NOT_FOUND_STATE);
        });
        mQueue.add(request);
    }

    private void jsonParse(View view) {
        String barcode = getInputBarcode();
        String mainURL = "https://world.openfoodfacts.org/api/v0/product/";
        String apiURL = mainURL + barcode;

        // Set up progress bar before call
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Fetching data from world.openfoodfacts.org");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Show it
        progressDialog.show();

        // The user's input is concatenated with the main URL and the program makes a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null, response -> {
            try {
                JSONObject productObject = response.getJSONObject("product");
                // Check and get the available data
                if(productObject.has("code"))
                    setCode(productObject.getString("code"));
                else
                    setCode("Unknown");
                if(productObject.has("product_name"))
                    setTitle(productObject.getString("product_name"));
                else
                    setTitle("Unknown");

                if (productObject.has("nutriscore_grade"))
                    setNutriScore(productObject.getString("nutriscore_grade"));
                else
                    setNutriScore("Unknown");
                if (productObject.has("nova_group"))
                    setNovaGroup(productObject.getString("nova_group"));
                else
                    setNovaGroup("Unknown");
                if (productObject.has("ecoscore_grade"))
                    setEcoScore(productObject.getString("ecoscore_grade"));
                else
                    setEcoScore("Unknown");
                if (productObject.has("ingredients_text"))
                    setIngredients(productObject.getString("ingredients_text"));
                else
                    setIngredients("Unknown");

                if (productObject.has("nutriments")) {
                    String originalString = productObject.getString("nutriments");
                    //------------------ Modify Nutriments string -------------------//
                    int capitalizeFirst = 0;

                    StringBuilder originalStringBuild = new StringBuilder();
                    char tmpChar = ' ';
                    for (int i = 0; i < originalString.length(); i++) {
                        tmpChar = originalString.charAt(i);

                        if (capitalizeFirst == 1) {
                            tmpChar = Character.toUpperCase(tmpChar);
                            capitalizeFirst = 0;
                        }
                        if(tmpChar == '{' || tmpChar == '"' || tmpChar == ',' || tmpChar == '}') {
                            capitalizeFirst++;
                        }

                        switch (tmpChar) {
                            case '_':
                            case '-':
                                tmpChar = ' ';
                                break;
                            case '"':
                                tmpChar = '\0';
                                break;
                            case '{':
                            case '}':
                            case ',':
                                tmpChar = '\n';
                                break;
//                            default:
//
//                                break;
                        }
                        originalStringBuild.append(tmpChar);
                    }
                    nutriments = originalStringBuild.toString();

                } else
                    setNutriments("Unknown");
                if (productObject.has("vegan"))
                    setVegan(productObject.getString("vegan"));
                else
                    setVegan("Unknown");
                if (productObject.has("vegetarian"))
                    setVegetarian(productObject.getString("vegetarian"));
                else
                    setVegetarian("Unknown");
                if (productObject.has("categories_imported"))
                    setCategoriesImported(productObject.getString("categories_imported"));
                else
                    setCategoriesImported("Unknown");

                // Set default image if not found
                if (productObject.has("image_front_small_url")) {
                    setImageUrl(productObject.getString("image_front_small_url"));
                } else {
                    setImageUrl("https://static.wixstatic.com/media/cd859f_11e62a8757e0440188f90ddc11af8230~mv2.png");
                }

                inputBarcodePutExtra = getCode();

                // Check if the product is not already included on the Database and add it
                addProduct();

                // Set and show Products card
                setProductCard(requireView());
                switchLayout(view, PRODUCT_FOUND_STATE);

                clearData();
                textInput_enterBarcode.setText("");
                progressDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
                alertFailDialogBox(view);
                progressDialog.dismiss();
            }
        }, error -> {
            // If during the request or response an error is occurred, a Snackbar message will pop up
            Snackbar.make(requireView(), "Something went wrong. Please check your connection", BaseTransientBottomBar.LENGTH_LONG).show();
            switchLayout(view, PRODUCT_NOT_FOUND_STATE);
            progressDialog.dismiss();
        });
        mQueue.add(request);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    // Add the scanned product on products list
    public void addProduct() {
        Product product = new Product(getCode(), getTitle(), getNutriScore(), getNovaGroup(), getEcoScore(), getIngredients(), getNutriments(), getVegan(), getVegetarian(), getCategoriesImported(),
                false, System.currentTimeMillis(), getImageUrl());

        productsList.addAll(getAllProductsSortedByTitle());

        // Check if the product is not already included on the Database
        boolean newProduct = true;
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getBarcode().equals(getCode())) {
                newProduct = false;
            }
        }

        if (newProduct) {
            alertAddProductDialogBox(product);
        } else {
            Toast.makeText(getContext(), "This item is already added to your products", Toast.LENGTH_LONG).show();
        }
    }

    // Insert product on products db
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
        Toast.makeText(getContext(), "New item added to your products", Toast.LENGTH_LONG).show();
    }

    List<Product> getAllProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(getActivity()).productsDao().getProductsSortedByTitle();
    }

    /*--------------------FUNCTIONS FOR REPEATING A SCAN------------------------*/
    public void scanAgain(View view) {
        textInput_enterBarcode.setText("");
        textView_barcodeResult.setText("");

        setInputBarcode("");
        setIsScanned(false);

        switchLayout(view, INITIAL_STATE);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(AddProductFragment.this).attach(AddProductFragment.this).commit();

    }

    public void clearData() {
        setCode("");
        setTitle("");
        setNutriScore("");
        setEcoScore("");
        setNovaGroup("");
        setIngredients("");
        setNutriments("");
        setVegan("");
        setVegetarian("");
        setCategoriesImported("");
        setImageUrl("");

        setInputBarcode("");
    }

    /*-------------------FUNCTIONS WHEN PRODUCT IS FOUND-----------------------*/
    public void setProductCard(View view) {
        TextView textView_addedProductTitle = view.findViewById(R.id.textView_addedProductTitle);
        ImageView imageView_addedProductNutriScore = view.findViewById(R.id.imageView_addedProductNutriScore);
        ImageView imageView_addedProductEcoScore = view.findViewById(R.id.imageView_addedProductEcoScore);
        ImageView imageView_addedNovaGroup = view.findViewById(R.id.imageView_addedNovaGroup);
        TextView textView_barcodeResult = view.findViewById(R.id.textView_barcodeResult);
        TextView textView_addedProductIngredients = view.findViewById(R.id.textView_addedProductIngredients);
        TextView textView_addedProductNutriments = view.findViewById(R.id.textView_addedProductNutriments);
        TextView textView_addedProductVegan = view.findViewById(R.id.textView_addedProductVegan);
        TextView textView_addedProductVegetarian = view.findViewById(R.id.textView_addedProductVegetarian);
        TextView textView_addedProductCategoriesImported = view.findViewById(R.id.textView_addedProductCategoriesImported);
        ImageView imageView_addedProductImage = view.findViewById(R.id.imageView_addedProductImage);


        // Initialize values on card
        textView_addedProductTitle.setText(title);

        switch (nutriScore) {
            case "1":
            case "A":
            case "a":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_a);
                break;
            case "2":
            case "B":
            case "b":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_b);
                break;
            case "3":
            case "C":
            case "c":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_c);
                break;
            case "4":
            case "D":
            case "d":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_d);
                break;
            case "5":
            case "E":
            case "e":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_e);
                break;
            default:
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_unknown);
                break;
        }

        switch (ecoScore) {
            case "1":
            case "A":
            case "a":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_a);
                break;
            case "2":
            case "B":
            case "b":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_b);
                break;
            case "3":
            case "C":
            case "c":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_c);
                break;
            case "4":
            case "D":
            case "d":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_d);
                break;
            default:
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
                break;
        }

        switch (novaGroup) {
            case "1":
            case "A":
            case "a":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_1);
                break;
            case "2":
            case "B":
            case "b":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_2);
                break;
            case "3":
            case "C":
            case "c":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_3);
                break;
            case "4":
            case "D":
            case "d":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_4);
                break;
            default:
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
                break;
        }

        textView_barcodeResult.setText("Barcode: " + inputBarcode);

        if (!ingredients.equals("")) {
            textView_addedProductIngredients.setText("Ingredients: " + ingredients);
        } else {
            textView_addedProductIngredients.setText("");
        }
        if (!nutriments.equals("")) {
            textView_addedProductNutriments.setText("Nutriments: " + nutriments);
        } else {
            textView_addedProductNutriments.setText("");
        }
        if (!vegan.equals("")) {
            textView_addedProductVegan.setText("Vegan: " + vegan);
        } else {
            textView_addedProductVegan.setText("");
        }
        if (!vegetarian.equals("")) {
            textView_addedProductVegetarian.setText("Vegetarian: " + vegetarian);
        } else {
            textView_addedProductVegetarian.setText("");
        }
        if (!categoriesImported.equals("")) {
            textView_addedProductCategoriesImported.setText("Categories: " + categoriesImported);
        } else {
            textView_addedProductCategoriesImported.setText("");
        }


        try {
            Picasso.get().load(getImageUrl()).resize(66, 75).centerCrop().into(imageView_addedProductImage);
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(requireView(), "Something went wrong. Please check your connection.", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    public void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

    /*--------------------------------------------------------------------------*/
    public void alertFailDialogBox(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Product not found. Do you want to scan again?");
        builder.setTitle("Scanning Result");
        builder.setPositiveButton("Try Again", (dialog, which) -> scanAgain(view)).setNegativeButton("Finish", (dialog, which) -> {
            requireActivity().finish();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertAddProductDialogBox(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Do you want to add this product to your list?");
        builder.setTitle("Product found!");
        builder.setPositiveButton("Add", (dialog, which) -> insert(product)).setNegativeButton("Finish", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void switchLayout(View view, int state) {
        // Camera
        SurfaceView surfaceView_camera = view.findViewById(R.id.surfaceView_camera);

        // Product card
        ScrollView scrollView_addedProduct = view.findViewById(R.id.scrollView_addedProduct);
        CardView cardView_addedProduct = view.findViewById(R.id.cardView_addedProduct);

        // Frame layout
        FrameLayout frameLayout_surface_view = view.findViewById(R.id.frameLayout_surfaceView);

        Button button_scanProduct = view.findViewById(R.id.button_scanProduct);

        switch (state) {
            case INITIAL_STATE:
                surfaceView_camera.setVisibility(View.VISIBLE);
                frameLayout_surface_view.setVisibility(View.INVISIBLE);
                cardView_addedProduct.setVisibility(View.INVISIBLE);
                button_scanProduct.setVisibility(View.INVISIBLE);
                scrollView_addedProduct.scrollTo(0, 0);
                break;
            case PRODUCT_FOUND_STATE:
                surfaceView_camera.setVisibility(View.INVISIBLE);
                frameLayout_surface_view.setVisibility(View.INVISIBLE);
                cardView_addedProduct.setVisibility(View.VISIBLE);
                button_scanProduct.setVisibility(View.VISIBLE);
                break;
            case PRODUCT_NOT_FOUND_STATE:
                surfaceView_camera.setVisibility(View.INVISIBLE);
                frameLayout_surface_view.setVisibility(View.VISIBLE);
                cardView_addedProduct.setVisibility(View.INVISIBLE);
                button_scanProduct.setVisibility(View.VISIBLE);
        }
    }

    /*----------------------------Getters & Setters-----------------------------*/
    public String getInputBarcode() {
        return inputBarcode;
    }

    public void setInputBarcode(String inputBarcode) { this.inputBarcode = inputBarcode; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getNutriScore() { return nutriScore; }

    public void setNutriScore(String nutriScore) { this.nutriScore = nutriScore; }

    public String getNovaGroup() { return novaGroup; }

    public void setNovaGroup(String novaGroup) { this.novaGroup = novaGroup; }

    public String getEcoScore() { return ecoScore; }

    public void setEcoScore(String ecoScore) { this.ecoScore = ecoScore; }

    public String getIngredients() { return ingredients; }

    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getNutriments() { return nutriments; }

    public void setNutriments(String nutriments) { this.nutriments = nutriments; }

    public String getVegan() { return vegan; }

    public void setVegan(String vegan) { this.vegan = vegan; }

    public String getVegetarian() { return vegetarian; }

    public void setVegetarian(String vegetarian) { this.vegetarian = vegetarian; }

    public String getCategoriesImported() { return categoriesImported; }

    public void setCategoriesImported(String categoriesImported) { this.categoriesImported = categoriesImported; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    private boolean isIsScanned() {
        return isScanned;
    }

    private void setIsScanned(boolean isScanned) { this.isScanned = isScanned; }
}



