package com.example.festunavigator.presentation.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.activityViewModels;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.festunavigator.R;
import com.example.festunavigator.data.data_source.RecordDto;
import com.example.festunavigator.data.data_source.TreeNodeDto;
import com.example.festunavigator.databinding.FragmentSearchBinding;
import com.example.festunavigator.presentation.search.adapters.EntriesAdapter;
import com.example.festunavigator.presentation.search.adapters.EntryItem;
import com.example.festunavigator.presentation.preview.MainShareViewModel;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements EntriesAdapter.OnItemClickListener {

    private FragmentSearchBinding binding;
    private final MainShareViewModel mainModel = activityViewModels().getValue();
    private EntriesAdapter entriesAdapter;
    private boolean selectingStart = true;
    private List<TreeNodeDto> allNodes = new ArrayList<>();
    private List<RecordDto> recordList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        entriesAdapter = new EntriesAdapter(this);
        binding.entryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.entryRecyclerView.setAdapter(entriesAdapter);

        // 获取数据
        allNodes = mainModel.getAllNodes();
        recordList = mainModel.getRecords();

        // 如果已有扫描的起点
        if (mainModel.getStartId() != null) {
            selectingStart = false;
            binding.inputLayout.setHint(getString(R.string.hint_to));
        } else {
            binding.inputLayout.setHint(getString(R.string.hint_from));
        }
        updateEntries("");

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateEntries(s != null ? s.toString() : "");
            }
        });
    }

    private void updateEntries(String query) {
        if (query.isEmpty()) {
            if (selectingStart && !recordList.isEmpty()) {
                List<EntryItem> items = new ArrayList<>();
                for (RecordDto rec : recordList) {
                    items.add(new EntryItem.RecordItem(rec));
                }
                entriesAdapter.submitList(items);
                binding.textHint.setVisibility(View.GONE);
            } else {
                entriesAdapter.submitList(new ArrayList<>());
                binding.textHint.setVisibility(View.VISIBLE);
                binding.textHint.setText(R.string.start_typing_hint);
            }
        } else {
            binding.textHint.setVisibility(View.GONE);
            List<EntryItem> filtered = new ArrayList<>();
            for (TreeNodeDto node : allNodes) {
                String idStr = String.valueOf(node.id);
                if (idStr.contains(query)) {
                    filtered.add(new EntryItem.NodeItem(node));
                }
            }
            entriesAdapter.submitList(filtered);
        }
    }

    @Override
    public void onItemClick(EntryItem item) {
        if (item instanceof EntryItem.NodeItem) {
            TreeNodeDto node = ((EntryItem.NodeItem) item).node;
            if (selectingStart) {
                mainModel.setStartId(node.id);
                selectingStart = false;
                binding.inputLayout.setHint(getString(R.string.hint_to));
                binding.searchEditText.setText("");
                updateEntries("");
            } else {
                mainModel.setEndId(node.id);
                mainModel.buildRoute();
                mainModel.addCurrentRouteToHistory();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new PreviewFragment())
                        .commit();
            }
        } else if (item instanceof EntryItem.RecordItem) {
            RecordDto record = ((EntryItem.RecordItem) item).record;
            mainModel.setStartId(record.startId);
            mainModel.setEndId(record.endId);
            mainModel.buildRoute();
            mainModel.addCurrentRouteToHistory();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new PreviewFragment())
                    .commit();
        }
    }
}
