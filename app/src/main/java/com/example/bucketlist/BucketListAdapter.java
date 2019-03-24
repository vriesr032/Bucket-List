package com.example.bucketlist;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private List<Item> bucketList;

    public BucketListAdapter(List<Item> bucketList) {
        this.bucketList = bucketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.grid_cell, viewGroup, false);
        return new BucketListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(bucketList.get(viewHolder.getAdapterPosition()).getTitle());
        viewHolder.itemDescription.setText(bucketList.get(viewHolder.getAdapterPosition()).getDescription());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewHolder.itemTitle.setPaintFlags(viewHolder.itemTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.itemDescription.setPaintFlags(viewHolder.itemDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }   else {
                    viewHolder.itemTitle.setPaintFlags(viewHolder.itemTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.itemDescription.setPaintFlags(viewHolder.itemDescription.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView itemTitle;
        private TextView itemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemTitle = itemView.findViewById(R.id.text1);
            itemDescription = itemView.findViewById(R.id.text2);
        }
    }
}
