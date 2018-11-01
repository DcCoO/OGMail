package com.example.daniel.ogmail.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.OGM.Email;
import com.example.daniel.ogmail.EmailAdapter;
import com.example.daniel.ogmail.InboxEmail;
import com.example.daniel.ogmail.OGM.OGM;
import com.example.daniel.ogmail.OGM.Response;
import com.example.daniel.ogmail.R;
import com.example.daniel.ogmail.application.MemoryManager;
import com.example.daniel.ogmail.application.ToastManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import android.view.Menu;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<InboxEmail> inbox;
    public static volatile ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MemoryManager.getInstance().clearMemory(this);

        setTitle("Inbox");

        final Context context = this;

        ListView list = (ListView) findViewById(R.id.listEmail);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Email email = (Email) inbox.get(position);
                Intent intent = new Intent(context, ShowActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);

            }
        });

        LoadRegisterActivity();

        //Email e = new Email(new Date(), "daniel", new String[0], "teste", "this is a test");

        //ArrayList<InboxEmail> inbox = new ArrayList<>();
        //inbox.add(new InboxEmail(e));
        //ArrayAdapter adapter = new EmailAdapter(this, inbox);
        //list.setAdapter(adapter);

        inbox = MemoryManager.getInstance().loadInboxEmails(this);
        adapter = new EmailAdapter(this, inbox);
        list.setAdapter(adapter);

        update();
    }

    void LoadRegisterActivity(){

        Boolean isFirstRun = getSharedPreferences(
                "shared preferences",
                MODE_PRIVATE
        ).getBoolean("isFirstRun", true);

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
        OGM.getInstance().startTracking(myEmail, new Callback() {
            @Override
            public void execute(Response response) {
                Email[] emails = OGM.getInstance().getEmails(myEmail, new Callback() {

                    @Override
                    public void execute(Response response) {
                        ToastManager.show("Emails received!", 0, (Activity) context);

                    }
                });
                for(int i = 0; i < emails.length; i++){
                    System.out.println(emails[i]);
                    InboxEmail iemail = new InboxEmail(emails[i]);
                    inbox.add(new InboxEmail(iemail));
                    MemoryManager.getInstance().save(context, iemail, MemoryManager.SaveType.INBOX);
                }

                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                    OGM.getInstance().clearInbox(myEmail, new Callback() {
                        @Override
                        public void execute(Response response) {
                            update();
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }


            }
        });
    }
}
