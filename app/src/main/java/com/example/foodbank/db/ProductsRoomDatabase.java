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

            new Product("4311501669662", "Bio schweizer zartbitter schokolade 70%", "D", "3", "C", "Kakaomasse, Rohrohrzucker, Kakaobutter, Vanilleschoten", "energy-kcal_value:601\n" +
                    "cocoa_value:70\n" +
                    "proteins_unit:g\n" +
                    "nova-group:3\n" +
                    "fiber_unit:g\n" +
                    "sodium_unit:g\n" +
                    "cocoa:70\n" +
                    "sodium_100g:0\n" +
                    "energy_100g:2493\n" +
                    "fiber_100g:9.3\n" +
                    "cocoa_label:Kakao (Minimum)\n" +
                    "sugars_unit:g\n" +
                    "carbohydrates_unit:g\n" +
                    "proteins_value:8.9\n" +
                    "carbohydrates_value:33.9\n" +
                    "proteins:8.9\n" +
                    "fiber:9.3\n" +
                    "alcohol_serving:0\n" +
                    "cocoa_100g:70\n" +
                    "proteins_100g:8.9\n" +
                    "sugars_100g:28.7\n" +
                    "energy:2493\n" +
                    "energy-kcal_100g:601\n" +
                    "salt_100g:0\n" +
                    "energy_unit:kJ\n" +
                    "saturated-fat_100g:27.2\n" +
                    "alcohol_unit:% vol\n" +
                    "nova-group_serving:3\n" +
                    "saturated-fat_unit:g\n" +
                    "sodium_value:0\n" +
                    "energy-kj_unit:kJ\n" +
                    "alcohol:0\n" +
                    "sugars:28.7\n" +
                    "fiber_value:9.3\n" +
                    "carbohydrates_100g:33.9\n" +
                    "energy_value:2493\n" +
                    "energy-kj:2493\n" +
                    "alcohol_value:0\n" +
                    "cocoa_unit:g\n" +
                    "saturated-fat:27.2\n" +
                    "alcohol_100g:0\n" +
                    "sodium:0\n" +
                    "nutrition-score-fr:18\n" +
                    "sugars_value:28.7\n" +
                    "salt:0\n" +
                    "salt_unit:g\n" +
                    "fat_100g:45.7\n" +
                    "salt_value:0\n" +
                    "fat:45.7\n" +
                    "nova-group_100g:3\n" +
                    "energy-kj_value:2493\n" +
                    "energy-kcal:601\n" +
                    "carbohydrates:33.9\n" +
                    "saturated-fat_value:27.2\n" +
                    "cocoa_serving:70\n" +
                    "energy-kcal_unit:kcal\n" +
                    "fruits-vegetables-nuts-estimate-from-ingredients_100g:0\n" +
                    "energy-kj_100g:2493\n" +
                    "fat_value:45.7\n" +
                    "fat_unit:g\n" +
                    "nutrition-score-fr_100g:18\n", "Yes", "Yes", "Imbiss, Süßwaren, Schokoladen, Bitterschokoladen\n", false, System.currentTimeMillis(),
                    "https://static.openfoodfacts.org/images/products/431/150/166/9662/front_de.32.200.jpg"),
            new Product("3017239004751", "Olives noires à la grecque bio - 301723900475", "D", "3", "C", "Olives noires*, sel, huile d'olive vierge*(1%)\\r\\n*Produits issus de l'agriculture biologique\n",
                    "saturated-fat_value:4.9000000953674\n" +
                            "energy-kcal_unit:kcal\n" +
                            "energy-kcal:409\n" +
                            "carbohydrates:9\n" +
                            "nutrition-score-fr_100g:13\n" +
                            "fruits-vegetables-nuts-estimate_value:100\n" +
                            "fat_value:39\n" +
                            "fruits-vegetables-nuts-estimate-from-ingredients_100g:1\n" +
                            "fat_unit:g\n" +
                            "carbon-footprint-from-known-ingredients_product:3.9\n" +
                            "nutrition-score-fr:13\n" +
                            "sugars_value:4\n" +
                            "saturated-fat:4.9000000953674\n" +
                            "sodium:2.76000003814696\n" +
                            "fruits-vegetables-nuts-estimate_label:Fruits, légumes, noix et huiles de colza, noix et olive (estimation manuelle avec la liste des ingrédients)\n" +
                            "salt_value:6.9000000953674\n" +
                            "fat:39\n" +
                            "nova-group_100g:3\n" +
                            "salt:6.9000000953674\n" +
                            "fat_100g:39\n" +
                            "salt_unit:g\n" +
                            "saturated-fat_unit:g\n" +
                            "nova-group_serving:3\n" +
                            "sodium_value:2.76000003814696\n" +
                            "sugars_100g:4\n" +
                            "proteins_100g:2.2999999523163\n" +
                            "energy_unit:kcal\n" +
                            "salt_100g:6.9000000953674\n" +
                            "energy-kcal_100g:409\n" +
                            "saturated-fat_100g:4.9000000953674\n" +
                            "energy:1711\n" +
                            "energy_value:409\n" +
                            "carbon-footprint-from-known-ingredients_100g:2.6\n" +
                            "sugars:4\n" +
                            "fruits-vegetables-nuts-estimate:100\n" +
                            "carbohydrates_100g:9\n" +
                            "sodium_100g:2.76000003814696\n" +
                            "sodium_unit:g\n" +
                            "nova-group:3\n" +
                            "proteins_unit:g\n" +
                            "energy-kcal_value:409\n" +
                            "fruits-vegetables-nuts-estimate_unit:g\n" +
                            "fruits-vegetables-nuts-estimate_serving:100\n" +
                            "proteins:2.2999999523163\n" +
                            "carbohydrates_value:9\n" +
                            "sugars_unit:g\n" +
                            "energy_100g:1711\n" +
                            "fruits-vegetables-nuts-estimate_100g:100\n" +
                            "proteins_value:2.2999999523163\n" +
                            "carbohydrates_unit:g\n", "Yes", "Yes", "Aliments et boissons à base de végétaux,Aliments d'origine végétale,Pickles,Produits de l'olivier,Pickles d'origine végétale,Olives,Olives noires",
                    true, System.currentTimeMillis(), "https://static.openfoodfacts.org/images/products/301/723/900/4751/nutrition_fr.23.200.jpg"),
            new Product("0012345678905", "Bouchée Apéritive", "D", "1", "D", "Courgette (40 %), _ŒUF_, Farines (avoine, pois chiche), bicarbonate, _MOUTARDE_ à L'Ancienne (dont Vinaigre d'alcool, graines de _MOUTARDE_, eau, sel, vin blanc 4,8 % (dont _SULFITES_), sucre, arôme naturel, acidifiant : acide lactique), Cœur: _MOUTARDE_ au miel (dont Sucre, eau, vinaigre d'alcool, graines de _MOUTARDE_, miel 8%, sel, épices, herbes, colorant : caramel E150c, acidifiant : acide citrique, conservateur : E224 (_SULFITES_))",
                    "nutrition-score-fr_100g:13\n" +
                            "vitamin-c_unit:mg\n" +
                            "calcium_value:0\n" +
                            "energy_serving:781\n" +
                            "saturated-fat_value:5.56\n" +
                            "energy-kcal:333.33\n" +
                            "fruits-vegetables-nuts_label:0\n" +
                            "cholesterol_value:18\n" +
                            "salt_value:1\n" +
                            "vitamin-a_unit:IU\n" +
                            "calcium:0\n" +
                            "fat_100g:5.56\n" +
                            "salt:1\n" +
                            "nutrition-score-fr:13\n" +
                            "fruits-vegetables-nuts_value:50\n" +
                            "sodium:0.4\n" +
                            "cholesterol_100g:0.018\n" +
                            "vitamin-a_serving:0\n" +
                            "sugars_serving:3.11\n" +
                            "saturated-fat:5.56\n" +
                            "trans-fat_serving:0\n" +
                            "vitamin-a:0\n" +
                            "vitamin-c_value:0\n" +
                            "energy_value:333.33\n" +
                            "fiber_serving:0\n" +
                            "fruits-vegetables-nuts-estimate:40\n" +
                            "fiber_value:0\n" +
                            "sugars:5.56\n" +
                            "vitamin-c:0\n" +
                            "calcium_serving:0\n" +
                            "sodium_value:0.4\n" +
                            "trans-fat_100g:0\n" +
                            "iron_serving:0\n" +
                            "saturated-fat_unit:g\n" +
                            "salt_100g:1\n" +
                            "energy_unit:kcal\n" +
                            "saturated-fat_100g:5.56\n" +
                            "proteins_serving:3.11\n" +
                            "sugars_100g:5.56\n" +
                            "fruits-vegetables-nuts_serving:50\n" +
                            "carbohydrates_unit:g\n" +
                            "fruits-vegetables-nuts-estimate_100g:40\n" +
                            "fiber_100g:0\n" +
                            "sugars_unit:g\n" +
                            "fat_serving:3.11\n" +
                            "sodium_unit:g\n" +
                            "sodium_100g:0.4\n" +
                            "fruits-vegetables-nuts-estimate_serving:40\n" +
                            "vitamin-c_100g:0\n" +
                            "vitamin-a_value:0\n" +
                            "proteins_unit:g\n" +
                            "fruits-vegetables-nuts-estimate_value:40\n" +
                            "fruits-vegetables-nuts_unit:g\n" +
                            "fat_unit:g\n" +
                            "fruits-vegetables-nuts-estimate-from-ingredients_100g:40\n" +
                            "fat_value:5.56\n" +
                            "energy-kcal_unit:kcal\n" +
                            "saturated-fat_serving:3.11\n" +
                            "calcium_unit:mg\n" +
                            "carbohydrates:72.22\n" +
                            "cholesterol_unit:mg\n" +
                            "trans-fat_unit:g\n" +
                            "fat:5.56\n" +
                            "nova-group_100g:4\n" +
                            "iron_unit:mg\n" +
                            "trans-fat_value:0\n" +
                            "fruits-vegetables-nuts-estimate_label:0\n" +
                            "cholesterol_serving:0.0101\n" +
                            "salt_unit:g\n" +
                            "fruits-vegetables-nuts:50\n" +
                            "calcium_100g:0\n" +
                            "sugars_value:5.56\n" +
                            "energy-kcal_serving:187\n" +
                            "vitamin-a_100g:0\n" +
                            "carbohydrates_serving:40.4\n" +
                            "sodium_serving:0.224\n" +
                            "carbon-footprint-from-known-ingredients_100g:34.72\n" +
                            "carbohydrates_100g:72.22\n" +
                            "iron:0\n" +
                            "salt_serving:0.56\n" +
                            "nova-group_serving:4\n" +
                            "energy:1395\n" +
                            "energy-kcal_100g:333.33\n" +
                            "proteins_100g:5.56\n" +
                            "iron_value:0\n" +
                            "fiber:0\n" +
                            "iron_100g:0\n" +
                            "carbon-footprint-from-known-ingredients_serving:19.4\n" +
                            "carbohydrates_value:72.22\n" +
                            "proteins:5.56\n" +
                            "proteins_value:5.56\n" +
                            "cholesterol:0.018\n" +
                            "energy_100g:1395\n" +
                            "fruits-vegetables-nuts_100g:50\n" +
                            "vitamin-c_serving:0\n" +
                            "trans-fat:0\n" +
                            "fruits-vegetables-nuts-estimate_unit:g\n" +
                            "fiber_unit:g\n" +
                            "energy-kcal_value:333.33\n" +
                            "nova-group:4\n" +
                            "additives_n:4\n", "Yes", "Yes", "Snacks, Viandes, Snacks salés, Apéritif, Charcuteries", false, System.currentTimeMillis(),
                    "https://static.openfoodfacts.org/images/products/001/234/567/8905/ingredients_fr.28.200.jpg")
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