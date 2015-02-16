package com.aspiration.lalookhata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import butterknife.InjectView;

public class MainActivity extends Activity {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    static OnSwipeTouchListener myOnTouchListener;
    DbHelper mydb;
    ArrayList<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DbHelper(this);

        accounts = mydb.getAllAccounts();
        for(int i=0;i<accounts.size();i++){
            Log.e("Detail:",accounts.get(i).getName()+" "+accounts.get(i).getId()+" " +accounts.get(i).getBalance());
        }
        /*Collections.sort(accounts,new Comparator<Account>() {
            @Override
            public int compare(Account lhs, Account rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });*/

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,1,false));
        //recyclerView.setHasFixedSize(true);
        //recyclerView.scrollToPosition(0);
        adapter = new CustomAdapter(this.getApplicationContext(), accounts);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //TextView emptyView = (TextView)findViewById(R.id.emptyView);

        myOnTouchListener = new OnSwipeTouchListener(this){
            @Override
            public void onTapDouble(View v) {
                changeBalance(recyclerView.getChildPosition(v));
            }
            @Override
            public void onSwipeRight(View v) {
                deleteAccount(recyclerView.getChildPosition(v));
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void AddAccountClick(View v){
        showDialog(R.layout.add_account, R.string.ok_btn, false,
                getString(R.string.add_title), null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = (EditText) ((AlertDialog) dialog).findViewById(R.id.name);
                        EditText amount = (EditText) ((AlertDialog) dialog).findViewById(R.id.amount);
                        if (name.getText().length() > 0) {
                            mydb.addAccount(name.getText().toString(), (amount.getText().length() > 0) ? Integer.parseInt(amount.getText().toString()) : 0);
                            accounts.add(new Account(adapter.getItemCount() + 1, name.getText().toString(), (amount.getText().length() > 0) ? Integer.parseInt(amount.getText().toString()) : 0));
                        }

                        //Check if it is working without it !!

                        Collections.sort(accounts, new Comparator<Account>() {
                            @Override
                            public int compare(Account lhs, Account rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
    });
    }

    private void changeBalance(final int position){

        showDialog(R.layout.change_balance,R.string.ok_btn,false,
            accounts.get(position).getName()+"'s Account",null,
            new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText = (EditText)((AlertDialog) dialog).findViewById(R.id.editBal);
                    if (editText.getText().length() > 0) {
                        Switch sw = (Switch)((AlertDialog)dialog).findViewById(R.id.type);
                        if(sw.isChecked())
                            accounts.get(position).setBalance(accounts.get(position).getBalance() - Integer.parseInt(editText.getText().toString()));
                        else
                            accounts.get(position).setBalance(accounts.get(position).getBalance() + Integer.parseInt(editText.getText().toString()));
                        mydb.changeBalance(accounts.get(position).getId(), accounts.get(position).getBalance());
                        adapter.notifyItemChanged(position);
                    }
                }
            });
    }

    private void deleteAccount(final int position){

        showDialog(0,R.string.ok,true,
                getString(R.string.close_acc),"Close "+ accounts.get(position).getName()+"'s Account?",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(mydb.DeleteAcc(accounts.get(position).getId()) != 0){
                            accounts.remove(position);
                            adapter.notifyItemRemoved(position);

                        }
                    }
                });
    }
    private void showDialog(int layout,int ok,boolean cancel,String title,String msg,
                            DialogInterface.OnClickListener okListen){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(layout != 0){
            builder.setView(LayoutInflater.from(this).inflate(layout, null));
        }
        if(cancel){
            builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        if(msg != null){
            builder.setMessage(msg);
        }
        builder.setPositiveButton(ok,okListen)
                .setTitle(title);

        AlertDialog alert = builder.create();
        alert.show();

        Resources resources = alert.getContext().getResources();
        int color = resources.getColor(R.color.menu);

        int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
        TextView alertTitle = (TextView)alert.getWindow().getDecorView().findViewById(alertTitleId);
        alertTitle.setTextColor(color);

        int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
        View titleDivider = alert.getWindow().getDecorView().findViewById(titleDividerId);
        titleDivider.setBackgroundColor(color);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}
