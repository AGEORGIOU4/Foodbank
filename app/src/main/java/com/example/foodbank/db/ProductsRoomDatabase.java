package com.example.foodbank.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodbank.ui.products.Product;

import java.util.concurrent.Executors;

/**
 * The database used by Room persistence library to manage the notes database.
 */
@Database(entities = {Product.class}, version = 1)
public abstract class ProductsRoomDatabase extends RoomDatabase {

    private static final Product[] INITIAL_PRODUCTS = new Product[]{
            new Product("0737628064502", "Thai peanut noodle kit includes stir-fry rice noodles & thai peanut seasoning", "C", "4", "Unknown", "Noodle: rice, water. seasoning packet: " +
                    "peanut, sugar, hydrolyzed soy protein, green onion, corn maltodextrin, spice (including paprika), citric acid, sea salt, extractives of paprika (color), silicon " +
                    "dioxide (added to make free flowing), yeast extract.", "calcium_100g\t:\t0.038\n" +
                    "vitamin-c\t:\t0\n" +
                    "fat_unit\t:\tg\n" +
                    "vitamin-c_100g\t:\t0\n" +
                    "cholesterol_100g\t:\t0\n" +
                    "carbohydrates_value\t:\t71.15\n" +
                    "sodium_100g\t:\t0.288\n" +
                    "nova-group_100g\t:\t4\n" +
                    "calcium_serving\t:\t0.0198\n" +
                    "sugars\t:\t13.46\n" +
                    "energy-kcal\t:\t385\n" +
                    "energy\t:\t1611\n" +
                    "carbohydrates_100g\t:\t71.15\n" +
                    "cholesterol_serving\t:\t0\n" +
                    "vitamin-c_unit\t:\tmg\n" +
                    "trans-fat_100g\t:\t0\n" +
                    "salt_value\t:\t720\n" +
                    "energy_unit\t:\tkcal\n" +
                    "saturated-fat\t:\t1.92\n" +
                    "proteins\t:\t9.62\n" +
                    "calcium_value\t:\t38\n" +
                    "proteins_unit\t:\tg\n" +
                    "fat_100g\t:\t7.69\n" +
                    "vitamin-a\t:\t0.0001155\n" +
                    "saturated-fat_value\t:\t1.92\n" +
                    "energy_value\t:\t385\n" +
                    "sugars_unit\t:\tg\n" +
                    "trans-fat_serving\t:\t0\n" +
                    "energy_serving\t:\t838\n" +
                    "salt_unit\t:\tmg\n" +
                    "calcium\t:\t0.038\n" +
                    "iron_100g\t:\t0.00069\n" +
                    "fat\t:\t7.69\n" +
                    "salt_100g\t:\t0.72\n" +
                    "vitamin-a_value\t:\t385\n" +
                    "fat_serving\t:\t4\n" +
                    "saturated-fat_serving\t:\t0.998\n" +
                    "vitamin-c_serving\t:\t0\n" +
                    "vitamin-a_100g\t:\t0.0001155\n" +
                    "carbohydrates\t:\t71.15\n" +
                    "sugars_value\t:\t13.46\n" +
                    "vitamin-a_serving\t:\t0.0000601\n" +
                    "salt_serving\t:\t0.374\n" +
                    "carbohydrates_unit\t:\tg\n" +
                    "energy-kcal_value\t:\t385\n" +
                    "fiber_unit\t:\tg\n" +
                    "proteins_100g\t:\t9.62\n" +
                    "sodium_unit\t:\tmg\n" +
                    "fruits-vegetables-nuts-estimate-from-ingredients_100g\t:\t0\n" +
                    "sodium_value\t:\t288\n" +
                    "trans-fat_value\t:\t0\n" +
                    "nutrition-score-fr_100g\t:\t4\n" +
                    "vitamin-c_value\t:\t0\n" +
                    "nutrition-score-fr\t:\t4\n" +
                    "proteins_serving\t:\t5\n" +
                    "proteins_value\t:\t9.62\n" +
                    "salt\t:\t0.72\n" +
                    "saturated-fat_100g\t:\t1.92\n" +
                    "cholesterol_unit\t:\tmg\n" +
                    "sugars_100g\t:\t13.46\n" +
                    "cholesterol\t:\t0\n" +
                    "calcium_unit\t:\tmg\n" +
                    "fiber_serving\t:\t0.988\n" +
                    "energy-kcal_unit\t:\tkcal\n" +
                    "trans-fat\t:\t0\n" +
                    "iron\t:\t0.00069\n" +
                    "fat_value\t:\t7.69\n" +
                    "iron_value\t:\t0.69\n" +
                    "energy-kcal_serving\t:\t200\n" +
                    "sugars_serving\t:\t7\n" +
                    "cholesterol_value\t:\t0\n" +
                    "carbohydrates_serving\t:\t37\n" +
                    "energy-kcal_100g\t:\t385\n" +
                    "iron_serving\t:\t0.000359\n" +
                    "nova-group_serving\t:\t4\n" +
                    "saturated-fat_unit\t:\tg\n" +
                    "energy_100g\t:\t1611\n" +
                    "fiber_100g\t:\t1.9\n" +
                    "sodium\t:\t0.288\n" +
                    "iron_unit\t:\tmg\n" +
                    "nova-group\t:\t4\n" +
                    "fiber_value\t:\t1.9\n" +
                    "sodium_serving\t:\t0.15\n" +
                    "fiber\t:\t1.9\n" +
                    "trans-fat_unit\t:\tg\n" +
                    "vitamin-a_unit\t:\tIU\n", "Yes", "Yes", "Plant-based foods and beverages, Plant-based foods, Cereals and potatoes, Cereals and their products, Noodles\n", false, System.currentTimeMillis(),
                    "https://static.openfoodfacts.org/images/products/073/762/806/4502/front_en.6.200.jpg"),
    };
    private static volatile ProductsRoomDatabase INSTANCE;

    public static ProductsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductsRoomDatabase.class, "products_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> getDatabase(context).productsDao().insert(INITIAL_PRODUCTS));
                                }
                            })
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ProductsDao productsDao();
}