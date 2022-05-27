package com.example.phonecallbroadcast.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MessageContact {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "telephone")
    public String telephone;

    @ColumnInfo(name = "message")
    public String message;
}
