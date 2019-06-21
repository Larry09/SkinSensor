package com.example.lahirufernando.skinsensor;
/**
 * Created by lahirufernando on 20/11/2017.
 */
import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Uses volley to execute and get the information needed from the webhost
 */
public class RegisterRequest extends Application {

    public void onCreate(){
        super.onCreate();
        FirebaseApp.initializeApp(this);
        if(FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
