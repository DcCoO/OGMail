package com.example.daniel.ogmail.Activities;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.daniel.ogmail.OGM.Email;
import com.example.daniel.ogmail.R;
import com.example.daniel.ogmail.application.Internet;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Email email = (Email) getIntent().getSerializableExtra("email");

        setTitle(email.from + "\'s Email");
        SetInternet();

        EditText from = findViewById(R.id.showFrom);
        EditText subject = findViewById(R.id.showSubject);
        EditText body = findViewById(R.id.showBody);

        from.setKeyListener(null);
        subject.setKeyListener(null);
        body.setKeyListener(null);

        from.setText(email.from);
        subject.setText(email.subject);
        body.setText(email.body);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    public void SetInternet(){
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        Internet.ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
