package com.example.daniel.ogmail.OGM;

public enum Response {
    //register(String myEmail, Callback callback);
    REGISTER_SUCCESS, ALREADY_REGISTERED,
    //public void sendEmail(Email email, Callback callback);
    EMAIL_SENT, EMAIL_FAILED,
    //public Email[] getEmails(String myEmails, Callback callback);
    EMAIL_RECEIVED, NO_NEW_EMAIL,
    //public boolean searchUser(String userEmail, Callback callback);
    USER_FOUND, USER_NOT_FOUND,

    TIMEOUT
}
