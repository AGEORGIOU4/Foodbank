package com.example.foodbank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.classes.CustomList;
import com.example.foodbank.classes.Product;
import com.example.foodbank.classes.ProductToList;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class SelectListAdapter extends RecyclerView.Adapter<SelectListAdapter.ViewHolder> {

    private final Vector<CustomList> listItems;

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    // Constructor
    public SelectListAdapter(final Vector<CustomList> listItems, OnItemClickListener onItemClickListener,
                             OnItemLongClickListener onItemLongClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Each products item has an image, a title, nutri-score, eco-score, nova-group, pop up menu and star
        public TextView textView_title;
        public TextView textView_description;
        public TextView textView_numOfProducts;
        public TextView textView_dateCreated;
        public ShapeableImageView imageView_selectedColor;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView_title = itemView.findViewById(R.id.textView_title);
            this.textView_description = itemView.findViewById(R.id.textView_description);
            this.textView_numOfProducts = itemView.findViewById(R.id.textView_numOfProducts);
            this.textView_dateCreated = itemView.findViewById(R.id.textView_dateCreated);
            this.imageView_selectedColor = itemView.findViewById(R.id.imageView_selectedColor);
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public SelectListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list, parent, false);
        return new ViewHolder(v);
    }

    // Set values on view elements
    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        CustomList listItem = listItems.get(position);

        //Convert System.currentTimeMillis to Date
        long noteMilliseconds = listItem.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        Date resultDate = new Date(noteMilliseconds);

        String title;
        String description;
        int numOfProducts = 0;
        String color = "default";
        int colorInteger = 0;

        //-----------------------GET VALUES-----------------------//
        if (listItem.getName() != null && !listItem.getName().equals("")) {
            title = listItem.getName().substring(0, 1).toUpperCase() + listItem.getName().substring(1);
        } else {
            title = "Unknown";
        }

        if (listItem.getDescription() != null && !listItem.getDescription().equals("")) {
            description = listItem.getDescription();
        } else {
            description = "Unknown";
        }

        // Set number of products for each list
        for (int i = 0; i < getLists(listItem.getId()).size(); i++) {
            for (int j = 0; j < getAllProductsSortedByTimestamp().size(); j++) {
                // Check if product is still in the Main products list
                if (getLists(listItem.getId()).get(i).getProduct_code().equals(getAllProductsSortedByTimestamp().get(j).getBarcode())) {
                    numOfProducts++;
                }
            }
        }

        if (listItem.getColor() != null && !listItem.getColor().equals("")) {
            color = listItem.getColor();
        } else {
            color = "Default";
        }

        // Set colors
        switch (color) {
            case "Red":
                colorInteger = 0xFFE57373;
                break;
            case "Blue":
                colorInteger = 0xFF73B6E5;
                break;
            case "Green":
                colorInteger = 0xFF93E7A4;
                break;
            case "Yellow":
                colorInteger = 0xFFEFE29B;
                break;
            default:
                colorInteger = 0;

        }

        //-------------------SET HOLDER VALUES--------------------//
        holder.textView_title.setText(title);
        holder.textView_description.setText(description);
        holder.textView_numOfProducts.setText(String.valueOf(numOfProducts) + " items");
        holder.textView_dateCreated.setText(sdf.format(resultDate));
        holder.imageView_selectedColor.setBackgroundColor(colorInteger);

        // Set listeners on Click, on Long Click Action Bar Menu
        holder.itemView.setOnClickListener(v -> onItemClickListener.itemClicked(v, position, listItem.getId()));
        holder.itemView.setOnLongClickListener(v -> onItemLongClickListener.itemLongClicked(v, position, listItem.getId()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // Interfaces
    public interface OnItemClickListener {
        void itemClicked(View v, int pos, int list_id);
    }

    public interface OnItemLongClickListener {
        boolean itemLongClicked(View v, int pos, int list_id);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<ProductToList> getLists(int list_id) {
        return ProductsRoomDatabase.getDatabase(context).productsDao().getLists(list_id);
    }

    List<Product> getAllProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(context).productsDao().getProductsSortedByTimestamp();
    }

}




