package com.example.lahirufernando.skinsensor;
/**
 * Created by lahirufernando on 30/11/2017.
 */
import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


public class ClickableAreaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
