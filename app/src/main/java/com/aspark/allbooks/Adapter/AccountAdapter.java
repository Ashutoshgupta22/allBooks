package com.aspark.allbooks.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspark.allbooks.R;

import org.junit.Test;

import java.util.Arrays;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.viewHolder> {

    String[] accountList = new String[]{"Profile","Theme","Search","Contact Developer"};


    public AccountAdapter() {
    }

    @NonNull
    @Override
    public AccountAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.viewHolder holder, int position) {

        holder.textView.setText(accountList[position]);

    }

    @Override
    public int getItemCount() {
        return accountList.length;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.account_item_textView);
        }
    }
}
