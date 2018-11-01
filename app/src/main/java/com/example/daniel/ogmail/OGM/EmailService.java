package com.example.daniel.ogmail.OGM;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.OGM.Email;

public interface EmailService {

    public void register(String myEmail, Callback callback);
    public void sendEmail(Email email, Callback callback);
    public Email[] getEmails(String myEmail, Callback callback);
    public void startTracking(String userEmail, Callback callback);
    public void stopTracking(Callback callback);
    public boolean searchUser(String userEmail, Callback callback);


    //getEmails retorna os emails que tem no servidor
    //startTracking dispara o callback sempre que ha alguma mudanca
    //cancelEmail erases the last email sent to user
}


