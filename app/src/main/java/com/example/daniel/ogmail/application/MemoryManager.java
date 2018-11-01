package com.example.daniel.ogmail.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daniel.ogmail.InboxEmail;
import com.example.daniel.ogmail.SentEmail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MemoryManager {

    private static MemoryManager instance = null;

    public static MemoryManager getInstance(){
        if(instance == null) instance = new MemoryManager();
        return instance;
    }

    private MemoryManager(){}

    public enum SaveType{
        SENT, INBOX
    }

    public void save(Context context, Object element, SaveType type){
        switch (type){
            case INBOX: SaveInboxEmail(context, (InboxEmail) element); break;
            case SENT: SaveSentEmail(context, (SentEmail) element); break;
        }
    }

    public void SaveMyEmail(Context context, String myEmail){
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("my_email", myEmail);
        editor.apply();
    }

    public String getMyEmail(Context context){
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sp.getString("my_email", null);
    }

    private void SaveInboxEmail(Context context, InboxEmail email){
        ArrayList<InboxEmail> emails = loadInboxEmails(context);
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        emails.add(email);
        String json = gson.toJson(emails);
        editor.putString("inbox", json);
        editor.apply();
    }

    private void SaveSentEmail(Context context, SentEmail email){
        ArrayList<SentEmail> emails = loadSentEmails(context);
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        emails.add(email);
        String json = gson.toJson(emails);
        editor.putString("sent", json);
        editor.apply();
    }

    public ArrayList<InboxEmail> loadInboxEmails(Context context){
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("inbox", null);
        Type type = new TypeToken<ArrayList<InboxEmail>>(){}.getType();
        ArrayList<InboxEmail> emails = gson.fromJson(json, type);
        if(emails == null) emails = new ArrayList<InboxEmail>();
        return emails;
    }

    public ArrayList<SentEmail> loadSentEmails(Context context){
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("sent", null);
        Type type = new TypeToken<ArrayList<SentEmail>>(){}.getType();
        ArrayList<SentEmail> emails = gson.fromJson(json, type);
        if(emails == null) emails = new ArrayList<SentEmail>();
        return emails;
    }

    public void clearMemory(Context context){
        ArrayList<InboxEmail> emails = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(emails);
        editor.putString("inbox", json);
        editor.apply();
    }

}
