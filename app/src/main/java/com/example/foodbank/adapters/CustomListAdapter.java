package com.example.foodbank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.classes.CustomList;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    private final Vector<CustomList> listItems;

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    // Constructor
    public CustomListAdapter(final Vector<CustomList> listItems, OnItemClickListener onItemClickListener,
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

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView_title = itemView.findViewById(R.id.textView_title);
            this.textView_description = itemView.findViewById(R.id.textView_description);
            this.textView_numOfProducts = itemView.findViewById(R.id.textView_numOfProducts);
            this.textView_dateCreated = itemView.findViewById(R.id.textView_dateCreated);
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
        int numOfProducts;
        String dateCreated;

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

        numOfProducts = listItem.getNumOfProducts();


        //-------------------SET HOLDER VALUES--------------------//
        holder.textView_title.setText(title);
        holder.textView_description.setText(description);
        holder.textView_numOfProducts.setText(String.valueOf(numOfProducts));
        holder.textView_dateCreated.setText(sdf.format(resultDate));

        // Set listeners on Click, on Long Click Action Bar Menu
        holder.itemView.setOnClickListener(v -> onItemClickListener.itemClicked(v, position, String.valueOf(listItem.getId())));
        holder.itemView.setOnLongClickListener(v -> onItemLongClickListener.itemLongClicked(v, position, String.valueOf(listItem.getId())));


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // Interfaces
    public interface OnItemClickListener {
        void itemClicked(View v, int pos, String value);
    }

    public interface OnItemLongClickListener {
        boolean itemLongClicked(View v, int pos, String value);
    }

}




