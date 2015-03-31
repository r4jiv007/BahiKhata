package com.aspiration.bahikhata;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
                        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if(actionId == EditorInfo.IME_ACTION_DONE){
                                    StartBtnClick();
                                }
                                return false;
                            }
                        });
                        name.requestFocus();
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(name,InputMethodManager.SHOW_IMPLICIT);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
                        startActivity(intent);
                    }
                } else {
                    // Failure!
                }
            }
        });
    }
    public void StartBtnClick(){
        if(name.getText().toString().isEmpty()){
            name.setError("Required");
        }
        else{
            ParseObject identity = new ParseObject("Identity");
            identity.put("name", name.getText().toString());
            identity.pinInBackground();

            Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
            startActivity(intent);
        }

    }

}
