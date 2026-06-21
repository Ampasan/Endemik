package com.example.endemik.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.endemik.model.Endemik;
import com.example.endemik.model.Favorit;

@Database(entities = {Endemik.class, Favorit.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract EndemikDao endemikDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "endemik_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
