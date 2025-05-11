package com.example.festunavigator.data.data_source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface GraphDao {
    @Query("SELECT * FROM TreeNodeDto")
    List<TreeNodeDto> getNodes();

    @Query("SELECT * FROM EdgeDto")
    List<EdgeDto> getEdges();

    @Query("SELECT * FROM RecordDto ORDER BY id DESC")
    List<RecordDto> getRecords();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNode(TreeNodeDto node);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEdge(EdgeDto edge);

    @Insert
    void insertRecord(RecordDto record);
}
