package com.silencedut.asynctaskschedulertest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by SilenceDut on 16/7/22.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }


}
