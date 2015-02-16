package com.aspiration.lalookhata;

import android.app.Application;

import com.shamanland.fonticon.FontIconTypefaceHolder;

/**
 * Created by abhi on 13/02/15.
 */
public class khataApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FontIconTypefaceHolder.init(getAssets(),"icons.ttf");
    }
}
