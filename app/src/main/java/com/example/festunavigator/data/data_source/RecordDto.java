package com.example.festunavigator.data.data_source;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecordDto {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int startId;
    public int endId;
}
