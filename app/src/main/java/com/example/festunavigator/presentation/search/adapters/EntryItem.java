package com.example.festunavigator.presentation.search.adapters;

import com.example.festunavigator.data.data_source.RecordDto;
import com.example.festunavigator.data.data_source.TreeNodeDto;

public abstract class EntryItem {
    public static class NodeItem extends EntryItem {
        public final TreeNodeDto node;
        public NodeItem(TreeNodeDto node) {
            this.node = node;
        }
        @Override
        public String getDisplayName() {
            return String.valueOf(node.id);
        }
    }
    public static class RecordItem extends EntryItem {
        public final RecordDto record;
        public RecordItem(RecordDto record) {
            this.record = record;
        }
        @Override
        public String getDisplayName() {
            return record.startId + " → " + record.endId;
        }
    }
    public abstract String getDisplayName();
}
