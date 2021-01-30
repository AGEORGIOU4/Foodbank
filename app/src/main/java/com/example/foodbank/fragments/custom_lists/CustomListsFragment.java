package com.example.foodbank.fragments.custom_lists;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.adapters.CustomListAdapter;
import com.example.foodbank.classes.CustomList;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.example.foodbank.main_activities.ViewProductActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Vector;

public class CustomListsFragment extends Fragment implements CustomListAdapter.OnItemClickListener, CustomListAdapter.OnItemLongClickListener {

    // Recycler View
    RecyclerView recyclerView;
    private final Vector<CustomList> lists = new Vector<>();

    private CustomListAdapter customListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_custom_lists, container, false);

        // Initialize each list from the db
        lists.clear();
        lists.addAll(getCustomLists());

        //Set recycler view and adapters
        recyclerView = root.findViewById(R.id.recyclerView_customLists);
        setRecyclerView();

        TabLayout tabLayout_customLists = root.findViewById(R.id.tabLayout_customLists);
        tabLayout_customLists.addTab(tabLayout_customLists.newTab().setText("Tab 1"));
        tabLayout_customLists.addTab(tabLayout_customLists.newTab().setText("Tab 2"));
        tabLayout_customLists.addTab(tabLayout_customLists.newTab().setText("Tab 3"));

        return root;
    }

    /*--------------------------------LAYOUT------------------------------------*/
    public void setRecyclerView() {
        // Item helper for swipe events
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set adapters for each sorting selection
        customListAdapter = new CustomListAdapter(lists, this, this);
        recyclerView.setAdapter(customListAdapter);
    }

    public void setTabs() {}

    /*-------------------------------DATABASE-----------------------------------*/
    List<CustomList> getCustomLists() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getCustomLists();
    }

    /*-----------------------------INTERFACES----------------------------------*/
    // View Product
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
}