package com.example.foodbank.ui.addProduct;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.MainActivity;
import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class AddProductFragment extends Fragment implements View.OnClickListener {

    // QR Code Scanner Elements
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    // Barcode

    RequestQueue mQueue;

    private SurfaceView surfaceView_camera;
    private CameraSource cameraSource;
    private TextView textView_barcodeResult;
    private String barcodeData;

    // Layout elements
    private ToneGenerator toneGen1;
    private EditText textInput_enterBarcode;

    // Product attributes
    private String inputBarcode;
    private String code;
    private String title;
    private String nutriScore;
    private String novaGroup;
    private String ecoScore;
    private String imageUrl;

    // Control the surface view/product card visibility
    private boolean productFound;
    // Control each scan
    private boolean isScanned;

    // Getters & Setters
    public boolean isProductFound() { return productFound; }

    public void setProductFound(boolean productFound) { this.productFound = productFound; }

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

    private boolean isIsScanned() {
        return isScanned;
    }

    private void setIsScanned(boolean isScanned) { this.isScanned = isScanned; }

    public String getInputBarcode() {
        return inputBarcode;
    }

    public void setInputBarcode(String inputBarcode) { this.inputBarcode = inputBarcode; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /*------------------------------------------------------------------------------------------------------------*/

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.e1_fragment_add_product, container, false);

        // Set product attributes on create
        clearProductData();

        // Implement an HTTP request using Volley library
        this.mQueue = Volley.newRequestQueue(requireContext());

        // QR Code Scanner
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView_camera = root.findViewById(R.id.surfaceView_camera);
        textView_barcodeResult = root.findViewById(R.id.textView_barcodeResult);
        setProductFound(false);
        setIsScanned(false);


        textInput_enterBarcode = root.findViewById(R.id.textInput_enterBarcode);

        // Layout elements
        Button buttonAddProduct = root.findViewById(R.id.button_addProduct);
        Button button_scanProduct = root.findViewById(R.id.button_scanProduct);
        buttonAddProduct.setOnClickListener(view -> checkBarcodeOnInput(root));
        button_scanProduct.setOnClickListener(v -> scanAgain());

        initialiseDetectorsAndSources();

        return root;
    }

    /*-------------------------------INPUTS--------------------------------------*/
    public void checkBarcodeOnInput(View view) {
        this.textInput_enterBarcode = view.findViewById(R.id.textInput_enterBarcode);

        if (textInput_enterBarcode.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Please enter barcode", Toast.LENGTH_SHORT).show();
        } else {
            setInputBarcode(textInput_enterBarcode.getText().toString());
            getResponse();
        }
    }

    private void initialiseDetectorsAndSources() {
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
                            textView_barcodeResult.setText(barcodeData);
                            setIsScanned(true);
                            getResponse();
                        }
                    });
                }
            }
        });
    }
    /*--------------------------------------------------------------------------*/

    /*-------------------------------RESPONSE-----------------------------------*/
    private void getResponse() {
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
                    jsonParseTitleAndCode();
                    // Show alert dialog on if status is 0
                } else {
                    alertDialogBox();
                    clearProductData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Show alert dialog on if status is not found
                alertDialogBox();
                clearProductData();
            }
        }, error -> {
            // If during the request or response an error is occurred, a Toast message will pop up
            Toast.makeText(getContext(), "Something went wrong. Please check your connection.", Toast.LENGTH_LONG).show();
        });
        mQueue.add(request);
    }

    private void jsonParseTitleAndCode() {

        String barcode = getInputBarcode();
        String mainURL = "https://world.openfoodfacts.org/api/v0/product/";
        String apiURL = mainURL + barcode;

        // The user's input is concatenated with the main URL and the program makes a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null, response -> {
            try {
                JSONObject productObject = response.getJSONObject("product");
                setCode(productObject.getString("code"));
                setTitle(productObject.getString("product_name"));

                if(productObject.has("nutriscore_grade"))
                    setNutriScore(productObject.getString("nutriscore_grade"));
                if(productObject.has("nova_group"))
                    setNovaGroup(productObject.getString("nova_group"));
                if(productObject.has("nutriscore_grade"))
                    setNutriScore(productObject.getString("nutriscore_grade"));
                if(productObject.has("ecoscore_grade"))
                setEcoScore(productObject.getString("ecoscore_grade"));
                if(productObject.has("image_front_small_url")) {
                    setImageUrl(productObject.getString("image_front_small_url"));
                } else {
                  setImageUrl("https://static.wixstatic.com/media/cd859f_11e62a8757e0440188f90ddc11af8230~mv2.png");
                }

                // Add product on Products database
                setProductFound(true);
                addProduct();
                Toast.makeText(getContext(), "Added to your products!", Toast.LENGTH_LONG).show();

                setProductCard(requireView());
                showProductCardHideSurfaceView();
                clearProductData();

            } catch (JSONException e) {
                e.printStackTrace();
                alertDialogBox();
            }
        }, error -> {
            // If during the request or response an error is occurred, a Toast message will pop up
            Toast.makeText(getContext(), "Something went wrong. Please check your connection.", Toast.LENGTH_LONG).show();
        });
        mQueue.add(request);
    }
    /*--------------------------------------------------------------------------*/

    // Add the scanned product on products list
    public void addProduct() {
        Product testProduct = new Product(getCode(), getTitle(), getNutriScore(), getNovaGroup(), getEcoScore(), "Ingredients", "Nutrients",
                false, System.currentTimeMillis(), getImageUrl());
        insert(testProduct);
    }

    // Insert product on products db
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
    }

    public void scanAgain() {
        textInput_enterBarcode.setText("");
        textView_barcodeResult.setText("");

        setInputBarcode("");
        setIsScanned(false);

        showProductCardHideSurfaceView();
    }

    public void alertDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Product not found. Do you want to scan again?");
        builder.setTitle("Scanning Result");
        builder.setPositiveButton("Try Again", (dialog, which) -> scanAgain()).setNegativeButton("Finish", (dialog, which) -> {
            requireActivity().finish();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

    public void clearProductData() {
        setInputBarcode("");

        setCode("");
        setTitle("");
        setNutriScore("");
        setEcoScore("");
        setNovaGroup("");
        setImageUrl("");
    }

    @Override
    public void onClick(View v) {

    }

    public void setProductCard(View view) {
        TextView textView_addedProductTitle = view.findViewById(R.id.textView_addedProductTitle);
        ImageView imageView_addedProductNutriScore = view.findViewById(R.id.imageView_addedProductNutriScore);
        ImageView imageView_addedProductEcoScore = view.findViewById(R.id.imageView_addedProductEcoScore);
        ImageView imageView_addedNovaGroup = view.findViewById(R.id.imageView_addedNovaGroup);
        ImageView imageView_addedProductImage = view.findViewById(R.id.imageView_addedProductImage);


        textView_addedProductTitle.setText(title);


        try {
            Picasso.get().load(getImageUrl()).resize(66, 75).centerCrop().into(imageView_addedProductImage);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Oops, something is wrong with the photo " + getImageUrl() , Toast.LENGTH_SHORT).show();
        }

        switch (nutriScore) {
            case "1": case "A": case "a":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_a);
                break;
            case "2": case "B": case "b":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_b);
                break;
            case "3": case "C": case "c":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_c);
                break;
            case "4": case "D": case "d":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_d);
                break;
            case "5": case "E": case "e":
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_e);
                break;
            default:
                imageView_addedProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_unknown);
                break;
        }

        switch (ecoScore) {
            case "1": case "A": case "a":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_a);
                break;
            case "2": case "B": case "b":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_b);
                break;
            case "3": case "C": case "c":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_c);
                break;
            case "4": case "D": case "d":
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_d);
                break;
            default:
                imageView_addedProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
                break;
        }

        switch (novaGroup) {
            case "1": case "A": case "a":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_1);
                break;
            case "2": case "B": case "b":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_2);
                break;
            case "3": case "C": case "c":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_3);
                break;
            case "4": case "D": case "d":
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_4);
                break;
            default:
                imageView_addedNovaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
                break;
        }

    }

    public void showProductCardHideSurfaceView() {
        CardView cardView_addedProduct = requireView().findViewById(R.id.cardView_addedProduct);
        SurfaceView surfaceView_camera = requireView().findViewById(R.id.surfaceView_camera);

        if (isProductFound()) {
            cardView_addedProduct.setVisibility(View.VISIBLE);
            surfaceView_camera.setVisibility(View.INVISIBLE);

            setProductFound(false);
        } else {
            cardView_addedProduct.setVisibility(View.INVISIBLE);
            surfaceView_camera.setVisibility(View.VISIBLE);
        }
    }
}



