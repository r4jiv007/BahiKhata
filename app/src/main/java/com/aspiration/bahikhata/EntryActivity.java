package com.aspiration.bahikhata;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.shamanland.fonticon.FontIconDrawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by abhi on 11/03/15.
 */
public class EntryActivity extends SherlockActivity{

    @InjectView(R.id.accountBalance) protected TextView accountBalance;
    DbHelper mydb;
    int accountId;
    Account account;
    SimpleCursorAdapter entryAdapter;
    Cursor cursor;
    Typeface myFontRegular,myFontLight,myFontBold,myFontSemibold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        //Fonts
        myFontRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        myFontLight = Typeface.createFromAsset(getAssets(),"OpenSans-Light.ttf");
        myFontBold = Typeface.createFromAsset(getAssets(),"OpenSans-Bold.ttf");
        myFontSemibold = Typeface.createFromAsset(getAssets(),"OpenSans-Semibold.ttf");

        //Action Bar
        final ActionBar actionBar = getSupportActionBar();

        ButterKnife.inject(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        accountId = extras.getInt("AccountId");

        mydb = new DbHelper(this);

        final ListView listView = (ListView)findViewById(R.id.entriesList);

        listView.addHeaderView(getLayoutInflater().inflate(R.layout.list_header_entry,null));
        cursor = mydb.getEntriesById(accountId);
        entryAdapter = new SimpleCursorAdapter(this,R.layout.list_entry,cursor,
                new String[]{mydb.ENTRY_COLUMN_DATE,mydb.ENTRY_COLUMN_DETAIL,mydb.ENTRY_COLUMN_AMOUNT},
                new int[]{R.id.date,R.id.detail,R.id.amount},0);
        listView.setAdapter(entryAdapter);
        listView.setHeaderDividersEnabled(false);

        account = mydb.getAccountById(accountId);
        accountBalance.setText(String.valueOf(account.getBalance()));
        actionBar.setTitle(account.getName());

        ((TextView) getWindow().findViewById(getResources().getIdentifier(
                "action_bar_title", "id", "android"))).setTypeface(myFontBold);
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
                CallPerson();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void AddEntryClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.add_entry, null);
        builder.setView(v);
        builder.setTitle(getString(R.string.add_title_entry));
        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final TextView date = (EditText)v.findViewById(R.id.date);
        Date dt = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        date.setText(dateFormat.format(dt).toString());

        //Changing the radiobutton style.
        Drawable radio = FontIconDrawable.inflate(getResources(),R.xml.icon_radio);
        Drawable radio_sel = FontIconDrawable.inflate(getResources(),R.xml.icon_radio_sel);

        /*RadioButton type1 = (RadioButton)v.findViewById(R.id.type1);
        RadioButton type2 = (RadioButton)v.findViewById(R.id.type2);
        *//*type1.setButtonDrawable(icon);
        type1.setCompoundDrawables(icon,icon,icon,icon);
        type1.setBackground(icon);*//*
        type1.setButtonDrawable(radio_sel);
        type2.setButtonDrawable(radio);*/

        builder.setPositiveButton(R.string.ok, null);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 EditText amount = (EditText) ((AlertDialog) dialog).findViewById(R.id.amount);
                 EditText detail = (EditText) ((AlertDialog) dialog).findViewById(R.id.detail);

                 RadioGroup radioGroupType = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.radioGroup1);
                 RadioButton radioBtnSelected;

                 if(!amount.getText().toString().isEmpty() && !date.getText().toString().isEmpty()){

                     int selected = radioGroupType.getCheckedRadioButtonId();
                     radioBtnSelected = (RadioButton) ((AlertDialog) dialog).findViewById(selected);
                     String type = radioBtnSelected.getText().toString();

                     if(type.equals("paid")){
                         account.setBalance(account.getBalance() - Integer.parseInt(amount.getText().toString()));
                         mydb.addEntry(date.getText().toString(),detail.getText().toString(),-Float.valueOf(amount.getText().toString()),accountId);
                     }
                     else{
                         account.setBalance(account.getBalance() + Integer.parseInt(amount.getText().toString()));
                         mydb.addEntry(date.getText().toString(),detail.getText().toString(),Float.valueOf(amount.getText().toString()),accountId);
                     }

                     mydb.changeBalance(account.getId(), account.getBalance());
                     accountBalance.setText(String.valueOf(account.getBalance()));
                     Cursor cursor1 = mydb.getEntriesById(accountId);
                     entryAdapter.changeCursor(cursor1);

                     entryAdapter.notifyDataSetChanged();

                     dialog.dismiss();
                 }
                 else if(amount.getText().toString().isEmpty()){
                     amount.setError("Please enter the Amount");
                 }
                 else if(date.getText().toString().isEmpty()){
                     date.setError("Please enter the Date");
                 }
             }
         });

        Resources resources = dialog.getContext().getResources();
        int color = resources.getColor(R.color.menu);

        int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
        TextView alertTitle = (TextView)dialog.getWindow().getDecorView().findViewById(alertTitleId);
        alertTitle.setTextColor(color);

        int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
        titleDivider.setBackgroundColor(color);
    }
    public void PickDate(View v){
        System.out.println("hello!");

        /*DialogFragment dialogFragment = new DatePickerFra();
        dialogFragment.show(getSupportFragmentManager(),"datePicker");*/
    }

    public void CallPerson(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + account.getContact().toString()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    protected void onDestroy() {
        super.onDestroy();
        if(mydb != null ) mydb.close();
        mydb = null;
    }
    /*private static class DatePickerFragment extends DatePickerFragment implements DatePickerDialog.OnDateSetListener{
        Oncreate
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        }
    }*/

}
