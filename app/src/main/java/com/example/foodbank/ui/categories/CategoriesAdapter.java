package com.example.foodbank.ui.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final Vector<Category> listItems;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public CategoriesAdapter(final Vector<Category> listItems, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c2_card_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Category listItem = listItems.get(position);

        // Get element from your data set at this position
        // Replace the contents of the view with that element
        holder.button_gridView_categories.setText(listItem.getName() + "\n" + "(" + listItem.getProducts() + ")");
        //holder.gridView_categories_btn.setOnClickListener(v -> onItemClickListener.itemClicked(v, position, listItem));
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Each category item has a title and a timestamp (Button, long)
        public Button button_gridView_categories;

        public ViewHolder(View itemView) {
            super(itemView);

            this.button_gridView_categories = itemView.findViewById(R.id.button_gridView_categories);
        }
    }
}




