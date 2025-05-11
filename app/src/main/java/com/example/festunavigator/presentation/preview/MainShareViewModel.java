package com.example.festunavigator.presentation.preview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.festunavigator.data.App;
import com.example.festunavigator.data.data_source.RecordDto;
import com.example.festunavigator.data.data_source.TreeNodeDto;
import com.example.festunavigator.data.ml.classification.TextAnalyzer;
import com.example.festunavigator.data.pathfinding.GraphPathfinder;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import java.util.List;

public class MainShareViewModel extends ViewModel {

    private final MutableLiveData<Frame> frameLiveData = new MutableLiveData<>();
    private final TextAnalyzer textAnalyzer = new TextAnalyzer();
    private Integer startId;
    private Integer endId;
    private Anchor originAnchor;
    private List<Integer> pathNodeIds = java.util.Collections.emptyList();

    public LiveData<Frame> getFrameLiveData() {
        return frameLiveData;
    }

    public Frame getLatestFrame() {
        return frameLiveData.getValue();
    }

    public void setFrame(Frame frame) {
        frameLiveData.postValue(frame);
    }

    public TextAnalyzer getTextAnalyzer() {
        return textAnalyzer;
    }

    public Integer getStartId() {
        return startId;
    }

    public void setStartId(Integer id) {
        startId = id;
    }

    public void setEndId(Integer id) {
        endId = id;
    }

    public boolean setCurrentPosition(int nodeId) {
        // 检查节点是否存在
        TreeNodeDto node = null;
        for (TreeNodeDto n : App.getGraphRepository().getNodes()) {
            if (n.id == nodeId) {
                node = n;
                break;
            }
        }
        if (node != null) {
            startId = nodeId;
            // 创建锚点（此处简化处理）
            originAnchor = null;
            return true;
        }
        return false;
    }

    public void buildRoute() {
        if (startId != null && endId != null) {
            pathNodeIds = GraphPathfinder.findPath(startId, endId);
        } else {
            pathNodeIds = java.util.Collections.emptyList();
        }
    }

    public List<Integer> getPathNodeIds() {
        return pathNodeIds;
    }

    public void addCurrentRouteToHistory() {
        if (startId != null && endId != null) {
            RecordDto record = new RecordDto();
            record.startId = startId;
            record.endId = endId;
            App.getGraphRepository().addRecord(record);
        }
    }

    public List<TreeNodeDto> getAllNodes() {
        return App.getGraphRepository().getNodes();
    }

    public List<RecordDto> getRecords() {
        return App.getGraphRepository().getRecords();
    }
}
