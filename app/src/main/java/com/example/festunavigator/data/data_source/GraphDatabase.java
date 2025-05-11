package com.example.festunavigator.data.data_source;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TreeNodeDto.class, EdgeDto.class, RecordDto.class}, version = 1)
public abstract class GraphDatabase extends RoomDatabase {
    public abstract GraphDao getGraphDao();
}
