package com.aspark.allbooks;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

public class ShelfAdapter extends RecyclerView.Adapter<ShelfAdapter.ViewHolder> {

    Context context;
    List<DataModel> booksDataList;


    public ShelfAdapter(Context context,List<DataModel> booksDataList ) {

        this.context = context;
        this.booksDataList = booksDataList;


    }

    @NonNull
    @Override
    public ShelfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.bookshelf_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelfAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .asBitmap()
                .override(RecyclerView.LayoutParams.MATCH_PARENT)
                .centerCrop()
                .load(booksDataList.get(position).getCoverUrl())
                .into(holder.shelfImageView);



    }

    @Override
    public int getItemCount() {
        if (booksDataList != null)
        return booksDataList.size();
        else
            return 0;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView shelfImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shelfImageView = itemView.findViewById(R.id.shelfCoverImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            DataModel bookData = booksDataList.get(getAdapterPosition());
            Intent intent = new Intent(context,BookDetail.class);
            intent.putExtra("bookData",bookData);
            context.startActivity(intent);


        }
    }
}
