package com.example.daniel.ogmail.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.OGM.Email;
import com.example.daniel.ogmail.OGM.OGM;
import com.example.daniel.ogmail.OGM.Response;
import com.example.daniel.ogmail.R;
import com.example.daniel.ogmail.application.MemoryManager;
import com.example.daniel.ogmail.application.ToastManager;

import java.util.Date;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        setTitle("New Email");

        Button button = findViewById(R.id.send);
        final Context context = this;

        final String from = MemoryManager.getInstance().getMyEmail(this);
        final EditText destiny = findViewById(R.id.emailDestiny);
        final EditText subject = findViewById(R.id.emailSubject);
        final EditText body = findViewById(R.id.emailBody);



        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String dest = destiny.getText().toString();
                int begin = 0, end;
                String[] to = new String[dest.length() - dest.replace(",", "").length() + 1];
                for(int i = 0; i < to.length; i++){
                    end = dest.indexOf(',', begin + 1);
                    if(end == -1) end = dest.length();
                    to[i] = dest.substring(begin, end);
                    begin = end + 1;
                }

                Email e = new Email(new Date(), from, to, subject.getText().toString(), body.getText().toString());
                OGM.getInstance().sendEmail(e, new Callback() {
                    @Override
                    public void execute(Response response) {
                        if(response == Response.EMAIL_FAILED){
                            ToastManager.show("Couldn\'t send email.", 0, (Activity) context);
                        }
                        else if(response == Response.EMAIL_SENT){
                            ToastManager.show("Email successfully sent.", 0, (Activity) context);
                        }
                        else if(response == Response.EMAIL_PARTIALLY_SENT){
                            ToastManager.show("Not all users received the email.", 0, (Activity) context);
                        }
                    }
                });
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ActionBar actionBar = this.getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //startActivity(new Intent(timer_2.this, timer_2_pause.class));
        //finish();
        System.out.println("VOLTOU");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
