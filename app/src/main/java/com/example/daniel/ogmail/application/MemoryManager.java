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

    private MemoryManager instance = null;

    public MemoryManager getInstance(){
        if(instance == null) instance = new MemoryManager();
        return instance;
    }

    private MemoryManager(){}

    public enum SaveType{
        SENT, INBOX, FRIEND
    }

    public void save(Context context, Object element, SaveType type){
        switch (type){
            case INBOX: SaveInboxEmail(context, (InboxEmail) element); break;
            case SENT: SaveSentEmail(context, (SentEmail) element); break;
            case FRIEND: SaveFriend(context, (String) element); break;
        }
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

    private void SaveFriend(Context context, String friend){
        ArrayList<String> friends = loadFriends(context);
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        friends.add(friend);
        String json = gson.toJson(friends);
        editor.putString("friends", json);
        editor.apply();
    }

    private ArrayList<InboxEmail> loadInboxEmails(Context context){
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("inbox", null);
        Type type = new TypeToken<ArrayList<InboxEmail>>(){}.getType();
        ArrayList<InboxEmail> emails = gson.fromJson(json, type);
        if(emails == null) emails = new ArrayList<InboxEmail>();
        return emails;
    }

    private ArrayList<SentEmail> loadSentEmails(Context context){
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("sent", null);
        Type type = new TypeToken<ArrayList<SentEmail>>(){}.getType();
        ArrayList<SentEmail> emails = gson.fromJson(json, type);
        if(emails == null) emails = new ArrayList<SentEmail>();
        return emails;
    }

    private ArrayList<String> loadFriends(Context context){
        SharedPreferences sp = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("friends", null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> friends = gson.fromJson(json, type);
        if(friends == null) friends = new ArrayList<String>();
        return friends;
    }

}
