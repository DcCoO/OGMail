package com.example.daniel.ogmail.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.OGM.CRH;
import com.example.daniel.ogmail.OGM.CryptoManager;
import com.example.daniel.ogmail.OGM.Email;
import com.example.daniel.ogmail.OGM.EmailProxy;
import com.example.daniel.ogmail.OGM.OGM;
import com.example.daniel.ogmail.OGM.Response;
import com.example.daniel.ogmail.R;
import com.example.daniel.ogmail.application.Internet;
import com.example.daniel.ogmail.application.MemoryManager;
import com.example.daniel.ogmail.application.ToastManager;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register");
        SetInternet();
        final EditText text = findViewById(R.id.email_field);

        Button button = findViewById(R.id.register);
        final Context context = this;

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String response = EmailProxy.getInstance().register(text.getText().toString(), null);
                        if (response.equals("fail")) {
                            ToastManager.show("Register failed.", 1, (Activity) context);
                        } else {
                            MemoryManager.getInstance().SaveMyEmail(context, text.getText().toString());
                            ToastManager.show("Email successfully registered.", 1, (Activity) context);
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                });
                thread.start();

            }
        });
    }

    public void SetInternet(){
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        Internet.ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
