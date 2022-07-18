package com.aspark.allbooks.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.R;

import java.util.List;

public class bookshelfNameAdapter extends RecyclerView.Adapter<bookshelfNameAdapter.viewHolder> {

   final String TAG ="bookshelfNameAdapter";
    List<String> bookshelfNameList ;
    int prevClickedPos=0;
    Boolean firstTime;
    RecyclerView bookshelf_RV;



    public bookshelfNameAdapter(List<String> bookshelfNameList, RecyclerView bookshelf_RV) {

        firstTime = true;
        this.bookshelfNameList = bookshelfNameList;
        this.bookshelf_RV = bookshelf_RV;

    }

    @NonNull
    @Override
    public bookshelfNameAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookshelfname_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookshelfNameAdapter.viewHolder holder, int position) {

        holder.bookshelfName_TextV.setText(bookshelfNameList.get(position));


        if (position==0 && firstTime) {
            firstTime =false;
            Log.d(TAG, "onBindViewHolder: its favorite");
            holder.bookshelfName_TextV.setBackground(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.chosen_bookshelfname_bg));
        }
        else
            holder.bookshelfName_TextV.setBackground(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.bookshelfname_item_bg));


    }

    @Override
    public int getItemCount() {
        return bookshelfNameList.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView bookshelfName_TextV;
        FireStore fireStore;
        String bookshelf;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            bookshelfName_TextV = itemView.findViewById(R.id.bookshelfName_TextV);

            bookshelf = bookshelfName_TextV.getText().toString();
            bookshelfName_TextV.setOnClickListener(this);
            bookshelfName_TextV.setOnLongClickListener(this);
            fireStore = new FireStore(itemView.getContext());
        }

        @Override
        public void onClick(View view) {

            Log.d(TAG, "onClick: prev Pos "+prevClickedPos);

            if(prevClickedPos != getAdapterPosition()) {

            fireStore.getBookshelf(bookshelfNameList.get(getAdapterPosition()),bookshelf_RV);

            notifyItemChanged(prevClickedPos);
            prevClickedPos = getAdapterPosition();
            bookshelfName_TextV.setBackground(AppCompatResources.getDrawable(view.getContext(), R.drawable.chosen_bookshelfname_bg));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
