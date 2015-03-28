package com.aspiration.bahikhata;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
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

import de.greenrobot.event.EventBus;

/**
 * Created by abhi on 11/03/15.
 */
public class TimeViewActivity extends SherlockActivity{

    TextView accountBalance;
    static String bahiname = null;
    ListView listView;
    CustomAdapter customAdapter;
    float total;
    TextView totalBalance;
    NumberFormat formatter;
    DateTimeFormatter format = DateTimeFormat.forPattern("dd MMM");
    DateTimeFormatter format1 = DateTimeFormat.forPattern("dd MMM yyyy  h:mm aa");
    Button dateSelect;

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

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);

        dateSelect = (Button)findViewById(R.id.dateSelect);
        DateTime dt = new DateTime();

        dateSelect.setText(format.print(dt));

        listView = (ListView)findViewById(R.id.entriesList);

        customAdapter = new CustomAdapter(this,dt);
        listView.setAdapter(customAdapter);
        customAdapter.loadObjects();

        //String balance = String.valueOf(account.getBalance());
        //accountBalance.setText(formatter.format(balance == ""?0L:Long.valueOf(balance)));

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

        //FindObject();
        //FindSubClass();
        //SaveSubClassObject();
    }

    private void FindSubClass(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sample5");
        query.whereEqualTo("name", "Abhijeet Goel");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if(e == null) {

                    System.out.println(parseObjects.get(0).getString("name"));
                    try {
                        System.out.println(parseObjects.get(0).getJSONArray("txs").getJSONObject(0).get("objectId"));
                    }
                    catch (JSONException e1){
                        e1.printStackTrace();
                    }

                    /*
                    ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);

                    query.whereEqualTo("ObjectId",parseObjects.get(0).getParseObject("txs").getObjectId());

                    query.findInBackground(new FindCallback<Transaction>() {
                        @Override
                        public void done(List<Transaction> transactions, com.parse.ParseException e) {
                            for (Transaction transaction:transactions){
                                System.out.println(transaction.getDetail());
                            }
                        }
                    });

                    System.out.println());*/
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        /*ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);
        //query.whereGreaterThan("dateTime",ParseQuery.getQuery("Sample2").get("names"));
        query.whereEqualTo("type",ParseQuery.getQuery(""))

        query.findInBackground(new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> transactions, com.parse.ParseException e) {
                for (Transaction transaction:transactions){
                    //transaction.getName();
                }
            }
        });*/

    }
    //Apply subclasses and then find the

    private void FindObject(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sample2");
        //query.whereStartsWith("name", "Big Daddy's");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if(e == null) {
                    System.out.println(parseObjects.toString());
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    private void SaveObject(){
        ParseObject parseObject = new ParseObject("Sample2");

        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            //object.put("date", format.parseDateTime(new DateTime().toString()).toDate());
            object.put("detail", "waddup?");
            object.put("type", "credit");
            object.put("amount", 30000);
            //array.put(object);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        parseObject.put("Test",object);
        System.out.println("added!!");
        parseObject.saveInBackground();
    }

    private void SaveSubClassObject(){
        ParseObject parseObject = new ParseObject("Sample5");

        Transaction transaction = new Transaction();
        transaction.setDateTime((new DateTime(2015,3,27,3,30)).toDate());
        transaction.setAmount(3000.0);
        transaction.setDetail("Time is powerful");
        transaction.setType("cr");
        Transaction transaction1 = new Transaction();
        transaction1.setDateTime((new DateTime(2015,3,28,5,30)).toDate());
        transaction1.setAmount(4000.0);
        transaction1.setDetail("Testing out time!");
        transaction1.setType("cr");
        parseObject.addAll("txs",Arrays.asList(transaction,transaction1));

        parseObject.put("name","Imon Raza");
        parseObject.put("balance",8000.0);

        parseObject.saveInBackground();
        /*
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            //object.put("date", format.parseDateTime(new DateTime().toString()).toDate());
            object.put("detail", "waddup?");
            object.put("type", "credit");
            object.put("amount", 30000);
            //array.put(object);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        parseObject.put("Test",object);
        System.out.println("added!!");
        parseObject.saveInBackground();*/
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

    }

    public void onEvent(DatePickEvent event){

        dateSelect.setText(format.print(event.dateTime));

        customAdapter = new CustomAdapter(this,event.dateTime);
        listView.setAdapter(customAdapter);
        customAdapter.loadObjects();

        //Now repopulate the data using the the dateTime received now..



    }
    public static class DatePickEvent {
        public final DateTime dateTime;

        public DatePickEvent(DateTime dateTime){
            this.dateTime = dateTime;
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
            EventBus.getDefault().post(new DatePickEvent(new DateTime(year,monthOfYear+1,dayOfMonth,0,0)));

        }
    }

    public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

        NumberFormat formatter;

        public CustomAdapter(Context context,final DateTime date){
            super(context,new QueryFactory<ParseObject>(){
                public ParseQuery create(){
                    ParseQuery query = new ParseQuery("Transaction");
                    //query.fromLocalDatastore();
                    //query.fromPin();

                    query.whereGreaterThan("dateTime",new DateTime(date.getYear(),date.getMonthOfYear(),date.getDayOfMonth(),0,0).toDate());
                    query.whereLessThan("dateTime",new DateTime(date.getYear(),date.getMonthOfYear(),date.getDayOfMonth(),23,59).toDate());

                    query.addDescendingOrder("dateTime");
                    return query;
                }
            });
        }

        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if(v == null){
                v = View.inflate(getContext(),R.layout.list_entry,null);
            }
            super.getItemView(object, v, parent);

            //dateFormat = new SimpleDateFormat("dd/MM/yy");
            //accountBalance = (TextView)findViewById(R.id.accountBalance);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Sample5");
            //query.whereEqualTo("name", name.getText().toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                           @Override
                           public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {


                           }
                       });

            //Money formatter
            formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            formatter.setMinimumFractionDigits(0);
            formatter.setMaximumFractionDigits(0);

            TextView timeView = (TextView)v.findViewById(R.id.time);
            TextView nameView = (TextView)v.findViewById(R.id.name);
            TextView detailView = (TextView)v.findViewById(R.id.detail);
            TextView amountView = (TextView)v.findViewById(R.id.amount);

            //date object fetch only time part.
            //if in milliseconds.
            //
            DateTimeFormatter builder = DateTimeFormat.forPattern("dd MMM yyyy  h:mm aa");
            DateTime dateTime = new DateTime(object.getDate("dateTime"));

            timeView.setText(builder.print(dateTime).split("  ")[1]);

            //DateTimeFormat.forStyle("")
            //nameView.setText(object.getString("name"));
            detailView.setText(object.getString("detail"));

            Double amount = object.getNumber("amount").doubleValue();

            if(object.getString("type").equals("cr")){
                amountView.setText(formatter.format(-amount));
                amountView.setTextColor(getContext().getResources().getColor(R.color.red));
            }
            else if(object.getString("type").equals("db")){
                amountView.setText(formatter.format(amount));
                amountView.setTextColor(getContext().getResources().getColor(R.color.green));
            }
            return v;
        }
    }


}
