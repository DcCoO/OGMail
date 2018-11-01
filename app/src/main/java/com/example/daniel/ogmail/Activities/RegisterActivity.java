package com.example.daniel.ogmail.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.daniel.ogmail.OGM.OGM;
import com.example.daniel.ogmail.OGM.Response;
import com.example.daniel.ogmail.R;
import com.example.daniel.ogmail.application.MemoryManager;
import com.example.daniel.ogmail.application.ToastManager;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register");

        final EditText text = findViewById(R.id.email_field);

        Button button = findViewById(R.id.register);
        final Context context = this;

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                CRH.register(text.getText().toString(), new Callback() {
                    @Override
                    public void execute(Response response) {

                        if(response == Response.TIMEOUT || !isNetworkAvailable()) {
                            ToastManager.show("Internet not available.", 1, (Activity) context);
                        }
                        else if(response == Response.ALREADY_REGISTERED) {
                            ToastManager.show("Email already registered.", 1, (Activity) context);
                        }
                        else if(response == Response.REGISTER_SUCCESS) {
                            MemoryManager.getInstance().SaveMyEmail(context, text.getText().toString());
                            ToastManager.show("Email successfully registered.", 1, (Activity) context);
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                });
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
