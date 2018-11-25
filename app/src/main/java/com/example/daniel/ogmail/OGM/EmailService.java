package com.example.daniel.ogmail.OGM;

import com.example.daniel.ogmail.Callback;

public interface EmailService {

    public String register(String myEmail, Callback callback);
    public String sendEmail(Email email, Callback callback);
    public String getEmails(String myEmail, Callback callback);
    public String searchUser(String userEmail, Callback callback);


    //getEmails retorna os emails que tem no servidor
    //startTracking dispara o callback sempre que ha alguma mudanca
    //cancelEmail erases the last email sent to user
}


