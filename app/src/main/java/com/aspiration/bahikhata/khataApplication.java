package com.aspiration.bahikhata;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.shamanland.fonticon.FontIconTypefaceHolder;

import java.lang.reflect.Field;

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

