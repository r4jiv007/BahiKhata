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
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String ACCOUNT_COLUMN_ID = "_id";
    public static final String ACCOUNT_COLUMN_NAME = "name";
    public static final String ACCOUNT_COLUMN_PLACE = "place";
    public static final String ACCOUNT_COLUMN_CONTACT = "contact";
    public static final String ACCOUNT_COLUMN_BALANCE = "balance";

    public static final String TABLE_ENTRY = "entries";
    public static final String ENTRY_COLUMN_ID = "_id";
    public static final String ENTRY_COLUMN_DATE = "date";
    public static final String ENTRY_COLUMN_DETAIL = "detail";
    public static final String ENTRY_COLUMN_AMOUNT = "amount";
    public static final String ENTRY_COLUMN_ACCOUNT_ID = "accountId";

    public static final int DATABASE_VERSION = 2;

    public DbHelper(Context context){
        super(context,DB_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_ACCOUNT+"("
                +ACCOUNT_COLUMN_ID+" integer primary key,"
                +ACCOUNT_COLUMN_NAME+" text,"
                +ACCOUNT_COLUMN_PLACE+" text,"
                +ACCOUNT_COLUMN_CONTACT+" long,"
                +ACCOUNT_COLUMN_BALANCE+" float);");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_ENTRY+"("
                +ENTRY_COLUMN_ID+" integer primary key,"
                +ENTRY_COLUMN_DATE+" text,"
                +ENTRY_COLUMN_DETAIL+" text,"
                +ENTRY_COLUMN_AMOUNT+" float,"
                +ENTRY_COLUMN_ACCOUNT_ID+" integer);");
        Log.e("Create","Tables Created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE FROM "+TABLE_ACCOUNT);
        db.execSQL("DELETE FROM "+TABLE_ENTRY);
        onCreate(db);
    }

    public int numberOfAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_ACCOUNT);
        return numRows;
    }

    public Cursor getAllAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_ACCOUNT + " ORDER BY "+ ACCOUNT_COLUMN_NAME + " ASC",null);
        return cursor;
    }

    public Account getAccountById(int AccountId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_ACCOUNT + " where " + ACCOUNT_COLUMN_ID + " = ?", new String[]{String.valueOf(AccountId)});
        res.moveToFirst();
        return new Account(res.getInt(res.getColumnIndex(ACCOUNT_COLUMN_ID)),
                    res.getString(res.getColumnIndex(ACCOUNT_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(ACCOUNT_COLUMN_PLACE)),
                    res.getLong(res.getColumnIndex(ACCOUNT_COLUMN_CONTACT)),
                    res.getLong(res.getColumnIndex(ACCOUNT_COLUMN_BALANCE)));
    }

    public boolean addEntry(String date,String detail,float amount,int accountId){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTRY_COLUMN_DATE,date);
        contentValues.put(ENTRY_COLUMN_DETAIL, detail);
        contentValues.put(ENTRY_COLUMN_AMOUNT,amount);
        contentValues.put(ENTRY_COLUMN_ACCOUNT_ID,accountId);
        db.insert(TABLE_ENTRY,null,contentValues);
        return true;
    }

    public Cursor getEntriesById(int accountId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+ TABLE_ENTRY + " where " + ENTRY_COLUMN_ACCOUNT_ID + " = ?" + " ORDER BY "+ ENTRY_COLUMN_DATE + " DESC",new String[]{String.valueOf(accountId)});
        return cursor;
    }

    public boolean addAccount(String name,String place,Long contact,float balance){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_COLUMN_NAME,name);
        contentValues.put(ACCOUNT_COLUMN_PLACE,place);
        contentValues.put(ACCOUNT_COLUMN_CONTACT,contact);
        contentValues.put(ACCOUNT_COLUMN_BALANCE,balance);
        db.insert(TABLE_ACCOUNT, null, contentValues);
        return true;
    }

    public boolean changeBalance(Integer id,Long balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_COLUMN_BALANCE,balance);
        db.update(TABLE_ACCOUNT,contentValues,ACCOUNT_COLUMN_ID+"=?",new String[]{Integer.toString(id)});
        return true;
    }

    public void DeleteAcc(Integer Id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT,ACCOUNT_COLUMN_ID+"=?",new String[]{String.valueOf(Id)});
        db.delete(TABLE_ENTRY,ENTRY_COLUMN_ACCOUNT_ID+"=?",new String[]{String.valueOf(Id)});
    }
}
