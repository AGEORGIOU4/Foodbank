package com.example.foodbank.main_activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.adapters.SelectListAdapter;
import com.example.foodbank.classes.CustomList;
import com.example.foodbank.classes.ProductToList;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Vector;

public class SelectListActivity extends AppCompatActivity implements SelectListAdapter.OnItemClickListener, SelectListAdapter.OnItemLongClickListener,
        AdapterView.OnItemSelectedListener {
    // Activity states for switching layouts
    private static final int INITIAL_STATE = 1001;
    private static final int CREATE_LIST_STATE = 1002;
    private int CURRENT_STATE = INITIAL_STATE;

    // Layout
    AppCompatSpinner spinner_listsOptions;

    // Recycler View
    RecyclerView recyclerView;
    private final Vector<CustomList> listsByDateCreated = new Vector<>();
    private final Vector<CustomList> listsByName = new Vector<>();
    private SelectListAdapter selectListAdapterByDateCreated;
    private SelectListAdapter selectListAdapterByName;

    // Lists controller
    private int LISTS_CONTROLLER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_list);

        setActionBar();

        // Initialize lists
        initializeLists();


        //Set recycler view and adapters
        recyclerView = findViewById(R.id.recyclerView_customLists);
        setRecyclerView();

        // Switch layout when user wants to create a new list
        createListView();

        // Change state if user wants to create a list (extras from custom list to switch frame layout)
        getExtras();

        setSpinner();

    }

    @Override
    protected void onResume() {
        // Change state if user wants to create a list (extras from custom list to switch frame layout)
        initializeLists();
        getExtras();
        super.onResume();
    }

    /*---------------------------------LISTS------------------------------------*/
    public void initializeLists() {
        // Initialize each list from the db
        listsByDateCreated.clear();
        listsByDateCreated.addAll(getCustomListsSortedByTimestamp());
        listsByName.clear();
        listsByName.addAll(getCustomListsSortedByName());
    }
    /*--------------------------------LAYOUT------------------------------------*/
    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Lists");
            actionBar.setBackgroundDrawable(getDrawable(R.drawable.action_bar_bc));
        }
    }

    public void setRecyclerView() {
        // Item helper for swipe events
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set adapters for each sorting selection
        selectListAdapterByDateCreated = new SelectListAdapter(listsByDateCreated, this, this);
        selectListAdapterByName = new SelectListAdapter(listsByName, this, this);
        recyclerView.setAdapter(selectListAdapterByDateCreated);
    }

    public void switchLayouts() {
        FrameLayout frameLayout_showSelectLists = findViewById(R.id.frameLayout_showSelectLists);
        FrameLayout frameLayout_showCreateList = findViewById(R.id.frameLayout_showCreateList);

        switch (CURRENT_STATE) {
            case INITIAL_STATE:
                frameLayout_showSelectLists.setVisibility(View.VISIBLE);
                frameLayout_showCreateList.setVisibility(View.INVISIBLE);
                break;
            case CREATE_LIST_STATE:
                frameLayout_showSelectLists.setVisibility(View.INVISIBLE);
                frameLayout_showCreateList.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void createListView() {
        // Create a new list form
        FloatingActionButton floatingActionButton_AddList = findViewById(R.id.floatingActionButton_AddList);
        floatingActionButton_AddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_STATE = CREATE_LIST_STATE;

                // Switch layout if user wants to create a list
                switchLayouts();
            }
        });

        // Go back button
        Button button_goBack = findViewById(R.id.button_goBack);
        button_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_STATE = INITIAL_STATE;
                switchLayouts();
            }
        });
    }

    /*-------------------------------SPINNER-----------------------------------*/
    public void setSpinner() {
        spinner_listsOptions = findViewById(R.id.spinner_listsOptions);
        String[] spinnerOptions = {"Date Created", "Title"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        spinner_listsOptions.setAdapter(spinnerAdapter);
        spinner_listsOptions.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedOption = parent.getItemAtPosition(position).toString();
        // Set recycler view adapter for each selection
        if (selectedOption.equals("Date Created")) {
            recyclerView.setAdapter(selectListAdapterByDateCreated);
            // Set controller to keep track of the order for deletion of list
            LISTS_CONTROLLER = 1;

        } else if (selectedOption.equals("Title")) {
            recyclerView.setAdapter(selectListAdapterByName);
            // Set controller to keep track of the order for deletion of list
            LISTS_CONTROLLER = 2;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<CustomList> getCustomListsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getCustomListsSortedByTimestamp();
    }

    List<CustomList> getCustomListsSortedByName() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getCustomListsSortedByName();
    }

    List<ProductToList> getProductsToLists(int list_id, String code) {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsToLists(list_id, code);
    }

    // Insert product to list
    void insert(ProductToList productToList) {
        ProductsRoomDatabase.getDatabase(this).productsDao().insert(productToList);
        Toast.makeText(this, "New item added to your list", Toast.LENGTH_LONG).show();
    }

    // Insert list
    void insert(CustomList customList) {
        ProductsRoomDatabase.getDatabase(this).productsDao().insert(customList);
    }

    public void createList(View view) {
        // User form for creating a list
        EditText editText_listTitle = findViewById(R.id.editText_listTitle);
        EditText editText_listDescription = findViewById(R.id.editText_listDescription);

        if (editText_listTitle.getText().toString().equals("") ||
                editText_listDescription.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
        } else {
            // Get input values
            String name = editText_listTitle.getText().toString();
            String description = editText_listDescription.getText().toString();

            // Create a new object of type Custom List
            CustomList customList = new CustomList(name, description, 0, System.currentTimeMillis());
            insert(customList);

            listsByDateCreated.clear();
            listsByDateCreated.addAll(getCustomListsSortedByTimestamp());
            selectListAdapterByDateCreated.notifyDataSetChanged();

            listsByName.clear();
            listsByName.addAll(getCustomListsSortedByName());
            selectListAdapterByName.notifyDataSetChanged();

            // Change controller state and switch layout
            CURRENT_STATE = INITIAL_STATE;
            switchLayouts();

            hideKeyboard(view);

            // Clear input field data
            editText_listTitle.setText("");
            editText_listDescription.setText("");
        }
    }

    void delete(final CustomList customList) {
        ProductsRoomDatabase.getDatabase(this).productsDao().delete(customList);
    }

    // Delete list
    void deleteItem(int pos) {
        CustomList tmpCustomList;

        // Create a temp product if user wants to undo, check the selected list and delete item

        // Step 1 - Get item from selected list, delete it and set a tmpProduct for undo case
        switch (LISTS_CONTROLLER) {
            case 1:
                tmpCustomList = listsByDateCreated.get(pos);
                delete(listsByDateCreated.get(pos));
                break;
            case 2:
                tmpCustomList = listsByName.get(pos);
                delete(listsByName.get(pos));
                break;

            default:
                tmpCustomList = listsByDateCreated.get(pos);
                break;
        }


        // Step 2  Clear the list and update it
        listsByDateCreated.clear();
        listsByName.clear();

        // Step 3 -  Update list content
        listsByDateCreated.addAll(getCustomListsSortedByTimestamp());
        listsByName.addAll(getCustomListsSortedByName());

        // Step 4 -  Notify adapter
        selectListAdapterByDateCreated.notifyDataSetChanged();
        selectListAdapterByName.notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(findViewById(R.id.frameLayout_showSelectLists), "You have deleted '" + tmpCustomList.getName() + "'", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    // Step 1 - Replace (insert back the tmp product as the 'deleted')
                    insert(tmpCustomList);

                    // Clear the list and update it
                    // Step 2 - Clear all lists
                    listsByDateCreated.clear();
                    listsByName.clear();

                    // Step 3 -  Update list content
                    listsByDateCreated.addAll(getCustomListsSortedByTimestamp());
                    listsByName.addAll(getCustomListsSortedByName());

                    // Step 4 - Notify adapter
                    selectListAdapterByDateCreated.notifyDataSetChanged();
                    selectListAdapterByName.notifyDataSetChanged();

                });
        snackbar.show();
    }

    /*--------------------------------EXTRAS------------------------------------*/
    public void getExtras() {
        // Change state if user wants to create a list
        Intent intent = getIntent();
        if (intent.hasExtra("extra_set_create_list_view")) {
            CURRENT_STATE = intent.getIntExtra("extra_set_create_list_view", 1001);
        }

        switchLayouts();
    }

    /*------------------------------INTERFACES----------------------------------*/
    // Insert product to list
    @Override
    public void itemClicked(View v, int pos, int list_id) {
        // Get product code and list id on click
        Intent intent = getIntent();
        if (intent.hasExtra("extra_product_code")) {
            // Get code from clicked product
            String code = intent.getStringExtra("extra_product_code");
            int counterForEmptyList = 0;
            int counter = 0;

            //Check if this product is not already added on the clicked list
            if (getProductsToLists(list_id, code).size() > 0) {
                Toast.makeText(this, "This item is already added on this list", Toast.LENGTH_SHORT).show();
            } else {
                ProductToList productToList = new ProductToList(code, list_id);
                insert(productToList);
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                selectListAdapterByDateCreated.notifyDataSetChanged();
            }
        }
    }

    // Show snackbar for deletion
    @Override
    public boolean itemLongClicked(View v, int pos, int list_id) {
        Snackbar.make(v, "Swipe to delete", Snackbar.LENGTH_SHORT).show();
        return true;
    }

    // Delete Item on Swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            deleteItem(pos);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*--------------------------------------------------------------------------*/
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}