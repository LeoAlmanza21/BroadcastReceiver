package com.example.phonecallbroadcast.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MessageContact.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DAOMessageContact DaoMessageContact();
}
