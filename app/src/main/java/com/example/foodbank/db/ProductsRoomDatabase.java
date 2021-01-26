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
                    "dioxide (added to make free flowing), yeast extract.", "\ncalcium 100g\t:\t0.038\n" +
                    "vitamin c\t:\t0\n" +
                    "fat unit\t:\tg\n" +
                    "vitamin c 100g\t:\t0\n" +
                    "cholesterol 100g\t:\t0\n" +
                    "carbohydrates value\t:\t71.15\n" +
                    "sodium 100g\t:\t0.288\n" +
                    "nova group 100g\t:\t4\n" +
                    "calcium serving\t:\t0.0198\n" +
                    "sugars\t:\t13.46\n" +
                    "energy kcal\t:\t385\n" +
                    "energy\t:\t1611\n" +
                    "carbohydrates 100g\t:\t71.15\n" +
                    "cholesterol serving\t:\t0\n" +
                    "vitamin c unit\t:\tmg\n" +
                    "trans fat 100g\t:\t0\n" +
                    "salt value\t:\t720\n" +
                    "energy unit\t:\tkcal\n" +
                    "saturated fat\t:\t1.92\n" +
                    "proteins\t:\t9.62\n" +
                    "calcium value\t:\t38\n" +
                    "proteins unit\t:\tg\n" +
                    "fat 100g\t:\t7.69\n" +
                    "vitamin a\t:\t0.0001155\n" +
                    "saturated fat value\t:\t1.92\n" +
                    "energy value\t:\t385\n" +
                    "sugars unit\t:\tg\n" +
                    "trans fat serving\t:\t0\n" +
                    "energy serving\t:\t838\n" +
                    "salt unit\t:\tmg\n" +
                    "calcium\t:\t0.038\n" +
                    "iron 100g\t:\t0.00069\n" +
                    "fat\t:\t7.69\n" +
                    "salt 100g\t:\t0.72\n" +
                    "vitamin a value\t:\t385\n" +
                    "fat serving\t:\t4\n" +
                    "saturated fat serving\t:\t0.998\n" +
                    "vitamin c serving\t:\t0\n" +
                    "vitamin a 100g\t:\t0.0001155\n" +
                    "carbohydrates\t:\t71.15\n" +
                    "sugars value\t:\t13.46\n" +
                    "vitamin a serving\t:\t0.0000601\n" +
                    "salt serving\t:\t0.374\n" +
                    "carbohydrates unit\t:\tg\n" +
                    "energy kcal value\t:\t385\n" +
                    "fiber unit\t:\tg\n" +
                    "proteins 100g\t:\t9.62\n" +
                    "sodium unit\t:\tmg\n" +
                    "fruits vegetables nuts estimate from ingredients 100g\t:\t0\n" +
                    "sodium value\t:\t288\n" +
                    "trans fat value\t:\t0\n" +
                    "nutrition score fr 100g\t:\t4\n" +
                    "vitamin c value\t:\t0\n" +
                    "nutrition score fr\t:\t4\n" +
                    "proteins serving\t:\t5\n" +
                    "proteins value\t:\t9.62\n" +
                    "salt\t:\t0.72\n" +
                    "saturated fat 100g\t:\t1.92\n" +
                    "cholesterol unit\t:\tmg\n" +
                    "sugars 100g\t:\t13.46\n" +
                    "cholesterol\t:\t0\n" +
                    "calcium unit\t:\tmg\n" +
                    "fiber serving\t:\t0.988\n" +
                    "energy kcal unit\t:\tkcal\n" +
                    "trans fat\t:\t0\n" +
                    "iron\t:\t0.00069\n" +
                    "fat value\t:\t7.69\n" +
                    "iron value\t:\t0.69\n" +
                    "energy kcal serving\t:\t200\n" +
                    "sugars serving\t:\t7\n" +
                    "cholesterol value\t:\t0\n" +
                    "carbohydrates serving\t:\t37\n" +
                    "energy kcal 100g\t:\t385\n" +
                    "iron serving\t:\t0.000359\n" +
                    "nova group serving\t:\t4\n" +
                    "saturated fat unit\t:\tg\n" +
                    "energy 100g\t:\t1611\n" +
                    "fiber 100g\t:\t1.9\n" +
                    "sodium\t:\t0.288\n" +
                    "iron unit\t:\tmg\n" +
                    "nova group\t:\t4\n" +
                    "fiber value\t:\t1.9\n" +
                    "sodium serving\t:\t0.15\n" +
                    "fiber\t:\t1.9\n" +
                    "trans fat unit\t:\tg\n" +
                    "vitamin a unit\t:\tIU\n", "Yes", "Yes", "Plant-based foods and beverages, Plant-based foods, Cereals and potatoes, Cereals and their products, Noodles\n", false, System.currentTimeMillis(),
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