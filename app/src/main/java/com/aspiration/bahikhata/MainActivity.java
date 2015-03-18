package com.aspiration.bahikhata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.vending.billing.IInAppBillingService;
import com.aspiration.bahikhata.util.IabHelper;
import com.aspiration.bahikhata.util.IabResult;
import com.aspiration.bahikhata.util.Inventory;
import com.aspiration.bahikhata.util.Purchase;
import com.shamanland.fonticon.FontIconDrawable;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends SherlockActivity{
    DbHelper mydb;
    IabHelper mHelper;
    String TAG="Error:",SKU_FULL = "full",newName = "",newNumber = "";
    boolean isFullVersion = false;
    static final int RC_REQUEST = 1001,PICK_CONTACT=1;
    static EditText name,contact,place;
    SimpleCursorAdapter accountAdapter;
    IInAppBillingService mService;
    Cursor cursor;
    Typeface myFontRegular,myFontLight,myFontBold,myFontSemibold;

    @InjectView(R.id.accList) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        //Fonts
        myFontRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        myFontLight = Typeface.createFromAsset(getAssets(),"OpenSans-Light.ttf");
        myFontBold = Typeface.createFromAsset(getAssets(),"OpenSans-Bold.ttf");
        myFontSemibold = Typeface.createFromAsset(getAssets(),"OpenSans-Semibold.ttf");

        //Action Bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        ((TextView) getWindow().findViewById(getResources().getIdentifier(
                "action_bar_title", "id", "android"))).setTypeface(myFontBold);

        /*TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);

        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new TypefaceSpan(this,""));*/


        //Playing with Data.
        mydb = new DbHelper(this);

        listView.addHeaderView(getLayoutInflater().inflate(R.layout.list_header_account,null));
        cursor = mydb.getAllAccounts();

        accountAdapter = new SimpleCursorAdapter(this,R.layout.list_account,cursor,
                new String[]{mydb.ACCOUNT_COLUMN_ID,mydb.ACCOUNT_COLUMN_NAME,mydb.ACCOUNT_COLUMN_PLACE,mydb.ACCOUNT_COLUMN_BALANCE},
                new int[]{R.id.id,R.id.name,R.id.place,R.id.balance},0);

        listView.setAdapter(accountAdapter);
        listView.setHeaderDividersEnabled(false);
        //listView.get

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),EntryActivity.class);

                TextView textView = (TextView)view.findViewById(R.id.id);
                final int accountId = Integer.valueOf(textView.getText().toString());
                intent.putExtra("AccountId",accountId);

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        /*mHelper = new IabHelper(this,getResources().getString(R.string.base64EncodedPublicKey));
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
            @Override
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess()){
                    Log.e(TAG,"Problem setting up in app billing"+result);
                }
                Log.e(TAG,"In app billing setup successfully!");
                //mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = null;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //mService = IInAppBillingService.Stub.asInterface(service);

            }
        };*/
        //mService


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

    public void PurchaseFullVersion(){
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
                            newNumber = phones.getString(phones.getColumnIndex("data1"));
                        }
                        newName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        AddAccountClick();
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
        if(mydb.numberOfAccounts() == 15 && !isFullVersion){
            PurchaseFullVersion();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View v = LayoutInflater.from(this).inflate(R.layout.add_account, null);
            builder.setView(v);
            builder.setTitle(getString(R.string.add_title));
            name = (EditText)v.findViewById(R.id.name);
            place = (EditText)v.findViewById(R.id.place);
            contact = (EditText)v.findViewById(R.id.contact);

            if(!newName.isEmpty()) name.setText(newName);
            if(!newNumber.isEmpty()) contact.setText(newNumber);

            builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton(R.string.ok, null);

            final AlertDialog dialog = builder.create();
            dialog.show();

            final Drawable icon_contact = FontIconDrawable.inflate(getResources(),R.xml.icon_vcard);
            name.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_contact, null);
            name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    try {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (name.getRight() - name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                // your action here
                                dialog.dismiss();
                                PickContactClick(v);

                                return true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(name.getText().toString().isEmpty()) {
                        name.setError("Name Please");
                    }
                    else{
                        mydb.addAccount(name.getText().toString(), place.getText().toString(), contact.getText().toString() , 0);
                        Cursor cursor1 = mydb.getAllAccounts();
                        accountAdapter.changeCursor(cursor1);
                        accountAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        newNumber = "";
                        newName = "";
                    }
                }
            });

            //Styling
            name.setTypeface(myFontLight);
            place.setTypeface(myFontLight);
            contact.setTypeface(myFontLight);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(myFontSemibold);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(myFontSemibold);
            ((TextView) dialog.findViewById(getResources().getIdentifier(
                    "alertTitle", "id", "android"))).setTypeface(myFontSemibold);

            Resources resources = dialog.getContext().getResources();
            int color = resources.getColor(R.color.menu);

            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView)dialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(color);

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(color);
        }
    }

    public void deleteAccountClick(View v){

        TextView textView = (TextView)((ViewGroup)(v.getParent())).findViewById(R.id.id);
        final int accountId = Integer.valueOf(textView.getText().toString());

        Account account = mydb.getAccountById(accountId);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+ account.getName()+"'s Account?");

        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

               mydb.DeleteAcc(accountId);
               cursor = mydb.getAllAccounts();
               accountAdapter.changeCursor(cursor);
               accountAdapter.notifyDataSetChanged();
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

        //Styling
        ((TextView) alert.findViewById(getResources().getIdentifier(
                "alertTitle", "id", "android"))).setTypeface(myFontSemibold);
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(myFontSemibold);
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(myFontSemibold);

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
