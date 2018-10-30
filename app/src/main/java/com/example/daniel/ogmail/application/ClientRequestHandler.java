package com.example.daniel.ogmail.application;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.EmailService;
import com.example.daniel.ogmail.OGM.Email;

public class ClientRequestHandler implements EmailService {

    private static ClientRequestHandler instance = null;

    public static ClientRequestHandler getInstance(){
        if(instance == null) instance = new ClientRequestHandler();
        return instance;
    }

    private ClientRequestHandler(){}

    @Override
    public void register(String myEmail, Callback callback) {

    }

    @Override
    public void sendEmail(Email email, Callback callback) {

    }

    @Override
    public Email[] getEmails(String myEmails, Callback callback) {
        return new Email[0];
    }

    @Override
    public void startTracking(String userEmail, Callback callback) {

    }

    @Override
    public void stopTracking(Callback callback) {

    }



    @Override
    public boolean searchUser(String userEmail, Callback callback) {
        return false;
    }


}
