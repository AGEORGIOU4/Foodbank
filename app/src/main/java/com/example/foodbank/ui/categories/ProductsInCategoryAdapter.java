package com.example.foodbank.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;

public class ProductsInCategoryAdapter extends RecyclerView.Adapter<ProductsInCategoryAdapter.ViewHolder> {

    private final Vector<ProductInCategory> listItems;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ProductsInCategoryAdapter(final Context context, final Vector<ProductInCategory> listItems, OnItemClickListener onItemClickListener) {
        this.context = context.getApplicationContext();
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ProductsInCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c4_card_category_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ProductInCategory listItem = listItems.get(position);

        // Capitalize First letter
        String title = listItem.getProduct_name();
        String nutriScore = listItem.getNutriscore_grade();
        String novaGroup = listItem.getNova_group();
        String ecoScore = listItem.getEcoscore_grade();
        String imageUrl = listItem.getImage_small_url();

        // Set listener on Click, Action Bar Menu
        holder.itemView.setOnClickListener(v -> onItemClickListener.itemClicked(v, position, listItem.getCode()));

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
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface OnItemClickListener {
        void itemClicked(View v, int pos, String value);
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

            this.checkBox_star = itemView.findViewById(R.id.checkBox_star);
        }
    }
}




