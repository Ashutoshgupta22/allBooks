package com.aspark.allbooks.Adapter;



import android.content.Context;
import android.content.Intent;
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
import com.facebook.shimmer.ShimmerDrawable;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

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

        View view;
     if (booksDataList ==null)
     view = LayoutInflater.from(context).inflate(R.layout.data_placeholder_layout,parent,false);
     else
       view  = LayoutInflater.from(context).inflate(R.layout.bookshelf_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelfAdapter.ViewHolder holder, int position) {

        if (booksDataList != null) {
            Glide.with(context)
                    .asBitmap()
                    .override(RecyclerView.LayoutParams.MATCH_PARENT)
                    .centerCrop()
                    .load(booksDataList.get(position).getCoverUrl())
                    .into(holder.shelfImageView);

//            ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
//            shimmerDrawable.setShimmer(holder.shimmerFrameLayout);
//           holder.shimmerFrameLayout.stopShimmer();
//           holder.shimmerFrameLayout.setVisibility(View.GONE);

        }

//            holder.shimmerFrameLayout.setVisibility(View.VISIBLE);
//            holder.shimmerFrameLayout.startShimmer();


    }

    @Override
    public int getItemCount() {
        if (booksDataList != null)
        return booksDataList.size();
        else
            return 10;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView shelfImageView;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shelfImageView = itemView.findViewById(R.id.shelfCoverImageView);
//            shimmerFrameLayout = itemView.findViewById(R.id.shimmerLayout);

            if (booksDataList !=null)
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            DataModel bookData = booksDataList.get(getAdapterPosition());
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("bookData",bookData);
            context.startActivity(intent);


        }
    }


}
