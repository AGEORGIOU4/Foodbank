package com.example.foodbank.ui.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Vector;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> implements Filterable {

    private final Vector<Product> listItems;
    private Vector<Product> listItemsAll;

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnStarClickListener onStarClickListener;
    private OnActionBarMenuClickListener onActionBarMenuClickListener;

    public MyProductsAdapter(final Vector<Product> listItems, OnItemClickListener onItemClickListener,
                             OnItemLongClickListener onItemLongClickListener,
                             OnActionBarMenuClickListener onActionBarMenuClickListener, OnStarClickListener onStarClickListener) {
        this.listItems = listItems;
        this.listItemsAll = new Vector<>(listItems);
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.onActionBarMenuClickListener = onActionBarMenuClickListener;
        this.onStarClickListener = onStarClickListener;
    }

    public MyProductsAdapter(final Vector<Product> listItems, OnItemClickListener onItemClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.d2_card_product, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Each products item has an image, a title, nutri-score, eco-score, nova-group, pop up menu and star
        public TextView textView_title;
        public ImageView imageView_nutriScore;
        public ImageView imageView_ecoScore;
        public ImageView imageView_novaGroup;
        public ImageView imageView_productImage;
        public CheckBox checkBox_star;
        public ImageView imageView_popupMenu;

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

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        Product listItem = listItems.get(position);

        String title = "Unknown";
        // Capitalize First letter
        if (listItem.getTitle() != null && !listItem.getTitle().equals("")) {
            title = listItem.getTitle().substring(0, 1).toUpperCase() + listItem.getTitle().substring(1);
        }
        String nutriScore = listItem.getNutriScore();
        String novaGroup = listItem.getNovaGroup();
        String ecoScore = listItem.getEcoScore();
        String imageUrl = listItem.getImageUrl();

        // Set listeners on Click, on Long Click Action Bar Menu
        holder.itemView.setOnClickListener(v -> onItemClickListener.itemClicked(v, position, listItem.getBarcode()));
        holder.itemView.setOnLongClickListener(v -> onItemLongClickListener.itemLongClicked(v, position, listItem.getTitle()));
        holder.checkBox_star.setOnClickListener(v -> onStarClickListener.itemClicked(v, position, listItem.isStarred()));

        try {
            holder.imageView_popupMenu.setOnClickListener(view -> onActionBarMenuClickListener.onPopupMenuClick(view, position, listItem.getBarcode(),
                    listItem.getTitle(), nutriScore, ecoScore, novaGroup, listItem.isStarred()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Get element from your data set at this position
        // Replace the contents of the view with that element
        holder.textView_title.setText(title);

        try {
            switch (nutriScore) {
                case "1":
                case "A":
                case "a":
                    holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_a);
                    break;
                case "2":
                case "B":
                case "b":
                    holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_b);
                    break;
                case "3":
                case "C":
                case "c":
                    holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_c);
                    break;
                case "4":
                case "D":
                case "d":
                    holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_d);
                    break;
                case "5":
                case "E":
                case "e":
                    holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_e);
                    break;
                default:
                    holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_unknown);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            switch (ecoScore) {
                case "1":
                case "A":
                case "a":
                    holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_a);
                    break;
                case "2":
                case "B":
                case "b":
                    holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_b);
                    break;
                case "3":
                case "C":
                case "c":
                    holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_c);
                    break;
                case "4":
                case "D":
                case "d":
                    holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_d);
                    break;
                default:
                    holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            switch (novaGroup) {
                case "1":
                case "A":
                case "a":
                    holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_1);
                    break;
                case "2":
                case "B":
                case "b":
                    holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_2);
                    break;
                case "3":
                case "C":
                case "c":
                    holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_3);
                    break;
                case "4":
                case "D":
                case "d":
                    holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_4);
                    break;
                default:
                    holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!listItem.getImageUrl().equals(""))
                Picasso.get().load(imageUrl).resize(66, 75).centerCrop().into(holder.imageView_productImage);
            else {

                // Set default photo in case no photo is found (cross check with API call)
                Picasso.get().load("https://static.wixstatic.com/media/cd859f_11e62a8757e0440188f90ddc11af8230~mv2.png")
                        .resize(66, 75).centerCrop().into(holder.imageView_productImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Oops, this should happened.", Toast.LENGTH_SHORT).show();
        }

        holder.checkBox_star.setChecked(listItem.isStarred());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        // run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Vector<Product> filteredList = new Vector<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(listItemsAll);
            } else {
                for (Product product : listItemsAll) {
                    if (product.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(product);
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
            listItems.addAll((Collection<? extends Product>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void itemClicked(View v, int pos, String value);
    }

    public interface OnItemLongClickListener {
        boolean itemLongClicked(View v, int pos, String value);
    }

    public interface OnActionBarMenuClickListener {
        void onPopupMenuClick(View view, int pos, String code, String title, String nutriScore,
                              String ecoScore, String novaGroup, boolean isStarred);
    }

    public interface OnStarClickListener {
        void itemClicked(View v, int pos, boolean checked);
    }

}




