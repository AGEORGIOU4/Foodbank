package com.example.foodbank.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodbank.classes.Settings;

import java.util.concurrent.Executors;

/**
 * The database used by Room persistence library to manage the notes database.
 */
@Database(entities = {Settings.class}, version = 1)
public abstract class SettingsRoomDatabase extends RoomDatabase {

    private static final Settings[] INITIAL_SETTINGS = new Settings[]{
            new Settings(false),
    };
    private static volatile SettingsRoomDatabase INSTANCE;

    public static SettingsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SettingsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SettingsRoomDatabase.class, "settings_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> getDatabase(context).settingsDao().insert(INITIAL_SETTINGS));
                                }
                            })
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract SettingsDao settingsDao();
}