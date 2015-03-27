package com.aspiration.bahikhata;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by abhi on 26/03/15.
 */
public class StartActivity extends SherlockActivity{
    TextView name;
    TextView bahiname;
    DbHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mydb = new DbHelper(this);
        Cursor cursor = mydb.getIdentity();

        if(cursor.getCount() > 0){
            Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
            cursor.moveToFirst();
            intent.putExtra("bahiname", cursor.getString(cursor.getColumnIndex(mydb.IDENTITY_BAHINAME)));
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_start);
            name = (TextView)findViewById(R.id.name);
            bahiname = (TextView)findViewById(R.id.bahiname);
        }

    }
    public void StartBtnClick(View v){
        if(name.getText().toString().isEmpty()){
            name.setError("Required");
        }
        else if(bahiname.getText().toString().isEmpty()){
            bahiname.setError("Required");
        }
        else{
            mydb.addIdentity(name.getText().toString(),bahiname.getText().toString());

            Intent intent = new Intent(getApplicationContext(), TimeViewActivity.class);
            intent.putExtra("bahiname", bahiname.getText().toString());
            finish();
            startActivity(intent);

        }

    }

}
