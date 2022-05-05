package com.aspark.allbooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    networkRequest networkRequest ;
//    String[] titleArray ;
//    String[] authorArray ;
//    String[] coverUrlArray;
    List<BooksData> booksDataList = new ArrayList<>();

    public SearchAdapter(Context context,List<BooksData> booksDataList) {
        this.context = context;
        networkRequest = new networkRequest();
//        this.titleArray = titleArray;
//        this.authorArray = authorArray;
//        this.coverUrlArray = coverUrlArray;
        this.booksDataList = booksDataList;
//        Log.i("searchAdapter", "title "+booksDataList.get(0).getTitle());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_view_item_layout,parent,false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.bookTitle.setText(booksDataList.get(position).getTitle());
        holder.authorName.setText(booksDataList.get(position).getAuthor());

        Glide.with(context)
                .asBitmap()
                .centerCrop()
                .load(booksDataList.get(position).getCoverUrl())
                .into(holder.bookCoverImage);

    }

    @Override
    public int getItemCount() {
        return booksDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCoverImage;
        TextView bookTitle,authorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.titleTextView);
            authorName = itemView.findViewById(R.id.authorTextView);
            bookCoverImage = itemView.findViewById(R.id.shelfCoverImageView);

        }
    }
}
