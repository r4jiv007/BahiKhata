package com.aspiration.bahikhata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.shamanland.fonticon.FontIconDrawable;

import java.text.NumberFormat;
import java.util.Locale;

public class AccountViewActivity extends SherlockActivity{
    DbHelper mydb;
    static EditText name,contact,place;
    SimpleCursorAdapter accountAdapter;
    Cursor cursor;
    NumberFormat formatter;
    ListView listView;
    float total;
    TextView totalBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        listView = (ListView)findViewById(R.id.accList);

        //Money formatter
        formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);

        //Action Bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        actionBar.setTitle(extras.getString("bahiname"));

        //Playing with Data.
        mydb = new DbHelper(this);
        cursor = mydb.getAllAccounts();

        accountAdapter = new SimpleCursorAdapter(this,R.layout.list_account,cursor,
                new String[]{mydb.ACCOUNT_COLUMN_NAME,mydb.ACCOUNT_COLUMN_BALANCE},
                new int[]{R.id.name,R.id.balance},0);

        listView.setHeaderDividersEnabled(false);
        listView.setFooterDividersEnabled(true);

        accountAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                TextView v = (TextView) view;

                if(columnIndex == cursor.getColumnIndex(mydb.ACCOUNT_COLUMN_BALANCE)){
                    float balance = Float.valueOf(cursor.getString(cursor.getColumnIndex(mydb.ACCOUNT_COLUMN_BALANCE)));
                    v.setText(formatter.format(balance));

                    if(balance < 0)
                        v.setTextColor(getResources().getColor(R.color.red));
                    else
                        v.setTextColor(getResources().getColor(R.color.green));

                    total += balance;

                    return true;
                }

                return false;
            }
        });

        listView.setAdapter(accountAdapter);
        totalBalance = (TextView)findViewById(R.id.totalBalance);
        totalBalance.setText(String.valueOf(total));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.menu_accountview, menu);

        Drawable icon = FontIconDrawable.inflate(getResources(),R.xml.icon_calendar);
        menu.findItem(R.id.timeView).setIcon(icon);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.timeView:
                SwitchView();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SwitchView(){
        Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
        intent.putExtra("bahiname", getActionBar().getTitle());
        startActivity(intent);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

}
