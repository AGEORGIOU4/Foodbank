package com.example.foodbank.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Vector;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> implements Filterable {

    private Vector<Category> listItems;
    private Vector<Category> listItemsAll;

    Filter filter = new Filter() {
        // run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Vector<Category> filteredList = new Vector<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(listItemsAll);
            } else {
                for (Category category : listItemsAll) {
                    if (category.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(category);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        // run on background thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            listItems.clear();
            listItems.addAll((Collection<? extends Category>) filterResults.values);
            notifyDataSetChanged();
        }
    };
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CategoriesAdapter(final Vector<Category> listItems, OnItemClickListener onItemClickListener) {
        this.listItems = listItems;
        this.listItemsAll = new Vector<>(listItems);
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c3_card_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        Category listItem = listItems.get(position);

        String title = "";
        int products_number = 0;
        // Capitalize First letter
        if (listItem.getName() != null && !listItem.getName().equals("")) {
            title = listItem.getName().substring(0, 1).toUpperCase() + listItem.getName().substring(1);

        } else {
            title = "Unknown";
        }
        holder.textView_title.setText(title);

        if (listItem.getProducts() != 0) {
            products_number = listItem.getProducts();

        } else {
            products_number = '\0';
        }

        holder.textView_products.setText(String.valueOf(products_number));

        // Set listeners on Click and pass its values
        holder.itemView.setOnClickListener(v -> onItemClickListener.itemClicked(v, position,
                listItem.getId(), listItem.getName(), listItem.getProducts()));

        // Get element from your data set at this position
        // Replace the contents of the view with that element

        //   holder.textView_products.setText(products_number);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public interface OnItemClickListener {
        void itemClicked(View v, int pos, String id, String categoryName, int productNumber);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Each category item has a title and products number
        public TextView textView_title;
        public TextView textView_products;


        public ViewHolder(View itemView) {
            super(itemView);
            this.textView_title = itemView.findViewById(R.id.textView_title);
            this.textView_products = itemView.findViewById(R.id.textView_products);
        }
    }
}




