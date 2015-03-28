package com.aspiration.bahikhata;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.List;

/**
 * Created by abhi on 26/03/15.
 */
public class StartActivity extends SherlockActivity{
    TextView name;
    TextView bahiname;
    //DbHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        khataApplication.setmContext(this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Identity");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    if(parseObjects.size() == 0){
                        setContentView(R.layout.activity_start);
                        name = (TextView)findViewById(R.id.name);
                        bahiname = (TextView)findViewById(R.id.bahiname);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
                        intent.putExtra("bahiname",parseObjects.get(0).getString("bahiname"));
                        startActivity(intent);
                    }
                } else {
                    // Failure!
                }
            }
        });
    }
    public void StartBtnClick(View v){
        if(name.getText().toString().isEmpty()){
            name.setError("Required");
        }
        else if(bahiname.getText().toString().isEmpty()){
            bahiname.setError("Required");
        }
        else{
            ParseObject identity = new ParseObject("Identity");
            identity.put("name", name.getText().toString());
            identity.put("bahiname", bahiname.getText().toString());
            //identity.saveInBackground();
            identity.pinInBackground();

            Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
            intent.putExtra("bahiname", bahiname.getText().toString());
            startActivity(intent);
        }

    }

}
