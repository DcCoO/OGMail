package com.example.daniel.ogmail.OGM;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.EmailService;
import com.example.daniel.ogmail.application.MemoryManager;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class OGM implements EmailService {

    private static OGM instance = null;

    private static volatile boolean tracking = false;

    public static OGM getInstance(){
        if(instance == null) instance = new OGM();
        return instance;
    }

    private OGM(){}

    @Override
    public void register(final String myEmail, final Callback callback) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String response = null;
                    for(int i = 0; i < 5; i++){
                        response = getUser(myEmail);
                        if(response.equals("timeout")){
                            callback.execute(Response.TIMEOUT);
                            return;
                        }
                        else if(!response.equals("null")) break;
                    }

                    if(!response.equals("null")) {
                        callback.execute(Response.ALREADY_REGISTERED);
                    }
                    else {
                        for(int i = 0; i < 5; i++){
                            response = registerUser(myEmail);
                            if(!response.equals("null")) break;
                        }
                        if(response.equals("null")) callback.execute(Response.TIMEOUT);
                        else callback.execute(Response.REGISTER_SUCCESS);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void sendEmail(final Email email, final Callback callback) {

        for(int i = 0; i < email.to.length; i++){
            System.out.println("dest: " + email.to[i]);
        }

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean hasOneUser = false;
                boolean hasntOneUser = false;
                try  {
                    for(int i = 0; i < email.to.length; i++){
                        boolean userExists = searchUser(email.to[i], null);
                        hasOneUser = hasOneUser || userExists;
                        hasntOneUser = hasntOneUser || !userExists;

                        if(!userExists) continue;

                        String cipher = CryptoManager.encryptEmail(email, i);
                        writeEmail(email.from, email.to[i], cipher);
                    }
                    if(!hasntOneUser) callback.execute(Response.EMAIL_SENT);
                    else if(!hasOneUser) callback.execute(Response.EMAIL_FAILED);
                    else callback.execute(Response.EMAIL_PARTIALLY_SENT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public Email[] getEmails(final String myEmail, final Callback callback) {
        //System.out.println("getEmails: Rodando");
        final Email[] emails = new Email[100];

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    String emailsJson = getEmails(myEmail);
                    System.out.println("getEmails: " + emailsJson);
                    int emailNumber = emailsJson.length() - emailsJson.replace(":", "").length() - 1;
                    int idBegin = 0, bracketBegin = 0;
                    int fieldBegin = 0, fieldEnd = 0;

                    String sender, body, cipher;

                    for(int i = 0; i < emailNumber; i++){
                        idBegin = emailsJson.indexOf('-', idBegin);
                        if(idBegin == -1) break;
                        bracketBegin = getIndex(emailsJson, '{', idBegin + 1);

                        //finding sender
                        fieldBegin = getIndex(emailsJson, '\"', bracketBegin + 1);
                        fieldEnd = getIndex(emailsJson, '\"', fieldBegin + 1);

                        sender = emailsJson.substring(fieldBegin + 1, fieldEnd);

                        //finding body
                        fieldBegin = getIndex(emailsJson, '\"', fieldEnd + 1);
                        fieldEnd = getIndex(emailsJson, '\"', fieldBegin + 1);

                        cipher = emailsJson.substring(fieldBegin + 1, fieldEnd);

                        Email email = CryptoManager.decryptCipher(cipher, sender, myEmail);
                        emails[i] = email;

                        //System.out.println("getEmails: " + email);

                        idBegin++;
                    }

                    callback.execute(Response.EMAIL_RECEIVED);


                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 100; i++)
            if(emails[i] == null) return Arrays.copyOfRange(emails, 0, i);
        return null;
    }

    @Override
    public void startTracking(final String userEmail, final Callback callback) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    System.out.println("startTracking: TRACKEANDO EMAIL " + userEmail);
                    tracking = true;
                    while(tracking){
                        if(hasNewEmails(userEmail)){
                            callback.execute(Response.EMAIL_RECEIVED);
                            System.out.println("startTracking: NEW EMAILS FOUND");
                            tracking = false;
                        }
                    }
                    System.out.println("startTracking: PARANDO DE TRACKEAR " + userEmail);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void stopTracking(Callback callback){
        tracking = false;
    }

    @Override
    public boolean searchUser(String userEmail, Callback callback) {
        return !getUser(userEmail).equals("null");
    }

    private int getIndex(String s, char c, int begin){
        for(int i = begin; i < s.length(); i++)
            if(s.charAt(i) == c) return i;
        return -1;
    }


    private String getUser(String userEmail) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL("https://o-e4bc8.firebaseio.com/users/" + userEmail + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        }
        catch (Exception e){
            return "timeout";
        }
    }

    private static String registerUser(String userEmail) throws Exception {
        String url = "https://o-e4bc8.firebaseio.com/users/" + userEmail + "/inbox.json";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "{ \"placeholder\" : \"" + userEmail + "\"}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();
    }

    private int getEmailCount(String userEmail) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://o-e4bc8.firebaseio.com/users/" + userEmail + "/inbox.json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.length() - result.toString().replace(":", "").length();
    }

    private boolean hasNewEmails(String userEmail) throws Exception {
        return getEmailCount(userEmail) > 1;
    }

    public void clearInbox(String userEmail, Callback callback) throws Exception {
        //if(0 == 0)return;
        System.out.println("RODANDO clearInbox");
        String url = "https://o-e4bc8.firebaseio.com/users/" + userEmail + "/inbox.json";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "{ \"placeholder\" : \""+ userEmail + "\"}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        //System.out.println(response.toString());
        //callback.execute(null);
    }

    private String getEmails(String userEmail) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://o-e4bc8.firebaseio.com/users/" + userEmail + ".json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public void writeEmail(String senderEmail, String destinyEmail, String ciphertext) throws Exception {
        System.out.println(senderEmail + ", " + destinyEmail + ", " + ciphertext);
        String url = "https://o-e4bc8.firebaseio.com/users/" + destinyEmail + "/inbox.json";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "{ \"" + senderEmail + "\" : \"" + ciphertext + "\"}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }
}
