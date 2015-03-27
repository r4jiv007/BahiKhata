package com.aspiration.bahikhata;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.parse.ParseObject;
import com.shamanland.fonticon.FontIconDrawable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.text.format.DateFormat;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.greenrobot.event.EventBus;

/**
 * Created by abhi on 26/03/15.
 */
public class AddTxActivity extends SherlockActivity{
    LocalDateTime dt;
    DateTimeFormatter format = DateTimeFormat.forPattern("dd MMM yyyy  KK:mm aa");
    TextView name,detail,date,amount;
    RadioGroup type;
    int accountId = 1;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtx);

        name = (TextView)findViewById(R.id.name);
        detail = (TextView)findViewById(R.id.detail);
        date = (TextView)findViewById(R.id.date);
        amount = (TextView)findViewById(R.id.amount);
        type = (RadioGroup)findViewById(R.id.type);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.add_tx_title);

        dt = new LocalDateTime();
        date.setText(dt.toString(format));

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

            ParseObject tx = new ParseObject("Khata");
            tx.put("name", name.getText().toString());
            tx.put("detail", detail.getText().toString());
            tx.put("date", date.getText().toString());

            if(selected == R.id.credit)
                tx.put("type","cr");
            else
                tx.put("type","db");
            tx.put("amount", Float.valueOf(amount.getText().toString()));

            //tx.pinInBackground();

            tx.saveInBackground();

            //accountBalance.setText(formatter.format(balance == ""?0L:Long.valueOf(balance)));
            //Cursor cursor1 = mydb.getEntriesById(accountId);*/


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

        if(event.isDate == true){
            LocalDateTime dateTime = new LocalDateTime(event.year,event.monthOfYear,event.dayOfMonth,dt.getHourOfDay(),dt.getMinuteOfHour());
            date.setText(dateTime.toString(format));
        }
        else{
            LocalDateTime dateTime = new LocalDateTime(dt.getYear(),dt.getMonthOfYear(),dt.getDayOfMonth(),event.hourOfDay,event.minute);
            date.setText(dateTime.toString(format));
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month  = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),this,year,month,day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());

            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            EventBus.getDefault().post(new DateEvent(dayOfMonth,monthOfYear,year,true));

        }
    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            //EventBus.getDefault().post(new Date(dayOfMonth,monthOfYear,year));
            EventBus.getDefault().post(new DateEvent(hourOfDay,minute,false));
        }
    }
}
