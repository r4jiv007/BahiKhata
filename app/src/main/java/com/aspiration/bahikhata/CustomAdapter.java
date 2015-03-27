package com.aspiration.bahikhata;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by abhi on 26/03/15.
 */
public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

    NumberFormat formatter;

    public CustomAdapter(Context context){
        super(context,new QueryFactory<ParseObject>(){
            public ParseQuery create(){
                ParseQuery query = new ParseQuery("Khata");
                //query.fromLocalDatastore();
                //query.fromPin();
                query.addDescendingOrder("date");
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

        //Money formatter
        formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);

        //TextView dateView = (TextView)v.findViewById(R.id.date);
        TextView nameView = (TextView)v.findViewById(R.id.name);
        TextView detailView = (TextView)v.findViewById(R.id.detail);
        TextView amountView = (TextView)v.findViewById(R.id.amount);

        //dateView.setText(String.valueOf(object.getDate("createdAt").getTime()));
        nameView.setText(object.getString("name"));
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
