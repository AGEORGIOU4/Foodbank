package com.example.foodbank.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodbank.classes.CustomList;
import com.example.foodbank.classes.Product;
import com.example.foodbank.classes.ProductToList;

import java.util.concurrent.Executors;

/**
 * The database used by Room persistence library to manage the notes database.
 */
@Database(entities = {Product.class, CustomList.class, ProductToList.class}, version = 1)
public abstract class ProductsRoomDatabase extends RoomDatabase {

    private static final Product[] INITIAL_PRODUCTS = new Product[]{
            new Product("0737628064502", "Thai peanut noodle kit includes stir-fry rice noodles & thai peanut seasoning", "A", "1", "A", "Noodle: rice, water. seasoning packet: " +
                    "peanut, sugar, hydrolyzed soy protein, green onion, corn maltodextrin, spice (including paprika), citric acid, sea salt, extractives of paprika (color), silicon " +
                    "dioxide (added to make free flowing), yeast extract.", "\ncalcium 100g:0.038\n" +
                    "vitamin c:0\n" + "fat unit:g\n" + "vitamin c 100g:0\n" + "cholesterol 100g:0\n" + "carbohydrates value:71.15\n" +
                    "sodium 100g:0.288\n" + "nova group 100g:4\n" + "calcium serving:0.0198\n" + "sugars:13.46\n" + "energy kcal:385\n" +
                    "energy:1611\n" + "carbohydrates 100g:71.15\n" + "cholesterol serving:0\n" + "vitamin c unit:mg\n" + "trans fat 100g:0\n" +
                    "salt value:720\n" + "energy unit:kcal\n" + "saturated fat:1.92\n" + "proteins:9.62\n" + "calcium value:38\n" + "proteins unit:g\n" +
                    "fat 100g:7.69\n" + "vitamin a:0.0001155\n" + "saturated fat value:1.92\n" + "energy value:385\n" + "sugars unit:g\n" + "trans fat serving:0\n" + "energy serving:838\n" + "salt unit:mg\n" + "calcium:0.038\n" +
                    "iron 100g:0.00069\n" + "fat:7.69\n" + "salt 100g:0.72\n" + "vitamin a value:385\n" + "fat serving:4\n" + "saturated fat serving:0.998\n" +
                    "vitamin c serving:0\n" + "vitamin a 100g:0.0001155\n" + "carbohydrates:71.15\n" + "sugars value:13.46\n" + "vitamin a serving:0.0000601\n" +
                    "salt serving:0.374\n" + "carbohydrates unit:g\n" + "energy kcal value:385\n" + "fiber unit:g\n" + "proteins 100g:9.62\n" +
                    "sodium unit:mg\n" + "fruits vegetables nuts estimate from ingredients 100g:0\n" + "sodium value:288\n" + "trans fat value:0\n" +
                    "nutrition score fr 100g:4\n" + "vitamin c value:0\n" + "nutrition score fr:4\n" + "proteins serving:5\n" + "proteins value:9.62\n" +
                    "salt:0.72\n" + "saturated fat 100g:1.92\n" + "cholesterol unit:mg\n" + "sugars 100g:13.46\n" + "cholesterol:0\n" + "calcium unit:mg\n" +
                    "fiber serving:0.988\n" + "energy kcal unit:kcal\n" + "trans fat:0\n" + "iron:0.00069\n" + "fat value:7.69\n" + "iron value:0.69\n" +
                    "energy kcal serving:200\n" + "sugars serving:7\n" + "cholesterol value:0\n" + "carbohydrates serving:37\n" + "energy kcal 100g:385\n" +
                    "iron serving:0.000359\n" + "nova group serving:4\n" + "saturated fat unit:g\n" + "energy 100g:1611\n" + "fiber 100g:1.9\n" + "sodium:0.288\n" +
                    "iron unit:mg\n" + "nova group:4\n" + "fiber value:1.9\n" + "sodium serving:0.15\n" + "fiber:1.9\n" + "trans fat unit:g\n" + "vitamin a unit:IU\n", "Yes", "Yes", "Plant-based foods and beverages, Plant-based foods, Cereals and potatoes, Cereals and their products, Noodles\n", false, System.currentTimeMillis(),
                    "https://static.openfoodfacts.org/images/products/073/762/806/4502/front_en.6.200.jpg"),

            new Product("3168930009078", "quater oats", "a", "b", "1", "100 % flocons d'avoine.", "magnesium:0.11\n" + "proteins_serving:4.4\n" +
                    "carbohydrates_100g:60\n" + "nova-group_100g:1\n" + "energy-kcal_100g:375\n" + "phosphorus:0.38\n" + "salt:0.01\n" +
                    "magnesium_serving:0.044\n" + "fat_unit:g\n" + "energy_serving:628\n" + "beta-glucan:3.6\n" + "energy_100g:1569\n" +
                    "fiber_serving:3.6\n" + "energy_unit:kcal\n" + "sodium_serving:0.0016\n" + "vitamin-b1_serving:0.00036\n" +
                    "beta-glucan_100g:3.6\n" + "energy_value:375\n" + "nutrition-score-fr_100g:-5\n" + "beta-glucan_value:3.6\n" +
                    "salt_modifier:<\n" + "phosphorus_unit:mg\n" + "vitamin-b1:0.0009\n" + "proteins:11\n" + "sodium:0.004\n" +
                    "carbon-footprint-from-known-ingredients_product:165\n" + "vitamin-b1_unit:mg\n" + "carbon-footprint-from-known-ingredients_100g:30\n" +
                    "sodium_100g:0.004\n" + "iron:0.0038\n" + "fiber:9\n" + "saturated-fat_value:1.5\n" + "fat_100g:8\n" + "sodium_unit:g\n" +
                    "phosphorus_label:0\n" + "beta-glucan_serving:1.44\n" + "proteins_value:11\n" + "energy-kcal_unit:kcal\n" +
                    "vitamin-b1_label:0\n" + "saturated-fat_100g:1.5\n" + "fiber_value:9\n" + "fat_serving:3.2\n" + "energy-kcal_value:375\n" +
                    "magnesium_unit:mg\n" + "energy-kcal_serving:150\n" + "sodium_modifier:<\n" + "fat_value:8\n" + "salt_value:0.01\n" +
                    "fiber_100g:9\n" + "carbohydrates:60\n" + "beta-glucan_unit:g\n" + "carbon-footprint-from-known-ingredients_serving:12\n" +
                    "carbohydrates_serving:24\n" + "sugars_unit:g\n" + "vitamin-b1_value:0.9\n" + "energy:1569\n" + "sodium_value:0.004\n" +
                    "sugars_value:1.1\n" + "magnesium_label:0\n" + "energy-kcal:375\n" + "phosphorus_value:380\n" + "iron_100g:0.0038\n" +
                    "fiber_unit:g\n" + "fat:8\n" + "nova-group:1\n" + "salt_serving:0.004\n" + "iron_value:3.8\n" + "saturated-fat:1.5\n" +
                    "iron_serving:0.00152\n" + "carbohydrates_unit:g\n" + "salt_100g:0.01\n" + "phosphorus_serving:0.152\n" + "magnesium_value:110\n" +
                    "proteins_unit:g\n" + "salt_unit:g\n" + "proteins_100g:11\n" + "fruits-vegetables-nuts-estimate-from-ingredients_100g:0\n" +
                    "iron_unit:mg\n" + "carbohydrates_value:60\n" + "phosphorus_100g:0.38\n" + "sugars_100g:1.1\n" + "iron_label:0\n" +
                    "nova-group_serving:1\n" + "nutrition-score-fr:-5\n" + "beta-glucan_label:0\n" + "saturated-fat_serving:0.6\n" +
                    "sugars_serving:0.44\n" + "sugars:1.1\n" + "vitamin-b1_100g:0.0009\n" + "magnesium_100g:0.11\n" + "saturated-fat_unit:g",
                    "Yes", "Yes", "Aliments et boissons à base de végétaux, Aliments d'origine végétale, Céréales et pommes de terre, Petit-déjeuners, Céréales et dérivés, Céréales pour petit-déjeuner, Flocons, Flocons de céréales, Flocons laminés, Flocons d'avoine",
                    true, System.currentTimeMillis(), "https://static.openfoodfacts.org/images/products/316/893/000/9078/ingredients_fr.19.400.jpg")
    };

    private static final CustomList[] INITIAL_LISTS = new CustomList[]{
            new CustomList(1, "My Snacks", "Less structured meal that are not eaten during regular meal times", 0, System.currentTimeMillis()),
            new CustomList(2, "My Beverages", "Favorite beverages", 0, System.currentTimeMillis())};

    private static final ProductToList[] INITIAL_PRODUCTS_TO_LISTS = new ProductToList[]{
            new ProductToList("0737628064502", 1),
            new ProductToList("3168930009078", 1)};


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
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> getDatabase(context).productsDao().insert(INITIAL_LISTS));
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> getDatabase(context).productsDao().insert(INITIAL_PRODUCTS_TO_LISTS));
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