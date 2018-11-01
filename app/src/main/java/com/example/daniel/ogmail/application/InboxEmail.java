package com.example.daniel.ogmail.application;

import com.example.daniel.ogmail.OGM.Email;

public class InboxEmail extends Email {
    public boolean wasRead;

    public InboxEmail(Email e){
        super(e.date, e.from, e.to, e.subject, e.body);
        wasRead = false;
    }

    public void read(){
        wasRead = true;
    }
}
