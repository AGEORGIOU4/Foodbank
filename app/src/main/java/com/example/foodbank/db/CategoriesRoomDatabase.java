package com.example.foodbank.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodbank.ui.categories.Category;

import java.util.concurrent.Executors;

/**
 * The database used by Room persistence library to manage the notes database.
 */
@Database(entities = {Category.class}, version = 1)
public abstract class CategoriesRoomDatabase extends RoomDatabase {

    private static final Category[] INITIAL_CATEGORIES = new Category[]{
            //0
            new Category("https://world.openfoodfacts.org/category/plant-based-foods-and-beverages", "en:plant-based-foods-and-beverages",
                    "Plant-based foods and beverages", 226237, 1),
            //1
            new Category("https://world.openfoodfacts.org/category/plant-based-foods", "en:plant-based-foods", "Plant-based foods",
                    195566, 1),
            //2
            new Category("https://world.openfoodfacts.org/category/snacks", "en:snacks",
                    "Snacks", 145164, 1),
            //3
            new Category("https://world.openfoodfacts.org/category/sweet-snacks", "en:sweet-snacks", "Sweet snacks",
                    95527, 1),
            //4
            new Category("https://world.openfoodfacts.org/category/beverages", "en:beverages",
                    "Beverages", 94374, 1),
            //5
            new Category("https://world.openfoodfacts.org/category/dairies", "en:dairies", "Dairies",
                    79633, 1),
            //6
            new Category("https://world.openfoodfacts.org/category/cereals-and-potatoes", "Cereals and potatoes",
                    "Cereals and potatoes", 71540, 1),
            //7
            new Category("https://world.openfoodfacts.org/category/fruits-and-vegetables-based-foods", "en:fruits-and-vegetables-based-foods", "Fruits and vegetables based foods",
                    62964, 1),
            //8
            new Category("https://world.openfoodfacts.org/category/meats", "en:meats", "Meats",
                    61200, 1),
            //9
            new Category("https://world.openfoodfacts.org/category/groceries", "en:groceries",
                    "Groceries", 61152, 1),
            //10
            new Category("https://world.openfoodfacts.org/category/fermented-foods", "en:fermented-foods", "Fermented foods",
                    60760, 1),
            //11
            new Category("https://world.openfoodfacts.org/category/fermented-milk-products", "en:fermented-milk-products",
                    "Fermented milk products", 60251, 1),
            //12
            new Category("https://world.openfoodfacts.org/category/biscuits-and-cakes", "en:biscuits-and-cakes", "Biscuits and cakes",
                    54528, 1),
            //13
            new Category("https://world.openfoodfacts.org/category/meals", "en:meals", "Meals", 53341, 1),
    };
    private static volatile CategoriesRoomDatabase INSTANCE;

    public static CategoriesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CategoriesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CategoriesRoomDatabase.class, "categories_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> getDatabase(context).categoriesDao().insert(INITIAL_CATEGORIES));
                                }
                            })
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract CategoriesDao categoriesDao();
}