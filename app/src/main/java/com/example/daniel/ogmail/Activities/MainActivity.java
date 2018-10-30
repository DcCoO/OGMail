package com.example.daniel.ogmail.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.OGM.Email;
import com.example.daniel.ogmail.EmailAdapter;
import com.example.daniel.ogmail.InboxEmail;
import com.example.daniel.ogmail.OGM.OGM;
import com.example.daniel.ogmail.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import android.view.Menu;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Inbox");

        ListView list = (ListView) findViewById(R.id.listEmail);

        LoadRegisterActivity();

        Email e = new Email(new Date(), "daniel", new String[0], "teste", "this is a test");

        ArrayList<InboxEmail> inbox = new ArrayList<>();
        inbox.add(new InboxEmail(e));
        ArrayAdapter adapter = new EmailAdapter(this, inbox);
        list.setAdapter(adapter);


        /*Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    postHTML();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();*/


    }

    void LoadRegisterActivity(){
        Boolean isFirstRun = getSharedPreferences(
                "shared preferences",
                MODE_PRIVATE
        ).getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(this, RegisterActivity.class));


            //getSharedPreferences("shared preferences", MODE_PRIVATE).edit()
            //        .putBoolean("isFirstRun", false).commit();
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

        }
        else if(item.getItemId() == R.id.sent_list){
            Intent intent = new Intent(this, SentActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.friends){
            Intent intent = new Intent(this, FriendsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void writeEmail(){

    }
    public void friends(){

    }
    public void update(){

    }

    //send email
    //retrieve emails
    //look for emails
    //look for friends
    //register me




}
