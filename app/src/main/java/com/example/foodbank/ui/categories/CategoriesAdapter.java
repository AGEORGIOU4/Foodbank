package com.example.foodbank.ui.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Category;
import com.example.foodbank.R;

import java.util.Vector;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item has a 1 title (string)
        public Button gridView_categories_btn;

        public ViewHolder(View itemView) {
            super(itemView);

            this.gridView_categories_btn = itemView.findViewById(R.id.gridView_categories_btn);
        }
    }

    private Vector<Category> listItems;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public CategoriesAdapter(final Vector<Category> listItems, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.a2_card_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Category listItem = listItems.get(position);

        // - get element from your data set at this position
        // - replace the contents of the view with that element
        holder.gridView_categories_btn.setText(listItem.getTitle());

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

}




