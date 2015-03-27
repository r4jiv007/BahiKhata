package com.aspiration.bahikhata;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.shamanland.fonticon.FontIconDrawable;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by abhi on 11/03/15.
 */
public class TimeViewActivity extends SherlockActivity{

    TextView accountBalance;
    DbHelper mydb;
    int accountId;
    Account account;
    DateFormat dateFormat;
    static String bahiname = null;
    ListView listView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(bahiname == null){
            bahiname = extras.getString("bahiname");
        }

        actionBar.setTitle(bahiname);
        accountId = 1;

        Button dateSelect = (Button)findViewById(R.id.dateSelect);
        Date dt = new Date();

        DateFormat dateFormat = new SimpleDateFormat("dd MMM");
        dateSelect.setText(dateFormat.format(dt).toString());

        listView = (ListView)findViewById(R.id.entriesList);
        //DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");

        customAdapter = new CustomAdapter(this);

        listView.setAdapter(customAdapter);
        customAdapter.loadObjects();
        listView.setHeaderDividersEnabled(true);
        listView.setFooterDividersEnabled(true);

        //String balance = String.valueOf(account.getBalance());
        //accountBalance.setText(formatter.format(balance == ""?0L:Long.valueOf(balance)));

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.menu_timeview, menu);
        // Inflate the menu; this adds items to the action bar if it is present.

        Drawable iconPlus = FontIconDrawable.inflate(getResources(), R.xml.icon_users);
        menu.findItem(R.id.accountView).setIcon(iconPlus);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {

        switch(item.getItemId()){
            case R.id.accountView:
                SwitchView();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SwitchView(){
        Intent intent = new Intent(getApplicationContext(), AccountViewActivity.class);
        intent.putExtra("bahiname", getActionBar().getTitle());

        startActivity(intent);
    }

    public void AddTxClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddTxActivity.class);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        if(mydb != null ) mydb.close();
        mydb = null;
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

            /*dt = new Date();
            date.setText(dateFormat.format(dt));*/

            //PopulateSetDate(year,monthOfYear+1,dayOfMonth);
        }
    }



}
