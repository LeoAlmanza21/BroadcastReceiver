package com.example.phonecallbroadcast.database;

import android.os.Message;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAOMessageContact {

    @Insert
    void insert(MessageContact messageContact);

    @Query("SELECT * FROM MessageContact")
    List<MessageContact> getAll();

    @Delete
    void delete(MessageContact messageContact);

    @Query("DELETE FROM MessageContact")
    void deleteAll();
}
