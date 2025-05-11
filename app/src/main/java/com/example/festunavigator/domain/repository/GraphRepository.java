package com.example.festunavigator.domain.repository;

import com.example.festunavigator.data.data_source.TreeNodeDto;
import com.example.festunavigator.data.data_source.EdgeDto;
import com.example.festunavigator.data.data_source.RecordDto;
import java.util.List;

public interface GraphRepository {
    List<TreeNodeDto> getNodes();
    List<EdgeDto> getEdges();
    List<RecordDto> getRecords();
    void addNode(TreeNodeDto node);
    void addEdge(EdgeDto edge);
    void addRecord(RecordDto record);
}
