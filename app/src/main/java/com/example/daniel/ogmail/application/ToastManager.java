package com.example.daniel.ogmail.application;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


public class ToastManager {



    public static void show(final String text, final int fast, final Activity activity){
        //Toast.makeText(context, text, fast).show();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity.getBaseContext(),text, fast).show();
            }
        });

    }
}
