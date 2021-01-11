package com.example.foodbank.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodbank.Product;

import java.util.concurrent.Executors;

/**
 * The database used by Room persistence library to manage the notes database.
 */
@Database(entities = {Product.class}, version = 1)
public abstract class ProductsRoomDatabase extends RoomDatabase {

    public abstract ProductsDao productsDao();

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

    private static final Product[] INITIAL_PRODUCTS = new Product[]{
            new Product("A test product", "This is the grade.", 5, false),
            new Product("Another test product", "This is the grade ", 3, true),
    };
}