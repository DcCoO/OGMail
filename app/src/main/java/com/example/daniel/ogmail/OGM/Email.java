package com.example.daniel.ogmail.OGM;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {

    public Date date;
    public String[] to;
    public String from;
    public String subject;
    public String body;

    public Email(Date date, String from, String[] to, String subject, String body){
        this.date = date;
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.body = body;
    }

    public String toString(){
        return "From: " + from + "\nSubject: " + subject + "\nBody: " + body;
    }


}
