package com.example.daniel.ogmail.OGM;

import com.example.daniel.ogmail.Callback;

public class CRH {

    private CRH(){}


    public static void register(String myEmail, Callback callback) {
        OGM.getInstance().register(myEmail, callback);
    }


    public static void sendEmail(Email email, Callback callback) {
        OGM.getInstance().sendEmail(email, callback);
    }


    public static Email[] getEmails(String myEmails, Callback callback) {
        return OGM.getInstance().getEmails(myEmails, callback);
    }


    public static void startTracking(String userEmail, Callback callback) {
        OGM.getInstance().startTracking(userEmail, callback);
    }


    public static void stopTracking(Callback callback) {
        OGM.getInstance().stopTracking(callback);
    }


    public static boolean searchUser(String userEmail, Callback callback) {
        return OGM.getInstance().searchUser(userEmail, callback);
    }

    public static boolean isTracking(){
        return OGM.getInstance().isTracking();
    }

    public static void clearInbox(String userEmail, Callback callback){
        try {
            OGM.getInstance().clearInbox(userEmail, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
