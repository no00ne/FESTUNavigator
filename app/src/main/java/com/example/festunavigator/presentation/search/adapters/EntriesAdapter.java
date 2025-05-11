package com.example.festunavigator.presentation.search.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.festunavigator.R;
import com.example.festunavigator.data.data_source.RecordDto;
import com.example.festunavigator.data.data_source.TreeNodeDto;
import java.util.ArrayList;
import java.util.List;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.EntryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(EntryItem item);
    }

    private List<EntryItem> entries = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;

    public EntriesAdapter(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void submitList(List<EntryItem> newEntries) {
        entries = newEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry, parent, false);
        return new EntryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        EntryItem item = entries.get(position);
        holder.textName.setText(item.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textName;
        EntryViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textViewName);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos = getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(entries.get(pos));
            }
        }
    }
}
