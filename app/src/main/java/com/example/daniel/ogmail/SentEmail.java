package com.example.daniel.ogmail;

import com.example.daniel.ogmail.OGM.Email;

public class SentEmail extends Email {

    public boolean wasSent;

    public SentEmail(Email e){
        super(e.date, e.from, e.to, e.subject, e.body);
        wasSent = false;
    }

    public void getSendConfirmation(){
        wasSent = true;
    }
}
