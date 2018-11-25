package com.example.daniel.ogmail.OGM;

import com.example.daniel.ogmail.Activities.MainActivity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Email implements Serializable {

    public Date date;
    public String to;
    public String from;
    public String subject;
    public String body;

    public Email(Date date, String from, String to, String subject, String body){
        this.date = date;
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.body = body;
    }

    public Email(String email){
        int first = email.indexOf('$');
        int second = email.indexOf('$', first + 1);
        this.to = MainActivity.myEmail;
        this.from = email.substring(0, first);
        this.subject = email.substring(first + 1, second);
        this.body = email.substring(second + 1, email.length());
        this.date = new Date();
    }

    public String toString(){
        //to $ from $ subject $ body
        return to + "$" + from + "$" + subject + "$" + body;
    }
}

