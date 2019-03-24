package com.example.bucketlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private List<Item> bucketList;
    private ItemClickListner itemClickListner;

    public interface ItemClickListner {
        void onCheckboxClick(Item item);
    }

    public BucketListAdapter(List<Item> bucketList, ItemClickListner itemClickListner) {
        this.bucketList = bucketList;
        this.itemClickListner = itemClickListner;
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

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListner.onCheckboxClick(bucketList.get(getAdapterPosition()));
                }
            });
        }
    }
}
