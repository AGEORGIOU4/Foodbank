package com.example.foodbank.ui.products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Product;
import com.example.foodbank.R;

import java.util.Vector;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item has an image title, grade, nova group and star
        public TextView textViewTitle;
        public TextView textViewGrade;
        public TextView textViewNovaGroup;
        public CheckBox starredCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewGrade = itemView.findViewById(R.id.textViewGrade);
            this.textViewNovaGroup = itemView.findViewById(R.id.textViewNovaGroup);
            this.starredCheckBox = itemView.findViewById(R.id.starredCheckBox);
        }
    }

    private Vector<Product> listItems;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public ProductsAdapter(final Vector<Product> listItems, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.a3_card_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Product listItem = listItems.get(position);

        // - get element from your data set at this position
        // - replace the contents of the view with that element

        holder.textViewTitle.setText(listItem.getTitle());
//        holder.textViewGrade.setText(listItem.getGrade());
//        holder.textViewNovaGroup.setText(listItem.getNovaGroup());
//        holder.starredCheckBox.setChecked(listItem.isStarred());
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




