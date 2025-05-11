package com.example.festunavigator.data.repository;

import com.example.festunavigator.data.data_source.GraphDatabase;
import com.example.festunavigator.data.data_source.TreeNodeDto;
import com.example.festunavigator.data.data_source.EdgeDto;
import com.example.festunavigator.data.data_source.RecordDto;
import com.example.festunavigator.domain.repository.GraphRepository;
import java.util.List;

public class GraphRepositoryImpl implements GraphRepository {
    private final GraphDatabase database;
    private final com.example.festunavigator.data.data_source.GraphDao dao;

    public GraphRepositoryImpl(GraphDatabase database) {
        this.database = database;
        this.dao = database.getGraphDao();
    }

    @Override
    public List<TreeNodeDto> getNodes() {
        List<TreeNodeDto> nodes = dao.getNodes();
        return nodes != null ? nodes : java.util.Collections.emptyList();
    }

    @Override
    public List<EdgeDto> getEdges() {
        List<EdgeDto> edges = dao.getEdges();
        return edges != null ? edges : java.util.Collections.emptyList();
    }

    @Override
    public List<RecordDto> getRecords() {
        List<RecordDto> records = dao.getRecords();
        return records != null ? records : java.util.Collections.emptyList();
    }

    @Override
    public void addNode(TreeNodeDto node) {
        dao.insertNode(node);
    }

    @Override
    public void addEdge(EdgeDto edge) {
        dao.insertEdge(edge);
    }

    @Override
    public void addRecord(RecordDto record) {
        dao.insertRecord(record);
    }
}
