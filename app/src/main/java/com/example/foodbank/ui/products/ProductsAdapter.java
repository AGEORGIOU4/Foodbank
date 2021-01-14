package com.example.foodbank.ui.products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Product;
import com.example.foodbank.R;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private final Vector<Product> listItems;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public ProductsAdapter(final Vector<Product> listItems, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Each products item has a title, nutri-score, eco-score, nova-group, timestamp and star
        public TextView textViewTitle;
        public CheckBox starredCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.starredCheckBox = itemView.findViewById(R.id.starredCheckBox);
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.d2_card_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Product listItem = listItems.get(position);

        // Get element from your data set at this position
        // Replace the contents of the view with that element
        holder.textViewTitle.setText(listItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    interface OnItemClickListener {
        void itemClicked(View v, int pos, String value);
    }

    interface OnItemLongClickListener {
        boolean itemLongClicked(View v, int pos, String value);
    }

}




