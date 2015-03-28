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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.shamanland.fonticon.FontIconDrawable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AccountViewActivity extends SherlockActivity{
    NumberFormat formatter;
    ListView accList;
    TextView totalBalance;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        accList = (ListView)findViewById(R.id.accList);

        customAdapter = new CustomAdapter(this);
        accList.setAdapter(customAdapter);
        customAdapter.loadObjects();

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

        totalBalance = (TextView)findViewById(R.id.totalBalance);
        Double total = 0.0;

        ParseQuery<ParseObject> query = new ParseQuery("Sample5");
        query.selectKeys(Arrays.asList("balance"));
        List<ParseObject> results;
        try{
            results = query.find();
        }
        catch (com.parse.ParseException e){
            results = Collections.emptyList();
            e.printStackTrace();
        }

        for(int i=0;i<results.size();i++){
            total += results.get(i).getNumber("balance").doubleValue();
        }

        totalBalance.setText(String.valueOf(formatter.format(total)));
        if(total < 0){
            totalBalance.setTextColor(getResources().getColor(R.color.red));
        }
        else{
            totalBalance.setTextColor(getResources().getColor(R.color.green));
        }
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

    public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

        NumberFormat formatter;

        public CustomAdapter(Context context){
            super(context,new QueryFactory<ParseObject>(){
                public ParseQuery create(){
                    ParseQuery query = new ParseQuery("Sample5");
                    query.addAscendingOrder("name");
                    return query;
                }
            });
        }

        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if(v == null){
                v = View.inflate(getContext(),R.layout.list_account,null);
            }
            super.getItemView(object, v, parent);

            //dateFormat = new SimpleDateFormat("dd/MM/yy");
            //accountBalance = (TextView)findViewById(R.id.accountBalance);

            //Money formatter
            formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            formatter.setMinimumFractionDigits(0);
            formatter.setMaximumFractionDigits(0);

            TextView nameView = (TextView)v.findViewById(R.id.name);
            TextView balanceView = (TextView)v.findViewById(R.id.balance);

            nameView.setText(object.getString("name"));
            Double balance = object.getNumber("balance").doubleValue();

            if(balance < 0){
                balanceView.setText(formatter.format(balance));
                balanceView.setTextColor(getContext().getResources().getColor(R.color.red));
            }
            else if(balance > 0){
                balanceView.setText(formatter.format(balance));
                balanceView.setTextColor(getContext().getResources().getColor(R.color.green));
            }
            return v;
        }
    }
}
