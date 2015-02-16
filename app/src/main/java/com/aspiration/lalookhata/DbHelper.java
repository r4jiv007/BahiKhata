package com.aspiration.lalookhata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abhi on 13/02/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bahikhata.db";
    public static final String ACCOUNT_TABLE_NAME = "accounts";
    public static final String ACCOUNT_COLUMN_ID = "id";
    public static final String ACCOUNT_COLUMN_NAME = "name";
    public static final String ACCOUNT_COLUMN_BALANCE = "balance";
    public static final int DATABASE_VERSION = 2;
    ArrayList<Account> accounts;

    public DbHelper(Context context){
        super(context,DB_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ACCOUNT_TABLE_NAME+"("+ACCOUNT_COLUMN_ID+" integer primary key,"+ACCOUNT_COLUMN_NAME+" text,"+ACCOUNT_COLUMN_BALANCE+" integer);");
        Log.e("Create","Table Created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account");
        onCreate(db);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACCOUNT_TABLE_NAME);
        return numRows;
    }

    public ArrayList<Account> getAllAccounts(){
        accounts = new ArrayList<Account>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from "+ACCOUNT_TABLE_NAME,null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            accounts.add(new Account(res.getInt(res.getColumnIndex(ACCOUNT_COLUMN_ID)),res.getString(res.getColumnIndex(ACCOUNT_COLUMN_NAME)), res.getInt(res.getColumnIndex(ACCOUNT_COLUMN_BALANCE))));
            res.moveToNext();
        }
        return accounts;
    }

    public boolean addAccount(String name,int balance){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_COLUMN_NAME,name);
        contentValues.put(ACCOUNT_COLUMN_BALANCE,balance);
        db.insert(ACCOUNT_TABLE_NAME,null,contentValues);
        return true;
    }

    public boolean changeBalance(Integer id,Integer balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_COLUMN_BALANCE,balance);
        db.update(ACCOUNT_TABLE_NAME,contentValues,"id=?",new String[]{Integer.toString(id)});
        return true;
    }
    public Integer DeleteAcc(Integer Id){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Value",String.valueOf(Id));
        return db.delete(ACCOUNT_TABLE_NAME,ACCOUNT_COLUMN_ID+"=?",new String[]{String.valueOf(Id)});
    }
    public void DeleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ACCOUNT_TABLE_NAME);
    }
}
