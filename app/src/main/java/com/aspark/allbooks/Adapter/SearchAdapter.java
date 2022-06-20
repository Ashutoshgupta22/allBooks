package com.aspark.allbooks.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.Activity.BookDetailActivity;
import com.aspark.allbooks.DataModel;
import com.aspark.allbooks.Network.NetworkRequest;
import com.aspark.allbooks.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    NetworkRequest networkRequest ;
   public List<DataModel> booksDataList ;
   View view;
   int pos;

   public SearchAdapter(Context context,List<DataModel> booksDataList) {
        this.context = context;
        networkRequest = new NetworkRequest();
        this.booksDataList = booksDataList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        pos = holder.getAdapterPosition();

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

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCoverImage;
        TextView bookTitle,authorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.titleTextView);
            authorName = itemView.findViewById(R.id.authorTextView);
            bookCoverImage = itemView.findViewById(R.id.shelfCoverImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                  //  SearchAdapter searchAdapter = new SearchAdapter();

                  DataModel  bookData = booksDataList.get(getAdapterPosition());

                    Intent intent = new Intent(view.getContext(), BookDetailActivity.class);

                    // to pass object of a class type , that class  should implement serializable.
                    intent.putExtra("bookData",bookData);
                    view.getContext().startActivity(intent);


                }
            });

        }


    }


    // this method may be used when making viewHolder static

//    public DataModel getBookData(int pos){
//
//
//
//       return booksDataList.get(pos);
//
//    }

}
