package com.aspiration.bahikhata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.shamanland.fonticon.FontIconDrawable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.text.format.DateFormat;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xml.sax.helpers.ParserAdapter;

import de.greenrobot.event.EventBus;

/**
 * Created by abhi on 26/03/15.
 */
public class AddTxActivity extends SherlockActivity{
    DateTimeFormatter format = DateTimeFormat.forPattern("dd MMM yyyy  h:mm aa");
    TextView detail,amount;
    AutoCompleteTextView name;
    TextView date;
    DateTime mydateTime;
    RadioGroup type;
    DateTime olddateTime;
    NumberFormat formatter;
    Boolean isFullVersion = false;
    ListView accList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addtx);

        name = (AutoCompleteTextView)findViewById(R.id.name);
        detail = (TextView)findViewById(R.id.detail);
        date = (TextView)findViewById(R.id.date);
        amount = (TextView)findViewById(R.id.amount);
        type = (RadioGroup)findViewById(R.id.type);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.add_tx_title);

        mydateTime = new DateTime();
        date.setText(mydateTime.toString(format));

        olddateTime = format.parseDateTime(date.getText().toString());

        String[] names;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Khata3");
        query.selectKeys(Arrays.asList("name"));

        List<ParseObject> results;
        try{
            results = query.find();
        }
        catch (com.parse.ParseException e){
            results = Collections.emptyList();
            e.printStackTrace();
        }
        names = new String[results.size()];

        for(int i=0;i<results.size();i++){
            names[i] = results.get(i).getString("name");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,names);
        name.setThreshold(1);
        name.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_addtx, menu);
        // Inflate the menu; this adds items to the action bar if it is present.

        Drawable iconPlus = FontIconDrawable.inflate(getResources(), R.xml.icon_check);
        menu.findItem(R.id.okBtn).setIcon(iconPlus);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.okBtn:
                AddTx();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void AddTx(){
        if(name.getText().toString().isEmpty()){
            name.setError("Required");
        }
        else if(detail.getText().toString().isEmpty()){
            detail.setError("Required");
        }
        else if(detail.getText().toString().isEmpty()){
            detail.setError("Required");
        }
        else if(amount.getText().toString().isEmpty()){
            amount.setError("Required");
        }
        else{
            int selected = type.getCheckedRadioButtonId();

            /*if(selected == R.id.credit){
                account.setBalance(account.getBalance() - Integer.parseInt(amount.getText().toString()));
                mydb.addEntry(dt.toString(), detail.getText().toString(), -Float.valueOf(amount.getText().toString()), accountId);
            }
            else{
                account.setBalance(account.getBalance() + Integer.parseInt(amount.getText().toString()));
                mydb.addEntry(dt.toString(),detail.getText().toString(),Float.valueOf(amount.getText().toString()),accountId);
            }

            mydb.changeBalance(account.getId(), account.getBalance());
            String balance = String.valueOf(account.getBalance());*/

            final String type;
            if(selected == R.id.credit)
                type = "cr";
            else
                type = "db";

            final HashMap tx = new HashMap();
            tx.put("date",format.parseDateTime(date.getText().toString()).toDate());
            tx.put("detail",detail.getText().toString());
            tx.put("type",type);
            tx.put("amount",Double.valueOf(amount.getText().toString()));

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Khata3");
            query.whereEqualTo("name", name.getText().toString());

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    if (e == null) {

                        if(parseObjects.size() == 0){
                            ParseObject transaction = new ParseObject("Khata3");
                            transaction.put("name", name.getText().toString());
                            transaction.add("txs", tx);
                            if(type == "cr")
                                transaction.put("balance",-Double.valueOf(amount.getText().toString()));
                            else
                                transaction.put("balance", Double.valueOf(amount.getText().toString()));
                            transaction.saveInBackground();
                        }
                        else{

                            parseObjects.get(0).add("txs", tx);
                            if(type == "cr")
                                parseObjects.get(0).put("balance",parseObjects.get(0).getDouble("balance")-Double.valueOf(amount.getText().toString()));
                            else
                                parseObjects.get(0).put("balance", parseObjects.get(0).getDouble("balance") + Double.valueOf(amount.getText().toString()));
                            parseObjects.get(0).saveInBackground();
                        }

                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });




            Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
            startActivity(intent);
        }

    }
    public void PickDate(View v){

        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(),"datePicker");

    }

    public void PickTime(View v){

        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.show(getFragmentManager(),"timePicker");

    }

    public void onEvent(DateEvent event){

        DateTime olddateTime = format.parseDateTime(date.getText().toString());
        DateTime newdateTime = new DateTime(event.year,event.monthOfYear+1,event.dayOfMonth,olddateTime.getHourOfDay(),olddateTime.getMinuteOfHour());
        date.setText(newdateTime.toString(format));

    }
    public void onEvent(TimeEvent event){

        DateTime olddateTime = format.parseDateTime(date.getText().toString());
        DateTime newdateTime = new DateTime(olddateTime.getYear(),olddateTime.getMonthOfYear(),olddateTime.getDayOfMonth(),event.hourOfDay,event.minute);
        date.setText(newdateTime.toString(format));
    }
    public static class DateEvent {
        public final int year;
        public final int monthOfYear;
        public final int dayOfMonth;

        public DateEvent(int dayOfMonth,int monthOfYear,int year){
            this.dayOfMonth = dayOfMonth;
            this.monthOfYear = monthOfYear;
            this.year = year;
        }

    }
    public static class TimeEvent {
        public final int hourOfDay;
        public final int minute;

        public TimeEvent(int hourOfDay, int minute) {
            this.hourOfDay = hourOfDay;
            this.minute = minute;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            DateTime dt = new DateTime();

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),this,dt.getYear(),dt.getMonthOfYear()+1,dt.getDayOfMonth());
            dialog.getDatePicker().setMaxDate(new Date().getTime());

            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            EventBus.getDefault().post(new DateEvent(dayOfMonth,monthOfYear,year));

        }
    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DateTime dt = new DateTime();

            return new TimePickerDialog(getActivity(), this, dt.getHourOfDay(), dt.getMinuteOfHour(),
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            EventBus.getDefault().post(new TimeEvent(hourOfDay,minute));

        }
    }
}
