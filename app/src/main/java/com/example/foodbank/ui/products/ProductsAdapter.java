package com.example.foodbank.ui.products;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Product;
import com.example.foodbank.R;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private static final String TAG = "testing";

    private final Vector<Product> listItems;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnActionBarMenuClickListener onActionBarMenuClickListener;

    public ProductsAdapter(final Vector<Product> listItems, OnItemClickListener onItemClickListener,
                           OnItemLongClickListener onItemLongClickListener,
                           OnActionBarMenuClickListener onActionBarMenuClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.onActionBarMenuClickListener = onActionBarMenuClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Each products item has an image, a title, nutri-score, eco-score, nova-group, pop up menu and star
        public ImageView imageView_productImage;
        public TextView textView_title;
        public ImageView imageView_nutriScore;
        public ImageView imageView_ecoScore;
        public ImageView imageView_novaGroup;
        public ImageView imageView_popupMenu;
        public CheckBox checkBox_star;

        public ViewHolder(View itemView) {
            super(itemView);

            this.imageView_productImage = itemView.findViewById(R.id.imageView_productImage);
            this.textView_title = itemView.findViewById(R.id.textView_title);
            this.imageView_nutriScore = itemView.findViewById(R.id.imageView_nutriScore);
            this.imageView_ecoScore = itemView.findViewById(R.id.imageView_ecoScore);
            this.imageView_novaGroup = itemView.findViewById(R.id.imageView_novaGroup);
            this.imageView_popupMenu = itemView.findViewById(R.id.imageView_popupMenu);
            this.checkBox_star = itemView.findViewById(R.id.checkBox_star);
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

        String title = listItem.getTitle();
        String nutriScore = listItem.getNutriScore().toUpperCase();
        String novaGroup = listItem.getNovaGroup().toUpperCase();
        String ecoScore = listItem.getEcoScore().toUpperCase();

        Log.d(TAG, title);
        Log.d(TAG, nutriScore);
        Log.d(TAG, novaGroup);
        Log.d(TAG, ecoScore);


        // Set listener on Action Bar Menu
        holder.imageView_popupMenu.setOnClickListener(view -> onActionBarMenuClickListener.onPopupMenuClick(view, position));



        // Get element from your data set at this position
        // Replace the contents of the view with that element
        holder.textView_title.setText(title);

        switch (nutriScore) {
            case "1": case "A": case "a":
                holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_a);
                break;
            case "2": case "B": case "b":
                holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_b);
                break;
            case "3": case "C": case "c":
                holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_c);
                break;
            case "4": case "D": case "d":
                holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_d);
                break;
            case "5": case "E": case "e":
                holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_e);
                break;
            default:
                holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_unknown);
                break;
        }

        switch (ecoScore) {
            case "1": case "A": case "a":
                holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_a);
                break;
            case "2": case "B": case "b":
                holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_b);
                break;
            case "3": case "C": case "c":
                holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_c);
                break;
            case "4": case "D": case "d":
                holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_d);
                break;
            default:
                holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
                break;
        }

        switch (novaGroup) {
            case "1": case "A": case "a":
                holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_1);
                break;
            case "2": case "B": case "b":
                holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_2);
                break;
            case "3": case "C": case "c":
                holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_3);
                break;
            case "4": case "D": case "d":
                holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_4);
                break;
            default:
                holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
                break;
        }
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

    interface OnActionBarMenuClickListener {
        void onPopupMenuClick(View view, int pos);
    }

}




