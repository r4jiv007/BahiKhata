package com.aspiration.lalookhata;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;

/**
 * Created by abhi on 13/02/15.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private Context mContext;
        private ArrayList<Account> accounts;

        public CustomAdapter(Context context, ArrayList<Account> accounts){
            this.accounts = accounts;
            this.mContext = context;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView nameView;
            private final TextView balanceView;

            public ViewHolder(View v){
                super(v);
                nameView = (TextView)v.findViewById(R.id.name);
                balanceView = (TextView)v.findViewById(R.id.balance);
            }

            public TextView getNameView() {
                return nameView;
            }

            public TextView getBalanceView() {
                return balanceView;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list,parent,false);
            v.setOnTouchListener(MainActivity.myOnTouchListener);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.nameView.setText(accounts.get(position).getName());
            holder.balanceView.setText(accounts.get(position).getBalance().toString());
        }

        @Override
        public int getItemCount() {
            return accounts.size();
        }
}
