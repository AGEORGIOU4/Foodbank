package com.example.foodbank.ui.addProduct;

import android.Manifest;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddProductFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "test";

    // QR Code Scanner Elements
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static boolean isScanned;

    // JSON Request Controller
    private static boolean productFound;
    // Barcode
    private static String BARCODE;
    RequestQueue mQueue;
    private SurfaceView surfaceView_camera;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private TextView textView_barcodeResult;
    private String barcodeData;
    // Layout elements
    private ToneGenerator toneGen1;
    private EditText textInput_enterBarcode;
    // Product attributes
    private String code = "";
    private String title = "";
    private String nutriScore = "";
    private String novaGroup = "";
    private String ecoScore = "";

    private static boolean isIsScanned() {
        return isScanned;
    }

    private static void setIsScanned(boolean isScanned) {
        AddProductFragment.isScanned = isScanned;
    }

    public static String getBARCODE() {
        return BARCODE;
    }

    public static void setBARCODE(String BARCODE) {
        AddProductFragment.BARCODE = BARCODE;
    }

    public static boolean isProductFound() {
        return productFound;
    }

    public static void setProductFound(boolean productFound) {
        AddProductFragment.productFound = productFound;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.e1_fragment_add_product, container, false);

        // Layout elements
        Button buttonAddProduct = root.findViewById(R.id.button_addProduct);
        Button button_scanProduct = root.findViewById(R.id.button_scanProduct);

        // Implement an HTTP request using Volley library
        this.mQueue = Volley.newRequestQueue(getContext());
        setProductFound(false);

        textInput_enterBarcode = root.findViewById(R.id.textInput_enterBarcode);

        // QR Code Scanner
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView_camera = root.findViewById(R.id.surfaceView_camera);
        textView_barcodeResult = root.findViewById(R.id.textView_barcodeResult);
        setIsScanned(false);
        initialiseDetectorsAndSources();

        // Barcode input checks (not empty), adding a product if found and jsonParsing and
        buttonAddProduct.setOnClickListener(view -> barcodeHandler(root));
        // Scan Again
        button_scanProduct.setOnClickListener(v -> scanAgain());

        return root;
    }

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView_camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    // maybe activity check about activity combat000000000000000000000
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView_camera.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    textView_barcodeResult.post(() -> {

                        // Get Barcode data from Scanner
                        if (!isIsScanned()) {
                            barcodeData = barcodes.valueAt(0).displayValue;
                            textView_barcodeResult.setText(barcodeData);
                            setIsScanned(true);
                            setBARCODE(barcodeData);
                            jsonParse();
                        }
                    });
                }
            }
        });
    }

    public void barcodeHandler(View view) {
        this.textInput_enterBarcode = view.findViewById(R.id.textInput_enterBarcode);

        if (textInput_enterBarcode.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Please enter barcode", Toast.LENGTH_SHORT).show();
        } else {
            setBARCODE(textInput_enterBarcode.getText().toString());
            jsonParse();
        }
    }

    private void jsonParse() {
        hideKeyboard();

        String barcode = getBARCODE();
        String mainURL = "https://world.openfoodfacts.org/api/v0/product/";
        String apiURL = mainURL + barcode;

        // The user's input is concatenated with the main URL and the program makes a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null,
                response -> {
                    try {
                        // if the response is successful we get useful data from the JSON file
                        JSONObject productObject = response.getJSONObject("product");

                        code = productObject.getString("code");
                        title = productObject.getString("product_name");
                        nutriScore = productObject.getString("nutriscore_grade").toUpperCase();
                        novaGroup = productObject.getString("nova_group").toUpperCase();
                        ecoScore = productObject.getString("ecoscore_grade").toUpperCase();

                        // Set product found controller / Show product card / Hide Surface View
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        setProductFound(true);
                        setProductCard(getView());
                        showProductCardHideSurfaceView();

                        // Add product on Products database
                        addProduct();
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // If during the request or response an error is occurred, a Toast message will pop up
            Toast.makeText(getContext(), "Something went wrong. Please check your connection.", Toast.LENGTH_LONG).show();
        });
        mQueue.add(request);
    }

    // Add the scanned product on products list
    public void addProduct() {
        Product testProduct = new Product(code, title, nutriScore, novaGroup, ecoScore, "Ingredients", "Nutrients", false, System.currentTimeMillis());
        insert(testProduct);
    }

    // Insert product on products db
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
    }

    @Override
    public void onClick(View v) {

    }

    public void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public void setProductCard(View view) {
        TextView textView_title = view.findViewById(R.id.textView_title);
        ImageView imageView_grade = view.findViewById(R.id.imageView_nutriScore);
        ImageView imageView_ecoScore = view.findViewById(R.id.imageView_ecoScore);
        ImageView imageView_novaGroup = view.findViewById(R.id.imageView_novaGroup);


        CheckBox starredCheckBox = view.findViewById(R.id.checkBox_star);

        textView_title.setText(title);

        switch (nutriScore) {
            case "A":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_a);
                break;
            case "B":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_b);
                break;
            case "C":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_c);
                break;
            case "D":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_d);
                break;
            case "E":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_e);
                break;
            default:
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_unknown);
                break;
        }

        switch (ecoScore) {
            case "1":
            case "A":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_a);
                break;
            case "2":
            case "B":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_b);
                break;
            case "3":
            case "C":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_c);
                break;
            case "4":
            case "D":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_d);
                break;
            default:
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
                break;
        }

        switch (novaGroup) {
            case "1":
            case "A":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_1);
                break;
            case "2":
            case "B":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_2);
                break;
            case "3":
            case "C":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_3);
                break;
            case "4":
            case "D":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_4);
                break;
            default:
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
                break;
        }
    }

    public void showProductCardHideSurfaceView() {
        CardView cardView_product = getView().findViewById(R.id.cardView_product);
        SurfaceView surfaceView_camera = getView().findViewById(R.id.surfaceView_camera);

        if (isProductFound()) {
            cardView_product.setVisibility(View.VISIBLE);
            surfaceView_camera.setVisibility(View.INVISIBLE);
        } else {
            cardView_product.setVisibility(View.INVISIBLE);
            surfaceView_camera.setVisibility(View.VISIBLE);
        }
    }

    public void scanAgain() {
        textInput_enterBarcode.setText("");
        setBARCODE("");
        textView_barcodeResult.setText(getBARCODE());
        setIsScanned(false);
        setProductFound(false);
        showProductCardHideSurfaceView();
    }
}



