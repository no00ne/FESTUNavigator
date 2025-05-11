package com.example.festunavigator.data.data_source;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TreeNodeDto {
    @PrimaryKey
    public int id;
    public float x;
    public float y;
    public float z;
}
