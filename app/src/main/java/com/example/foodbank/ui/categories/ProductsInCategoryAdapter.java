package com.example.foodbank.ui.categories;

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

public class ProductsInCategoryAdapter extends RecyclerView.Adapter<ProductsInCategoryAdapter.ViewHolder> implements Filterable {

    private Vector<ProductInCategory> listItems;
    private Vector<ProductInCategory> listItemsAll;

    private Context context;
    private OnItemClickListener onItemClickListener;

    // Constructor
    public ProductsInCategoryAdapter(final Context context, Vector<ProductInCategory> listItems,
                                     OnItemClickListener onItemClickListener) {
        this.context = context.getApplicationContext();
        this.listItems = listItems;
        this.listItemsAll = new Vector<>(listItems);
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Each products item has an image, a title, nutri-score, eco-score, nova-group, pop up menu and star
        public ImageView imageView_productImage;
        public TextView textView_title;
        public ImageView imageView_nutriScore;
        public ImageView imageView_ecoScore;
        public ImageView imageView_novaGroup;
        public CheckBox checkBox_star;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView_productImage = itemView.findViewById(R.id.imageView_productImage);
            this.textView_title = itemView.findViewById(R.id.textView_title);
            this.imageView_nutriScore = itemView.findViewById(R.id.imageView_nutriScore);
            this.imageView_ecoScore = itemView.findViewById(R.id.imageView_ecoScore);
            this.imageView_novaGroup = itemView.findViewById(R.id.imageView_novaGroup);

            this.checkBox_star = itemView.findViewById(R.id.checkBox_viewStarred);
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ProductsInCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c4_card_product_category, parent, false);
        return new ViewHolder(v);
    }

    // Set values on view elements
    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        ProductInCategory listItem = listItems.get(position);

        String code = listItem.getCode();
        String title;
        String nutriScore;
        String novaGroup;
        String ecoScore;
        String imageUrl;

        // Set values for each element
        if (listItem.getProduct_name() != null && !listItem.getProduct_name().equals("")) {
            title = listItem.getProduct_name();
        } else {
            title = "Unknown";
        }

        if (listItem.getNutriscore_grade() != null && !listItem.getNutriscore_grade().equals("")) {
            nutriScore = listItem.getNutriscore_grade();
        } else {
            nutriScore = "Unknown";
        }

        if (listItem.getNova_group() != null && !listItem.getNova_group().equals("")) {
            novaGroup = listItem.getNova_group();
        } else {
            novaGroup = "Unknown";
        }

        if (listItem.getEcoscore_grade() != null && !listItem.getEcoscore_grade().equals("")) {
            ecoScore = listItem.getEcoscore_grade();
        } else {
            ecoScore = "Unknown";
        }

        if (listItem.getImage_small_url() != null && !listItem.getImage_small_url().equals("")) {
            imageUrl = listItem.getImage_small_url();
        } else {
            imageUrl = "Unknown";
        }

        // Get element from your data set at this position
        // Replace the contents of the view with that element

        try {
            if (title == null) {
                holder.textView_title.setText("Unknown");
            } else {
                holder.textView_title.setText(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (nutriScore == null) {
                holder.imageView_nutriScore.setImageResource(R.drawable.d_img_nutriscore_unknown);
            } else {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (ecoScore == null) {
                holder.imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
            } else {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (novaGroup == null) {
                holder.imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
            } else {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (imageUrl == null) {
                // Set default photo in case no photo is found (cross check with API call)
                Picasso.get().load("https://static.wixstatic.com/media/cd859f_11e62a8757e0440188f90ddc11af8230~mv2.png")
                        .resize(66, 75).centerCrop().into(holder.imageView_productImage);
            } else {
                Picasso.get().load(imageUrl).resize(66, 75).centerCrop().into(holder.imageView_productImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Oops, this should happened.", Toast.LENGTH_SHORT).show();
        }


        // Set listener on Click, Action Bar Menu
        holder.itemView.setOnClickListener(v -> onItemClickListener.itemClicked(v, position, code));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // Filter list on search action
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        // run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Vector<ProductInCategory> filteredList = new Vector<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(listItemsAll);
            } else {
                for (ProductInCategory productInCategory : listItemsAll) {
                    if (productInCategory.getProduct_name() != null && !productInCategory.getProduct_name().equals(""))
                        if (productInCategory.getProduct_name().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(productInCategory);
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

            try {
                listItems.clear();
                listItems.addAll((Collection<? extends ProductInCategory>) filterResults.values);
            } catch (Exception e) {
                e.printStackTrace();
            }
            notifyDataSetChanged();
        }
    };

    // Interfaces
    public interface OnItemClickListener {
        void itemClicked(View v, int pos, String code);
    }


}




