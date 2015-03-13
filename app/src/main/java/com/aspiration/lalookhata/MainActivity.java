package com.aspiration.lalookhata;

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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.aspiration.lalookhata.util.IabHelper;
import com.aspiration.lalookhata.util.IabResult;
import com.aspiration.lalookhata.util.Inventory;
import com.aspiration.lalookhata.util.Purchase;
import com.shamanland.fonticon.FontIconDrawable;

import java.util.ArrayList;

public class MainActivity extends SherlockActivity{
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    static View.OnClickListener myClickListener;
    static View.OnClickListener myDeleteListener;
    DbHelper mydb;
    ArrayList<Account> accounts;
    IabHelper mHelper;
    String TAG="Error:";
    boolean isFullVersion = false;
    static  final String SKU_FULL = "full";
    static final int RC_REQUEST = 1001;
    static final int PICK_CONTACT=1;
    static EditText name;
    static EditText contact;
    SimpleCursorAdapter accountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Action Bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        //Playing with Data.
        mydb = new DbHelper(this);

        final ListView listView = (ListView)findViewById(R.id.accList);
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.list_header_account,null));
        accountAdapter = new SimpleCursorAdapter(this,R.layout.list_account,mydb.getAllAccounts(),
                new String[]{mydb.ACCOUNT_COLUMN_NAME,mydb.ACCOUNT_COLUMN_PLACE,mydb.ACCOUNT_COLUMN_BALANCE},
                new int[]{R.id.name,R.id.place,R.id.balance},0);
        listView.setAdapter(accountAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),EntryActivity.class);
                intent.putExtra("AccountId",1);
                        //accounts.get(recyclerView.getChildPosition(v)).getId());
                startActivity(intent);
            }
        });

        /*myDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(recyclerView.getChildPosition((View)v.getParent()));
            }
        };*/

        /*
        mHelper = new IabHelper(this,getResources().getString(R.string.base64EncodedPublicKey));
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
            @Override
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess()){
                    Log.e(TAG,"Problem setting up in app billing"+result);
                }
                Log.e(TAG,"In app billing setup successfully!");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
        */

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener(){
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if(result.isFailure()){
                Log.e(TAG,"Error purchasing!"+ result);
            }
            else if(info.getSku().equals(SKU_FULL)){
                //If purchased then unlock
                Log.e(TAG,"Full Version Purchased!");
                isFullVersion = true;
            }
        }
    };
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            if(result.isFailure()){

            }
            else{
                isFullVersion = inv.hasPurchase(SKU_FULL);
            }
        }
    };

    public void PurchaseFullVersion(View v){
        mHelper.launchPurchaseFlow(this,SKU_FULL,RC_REQUEST,mPurchaseFinishedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHelper != null ) mHelper.dispose();
        mHelper = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.menu_main, menu);
        // Inflate the menu; this adds items to the action bar if it is present.

        Drawable icon = FontIconDrawable.inflate(getResources(),R.xml.icon_user_add);
        menu.findItem(R.id.user_add).setIcon(icon);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.user_add:
                AddAccountClick();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);

                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:"+cNumber);
                            contact.setText(phones.getString(phones.getColumnIndex("data1")));
                        }
                        name.setText(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    }
                }
                break;
        }
    }

    public void PickContactClick(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    public void AddAccountClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.add_account, null));
        builder.setTitle(getString(R.string.add_title));
        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = (EditText) ((AlertDialog) dialog).findViewById(R.id.name);
                EditText place = (EditText) ((AlertDialog) dialog).findViewById(R.id.place);
                contact = (EditText) ((AlertDialog) dialog).findViewById(R.id.contact);

                if (name.getText().length() > 0) {
                    mydb.addAccount(name.getText().toString(), place.getText().toString(), Long.valueOf(contact.getText().toString()), 0);
                    //accounts.add(new Account(adapter.getItemCount() + 1, name.getText().toString(), place.getText().toString(), Long.valueOf(contact.getText().toString()), 0L));
                }
                accountAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        Resources resources = alert.getContext().getResources();
        int color = resources.getColor(R.color.menu);

        int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
        TextView alertTitle = (TextView)alert.getWindow().getDecorView().findViewById(alertTitleId);
        alertTitle.setTextColor(color);

        int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
        View titleDivider = alert.getWindow().getDecorView().findViewById(titleDividerId);
        titleDivider.setBackgroundColor(color);
    }

    private void deleteAccount(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+ accounts.get(position).getName()+"'s Account?");

        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(mydb.DeleteAcc(accounts.get(position).getId()) != 0){
                    accounts.remove(position);
                    adapter.notifyItemRemoved(position);

                }
            }
        });
        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        Resources resources = alert.getContext().getResources();
        int color = resources.getColor(R.color.menu);

        int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
        TextView alertTitle = (TextView)alert.getWindow().getDecorView().findViewById(alertTitleId);
        alertTitle.setTextColor(color);

        int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
        View titleDivider = alert.getWindow().getDecorView().findViewById(titleDividerId);
        titleDivider.setBackgroundColor(color);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

}