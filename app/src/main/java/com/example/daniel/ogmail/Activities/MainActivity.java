package com.example.daniel.ogmail.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.OGM.CRH;
import com.example.daniel.ogmail.OGM.Email;
import com.example.daniel.ogmail.OGM.EmailProxy;
import com.example.daniel.ogmail.application.EmailAdapter;
import com.example.daniel.ogmail.application.InboxEmail;
import com.example.daniel.ogmail.OGM.EmailComparator;
import com.example.daniel.ogmail.OGM.Response;
import com.example.daniel.ogmail.R;
import com.example.daniel.ogmail.application.Internet;
import com.example.daniel.ogmail.application.MemoryManager;
import com.example.daniel.ogmail.application.ToastManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.Menu;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<InboxEmail> inbox;
    public static volatile ArrayAdapter adapter;
    public static String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetInternet();
        myEmail = MemoryManager.getInstance().getMyEmail(this);
        setTitle(myEmail + "\'s Inbox");

        final Context context = this;

        ListView list = findViewById(R.id.listEmail);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InboxEmail iemail = inbox.get(position);
                iemail.wasRead = true;
                inbox.set(position, iemail);
                MemoryManager.getInstance().UpdateInboxEmails(context, inbox);
                Intent intent = new Intent(context, ShowActivity.class);
                intent.putExtra("email", iemail);
                startActivity(intent);

            }
        });

        LoadRegisterActivity();

        inbox = MemoryManager.getInstance().loadInboxEmails(this);
        Collections.sort(inbox, new EmailComparator());
        adapter = new EmailAdapter(this, inbox);
        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    void LoadRegisterActivity(){

        Boolean isFirstRun = getSharedPreferences(
                "shared preferences",
                MODE_PRIVATE
        ).getBoolean("isFirstRun", true);
        //isFirstRun = true;
        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(this, RegisterActivity.class));


            getSharedPreferences("shared preferences", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.new_email){
            Intent intent = new Intent(this, EmailActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.update){
            update();
        }
        else if(item.getItemId() == R.id.clear){
            MemoryManager.getInstance().clearMemory(this);
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        return true;
    }



    public void update(){
        final String myEmail = MemoryManager.getInstance().getMyEmail(this);
        final Context context = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = EmailProxy.getInstance().getEmails(myEmail, null);
                //System.out.println(response);

                String pattern = "[^\"$]+\\$[^\"$]+\\$[^\"$]+";

                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(response);
                while (m.find()) {
                    String match = m.group(0);
                    InboxEmail email = new InboxEmail(new Email(match));
                    inbox.add(new InboxEmail(email));
                    MemoryManager.getInstance().SaveInboxEmail(context, email);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(inbox, new EmailComparator());
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
        thread.start();

    }

    public void SetInternet(){
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        Internet.ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
