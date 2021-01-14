package com.example.foodbank.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodbank.Category;

import java.util.concurrent.Executors;

/**
 * The database used by Room persistence library to manage the notes database.
 */
@Database(entities = {Category.class}, version = 1)
public abstract class CategoriesRoomDatabase extends RoomDatabase {

    public abstract CategoriesDao categoriesDao();

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

    private static final Category[] INITIAL_CATEGORIES = new Category[]{
            new Category("Snacks", System.currentTimeMillis()),
    };
}