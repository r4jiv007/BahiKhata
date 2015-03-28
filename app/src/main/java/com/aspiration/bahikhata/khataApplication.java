package com.aspiration.bahikhata;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import java.lang.reflect.Field;

/**
 * Created by abhi on 13/02/15.
 */
public class khataApplication extends Application {
    private static Context mContext;

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        khataApplication.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FontIconTypefaceHolder.init(getAssets(),"icons.ttf");

        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Transaction.class);
        Parse.initialize(this, "7jvGqmAF85p0EnxSkYI8lecW8evmvb8wnUNaRqvX", "BhIoNiXvBSn6iEbmZR0rekKfWtrXaEnoFMHQHbUu");

        /*ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);*/

    }
}

