package com.example.foodbank.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;

import java.util.List;
import java.util.Vector;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnItemClickListener, ProductsAdapter.OnItemLongClickListener,
ProductsAdapter.OnActionBarMenuClickListener {

    // Recycler View
    public Vector<Product> productsList = new Vector<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.d1_fragment_products, container, false);

        // Initialize each products from the db to the productsList
        productsList.addAll(getAllProductsSortedByTimestamp());

        // Recycler View implementation
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_products);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        ProductsAdapter adapter = new ProductsAdapter(productsList, this, this, this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    List<Product> getAllProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTitle();
    }

    List<Product> getAllProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTimestamp();
    }

    @Override
    public void itemClicked(View v, int pos, String value) {

    }

    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        return false;
    }

    @Override
    public void onPopupMenuClick(View view, int pos) {
        Product listItem = productsList.get(pos);
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_product_card, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            //do your things in each of the following cases
            if (item.getItemId() == R.id.menu_addToList) {
                Toast.makeText(getContext(), "Item " + pos + " clicked! It's the " + listItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });
        popup.show();
    }
}