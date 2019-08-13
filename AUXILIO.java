package com.rushikesh.auxilio;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rushikesh on 15/1/17.
 */

public class AUXILIO extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if(FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }
}
