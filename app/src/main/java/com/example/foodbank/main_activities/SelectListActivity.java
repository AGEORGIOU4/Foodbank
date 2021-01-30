package com.example.foodbank.main_activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

public class SelectListActivity extends AppCompatActivity implements SelectListAdapter.OnItemClickListener, SelectListAdapter.OnItemLongClickListener {
    // Activity states for switching layouts
    private static final int INITIAL_STATE = 1001;
    private static final int CREATE_LIST_STATE = 1002;
    private int CURRENT_STATE = INITIAL_STATE;

    // Recycler View
    RecyclerView recyclerView;
    private final Vector<CustomList> lists = new Vector<>();
    private SelectListAdapter selectListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_list);

        setActionBar();

        // Initialize each list from the db
        lists.clear();
        lists.addAll(getCustomLists());

        //Set recycler view and adapters
        recyclerView = findViewById(R.id.recyclerView_customLists);
        setRecyclerView();


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

    @Override
    protected void onResume() {
        super.onResume();
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
        selectListAdapter = new SelectListAdapter(lists, this, this);
        recyclerView.setAdapter(selectListAdapter);
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

    /*-------------------------------DATABASE-----------------------------------*/
    List<CustomList> getCustomLists() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getCustomLists();
    }

    List<ProductToList> getProductsToLists() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsToLists();
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
        EditText editText_listTitle = findViewById(R.id.editText_listTitle);
        EditText editText_listDescription = findViewById(R.id.editText_listDescription);

        if (editText_listTitle.getText().toString().equals("") ||
                editText_listDescription.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
        } else {
            String name = editText_listTitle.getText().toString();
            String description = editText_listDescription.getText().toString();
            CustomList customList = new CustomList(name, description, 0, System.currentTimeMillis());
            insert(customList);
            lists.clear();
            lists.addAll(getCustomLists());
            selectListAdapter.notifyDataSetChanged();
            //
            // setRecyclerView();
            CURRENT_STATE = INITIAL_STATE;
            switchLayouts();
            hideKeyboard(view);
        }


    }

    /*------------------------------INTERFACES----------------------------------*/
    // Insert product to list
    @Override
    public void itemClicked(View v, int pos, String value) {
        // Get product code and list id on click
        Intent intent = getIntent();
        String code = intent.getStringExtra("extra_product_code");

        // Insert product to list
        ProductToList productToList = new ProductToList(code, value);
        insert(productToList);

        Toast.makeText(this, "New item added to your list", Toast.LENGTH_SHORT).show();
    }

    // Show snackbar for deletion
    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
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
            // deleteItem(pos);
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