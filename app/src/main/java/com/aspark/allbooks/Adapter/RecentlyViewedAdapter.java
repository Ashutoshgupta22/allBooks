package com.aspark.allbooks.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Activity.BookDetailActivity;
import com.aspark.allbooks.DataModel;
import com.aspark.allbooks.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.viewHolder> {

    Context context;
    ArrayList<DataModel> arrayList =new ArrayList<>();
    final String TAG = "RecentlyViewedAdapter";

    public RecentlyViewedAdapter(ArrayList<DataModel> arrayList) {
       this.arrayList = arrayList;
    }

    public RecentlyViewedAdapter() {

    }

    @NonNull
    @Override
    public RecentlyViewedAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        context = parent.getContext();

        if (arrayList.size() != 0)
         view = LayoutInflater.from(context).inflate(R.layout.bookshelf_item,parent,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.data_placeholder_layout,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedAdapter.viewHolder holder, int position) {

        if (arrayList.size() != 0) {
            Glide.with(context)
                    .asBitmap()
                    .centerCrop()
                    .load(arrayList.get(position).getCoverUrl())
                    .into(holder.bookCover);
        }

    }

    @Override
    public int getItemCount() {
        if (arrayList.size() ==0)
            return 6;
        else
        return  arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView bookCover ;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            bookCover = itemView.findViewById(R.id.shelfCoverImageView);
            if (arrayList.size() !=0)
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            DataModel bookData = arrayList.get(getAdapterPosition());
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("bookData",bookData);
            context.startActivity(intent);
        }
    }
}
