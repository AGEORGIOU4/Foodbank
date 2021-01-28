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
            new Product("0737628064502", "Thai peanut noodle kit includes stir-fry rice noodles & thai peanut seasoning", "C", "4", "A", "Noodle: rice, water. seasoning packet: " +
                    "peanut, sugar, hydrolyzed soy protein, green onion, corn maltodextrin, spice (including paprika), citric acid, sea salt, extractives of paprika (color), silicon " +
                    "dioxide (added to make free flowing), yeast extract.", "\ncalcium 100g:0.038\n" +
                    "vitamin c:0\n" + "fat unit:g\n" + "vitamin c 100g:0\n" + "cholesterol 100g:0\n" + "carbohydrates value:71.15\n" +
                    "sodium 100g:0.288\n" +
                    "nova group 100g:4\n" +
                    "calcium serving:0.0198\n" +
                    "sugars:13.46\n" +
                    "energy kcal:385\n" +
                    "energy:1611\n" +
                    "carbohydrates 100g:71.15\n" +
                    "cholesterol serving:0\n" +
                    "vitamin c unit:mg\n" +
                    "trans fat 100g:0\n" +
                    "salt value:720\n" +
                    "energy unit:kcal\n" +
                    "saturated fat:1.92\n" +
                    "proteins:9.62\n" +
                    "calcium value:38\n" +
                    "proteins unit:g\n" +
                    "fat 100g:7.69\n" +
                    "vitamin a:0.0001155\n" +
                    "saturated fat value:1.92\n" +
                    "energy value:385\n" +
                    "sugars unit:g\n" +
                    "trans fat serving:0\n" +
                    "energy serving:838\n" +
                    "salt unit:mg\n" +
                    "calcium:0.038\n" +
                    "iron 100g:0.00069\n" +
                    "fat:7.69\n" +
                    "salt 100g:0.72\n" +
                    "vitamin a value:385\n" +
                    "fat serving:4\n" +
                    "saturated fat serving:0.998\n" +
                    "vitamin c serving:0\n" +
                    "vitamin a 100g:0.0001155\n" +
                    "carbohydrates:71.15\n" +
                    "sugars value:13.46\n" +
                    "vitamin a serving:0.0000601\n" +
                    "salt serving:0.374\n" +
                    "carbohydrates unit:g\n" +
                    "energy kcal value:385\n" +
                    "fiber unit:g\n" +
                    "proteins 100g:9.62\n" +
                    "sodium unit:mg\n" +
                    "fruits vegetables nuts estimate from ingredients 100g:0\n" +
                    "sodium value:288\n" +
                    "trans fat value:0\n" +
                    "nutrition score fr 100g:4\n" +
                    "vitamin c value:0\n" +
                    "nutrition score fr:4\n" +
                    "proteins serving:5\n" +
                    "proteins value:9.62\n" +
                    "salt:0.72\n" +
                    "saturated fat 100g:1.92\n" +
                    "cholesterol unit:mg\n" +
                    "sugars 100g:13.46\n" +
                    "cholesterol:0\n" +
                    "calcium unit:mg\n" +
                    "fiber serving:0.988\n" +
                    "energy kcal unit:kcal\n" +
                    "trans fat:0\n" +
                    "iron:0.00069\n" +
                    "fat value:7.69\n" +
                    "iron value:0.69\n" +
                    "energy kcal serving:200\n" +
                    "sugars serving:7\n" +
                    "cholesterol value:0\n" +
                    "carbohydrates serving:37\n" +
                    "energy kcal 100g:385\n" +
                    "iron serving:0.000359\n" +
                    "nova group serving:4\n" +
                    "saturated fat unit:g\n" +
                    "energy 100g:1611\n" +
                    "fiber 100g:1.9\n" +
                    "sodium:0.288\n" +
                    "iron unit:mg\n" +
                    "nova group:4\n" +
                    "fiber value:1.9\n" +
                    "sodium serving:0.15\n" +
                    "fiber:1.9\n" +
                    "trans fat unit:g\n" +
                    "vitamin a unit:IU\n", "Yes", "Yes", "Plant-based foods and beverages, Plant-based foods, Cereals and potatoes, Cereals and their products, Noodles\n", false, System.currentTimeMillis(),
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