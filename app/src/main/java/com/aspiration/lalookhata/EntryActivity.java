package com.aspiration.lalookhata;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.shamanland.fonticon.FontIconDrawable;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by abhi on 11/03/15.
 */
public class EntryActivity extends SherlockActivity{

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    ArrayList<Entry> entries;
    DbHelper mydb;
    int accountId;
    Account account;
    @InjectView(R.id.accountBalance) protected TextView accountBalance;
    SimpleCursorAdapter entryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        //Action Bar
        final ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(account.getName());

        ButterKnife.inject(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        accountId = extras.getInt("AccountId");

        mydb = new DbHelper(this);

        final ListView listView = (ListView)findViewById(R.id.entriesList);
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.list_header_entry,null));
        entryAdapter = new SimpleCursorAdapter(this,R.layout.list_account,mydb.getEntriesById(accountId),
                new String[]{mydb.ENTRY_COLUMN_DATE,mydb.ENTRY_COLUMN_DETAIL,mydb.ENTRY_COLUMN_AMOUNT},
                new int[]{R.id.date,R.id.detail,R.id.amount},0);
        listView.setAdapter(entryAdapter);

        account = mydb.getAccountById(accountId);
        accountBalance.setText(String.valueOf(account.getBalance()));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.menu_entry, menu);
        // Inflate the menu; this adds items to the action bar if it is present.

        Drawable iconPhone = FontIconDrawable.inflate(getResources(), R.xml.icon_phone);
        Drawable iconPlus = FontIconDrawable.inflate(getResources(), R.xml.icon_plus);
        menu.findItem(R.id.entry_add).setIcon(iconPlus);
        menu.findItem(R.id.call).setIcon(iconPhone);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {

        switch(item.getItemId()){
            case R.id.entry_add:
                AddEntryClick();
                return true;
            case R.id.call:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void AddEntryClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.add_entry, null));
        builder.setTitle(getString(R.string.add_title_entry));
        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText amount = (EditText) ((AlertDialog) dialog).findViewById(R.id.amount);
                EditText date = (EditText) ((AlertDialog) dialog).findViewById(R.id.date);
                EditText detail = (EditText) ((AlertDialog) dialog).findViewById(R.id.detail);
                RadioButton type1 = (RadioButton)((AlertDialog) dialog).findViewById(R.id.type1);
                RadioButton type2 = (RadioButton)((AlertDialog) dialog).findViewById(R.id.type2);

                if(type1.isChecked()){
                    account.setBalance(account.getBalance() + Integer.parseInt(amount.getText().toString()));
                }
                else if(type2.isChecked()){
                    //amount.setText(amount.getText().toString());
                    account.setBalance(account.getBalance() - Integer.parseInt(amount.getText().toString()));
                }

                mydb.addEntry(date.getText().toString(),detail.getText().toString(),Float.valueOf(amount.getText().toString()),accountId);
                entries.add(new Entry(adapter.getItemCount()+1,date.getText().toString(),detail.getText().toString(),Long.valueOf(amount.getText().toString()),accountId));

                mydb.changeBalance(account.getId(), account.getBalance());
                accountBalance.setText(String.valueOf(account.getBalance()));

                adapter.notifyDataSetChanged();
            }
        });

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

    protected void onDestroy() {
        super.onDestroy();
        if(mydb != null ) mydb.close();
        mydb = null;
    }

}
