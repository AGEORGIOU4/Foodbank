package com.example.foodbank.fragments.custom_lists;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.adapters.MyProductsAdapter;
import com.example.foodbank.classes.CustomList;
import com.example.foodbank.classes.Product;
import com.example.foodbank.classes.ProductToList;
import com.example.foodbank.classes.Settings;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.example.foodbank.db.SettingsRoomDatabase;
import com.example.foodbank.main_activities.SelectListActivity;
import com.example.foodbank.main_activities.ViewProductActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Vector;

public class CustomListsFragment extends Fragment implements MyProductsAdapter.OnItemClickListener, MyProductsAdapter.OnItemLongClickListener,
        MyProductsAdapter.OnActionBarMenuClickListener, MyProductsAdapter.OnStarClickListener {

    // Layout
    Button button_addProductsOnEmptyList;
    TabLayout tabLayout_customLists;

    // Recycler View
    RecyclerView recyclerView;
    private final Vector<CustomList> lists = new Vector<>();
    private final Vector<Product> productsInLists = new Vector<>();
    private MyProductsAdapter selectListAdapter;

    // Tab Controller
    private int TAB_SELECTION = 1001;

    // Variables
    private int SELECTED_LIST_ID = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_custom_lists, container, false);

        tabLayout_customLists = root.findViewById(R.id.tabLayout_customLists);

        // Default selected list
        SELECTED_LIST_ID = getCustomLists().get(0).getId();

        // Get data from DB and populate lists vector
        initializeLists(root);

        // Dynamically add each list on tab layout
        setTabLayout(root);

        setRecyclerView(root);

        populateProductsForEachList(root);

        // Floating button for creating a custom list
        addListAction(root);


        return root;
    }

    @Override
    public void onResume() {
        loadSettings();

        // re-initialize lists, notify adapter
        initializeLists(requireView());
        setTabLayout(requireView());
        setRecyclerView(requireView());
        super.onResume();
    }

    /*---------------------------------LISTS------------------------------------*/
    public void initializeLists(View view) {
        // Initialize each list from the db and set tab layout
        lists.clear();
        lists.addAll(getCustomLists());
    }

    public void populateProductsForEachList(View view) {
        // Clear products list
        productsInLists.clear();

        // Click must be currentListIDd
        for (int i = 0; i < getLists(SELECTED_LIST_ID).size(); i++) {

            // Check if product is in the main list
            for (int j = 0; j < getProductsSortedByTimestamp().size(); j++) {
                if (getLists(SELECTED_LIST_ID).get(i).getProduct_code().equals(getProductsSortedByTimestamp().get(j).getBarcode())) {
                    Product product = getProductsSortedByTimestamp().get(j);
                    productsInLists.add(product);
                }
            }
        }
        setRecyclerView(view);
    }

    public void addListAction(View view) {
        FloatingActionButton floatingActionButton_AddList = view.findViewById(R.id.floatingActionButton_AddList);
        floatingActionButton_AddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SelectListActivity.class);
                intent.putExtra("extra_set_create_list_view", 1002);
                startActivity(intent);
            }
        });
    }

    /*--------------------------------LAYOUT------------------------------------*/
    public void setRecyclerView(View view) {
        //Set recycler view and adapter
        recyclerView = view.findViewById(R.id.recyclerView_customLists);
        // Item helper for swipe events
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set adapters for each sorting selection
        selectListAdapter = new MyProductsAdapter(productsInLists, this, this, this, this);
        recyclerView.setAdapter(selectListAdapter);
    }

    public void setTabLayout(View view) {
        // Clear tab layout
        tabLayout_customLists.removeAllTabs();

        // Set list name on each tab
        try {
            for (int i = 0; i < getCustomLists().size(); i++) {
                tabLayout_customLists.addTab(tabLayout_customLists.newTab().setText(lists.get(i).getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tabLayout_customLists.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int counter = tab.getPosition();
                SELECTED_LIST_ID = getCustomLists().get(counter).getId();

                populateProductsForEachList(view);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<CustomList> getCustomLists() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getCustomListsSortedByTimestamp();
    }

    List<Product> getProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTimestamp();
    }

    List<ProductToList> getProductsToLists(int list_id, String code) {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsToLists(list_id, code);
    }

    List<ProductToList> getLists(int list_id) {
        return ProductsRoomDatabase.getDatabase(requireContext()).productsDao().getLists(list_id);
    }

    List<ProductToList> getLists() {
        return ProductsRoomDatabase.getDatabase(requireContext()).productsDao().getLists();
    }

    /*------------------------------INTERFACES----------------------------------*/
    // View Product Individually
    @Override
    public void itemClicked(View v, int pos, String value) {
        Intent intent = new Intent(getActivity(), ViewProductActivity.class);
        intent.putExtra("extra_products_code", value);
        startActivity(intent);
    }

    // Show snackbar for deletion
    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        Snackbar.make(v, "Swipe to delete", Snackbar.LENGTH_SHORT).show();
        return true;
    }

    // Show options menu
    @Override
    public void onPopupMenuClick(View view, int pos, String code, final String title, String nutriScore,
                                 String ecoScore, String novaGroup, boolean isStarred) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.product_card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            // set actions for each case on the popup menu
            if (item.getItemId() == R.id.menu_viewProduct) {
                Intent intent = new Intent(getActivity(), ViewProductActivity.class);
                intent.putExtra("extra_products_code", code);
                startActivity(intent);
                return true;
            }
            // Add to list - start select list activity and pass product barcode
            if (item.getItemId() == R.id.menu_addToList) {
                Intent intent = new Intent(requireActivity(), SelectListActivity.class);
                intent.putExtra("extra_product_code", code);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.menu_editProduct) {

            }

            if (item.getItemId() == R.id.menu_deleteProduct) {
                //deleteItem(pos);
                return true;
            }
            return false;
        });
        popup.show();
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

    // Handle checkbox(star) clicks
    @Override
    public void itemClicked(View v, int pos, boolean checked) {

    }

    /*-------------------------------SETTINGS-----------------------------------*/
    List<Settings> getSettings() {
        return SettingsRoomDatabase.getDatabase(requireContext()).settingsDao().getSettings();
    }

    public void loadSettings() {
        List<Settings> settings = getSettings();
        boolean theme = settings.get(0).isDarkMode();
        System.out.println("theme is " + theme);

        if (theme) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
        }
    }
}