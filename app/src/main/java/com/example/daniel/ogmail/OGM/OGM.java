package com.example.daniel.ogmail.OGM;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.EmailService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    for(int i = 0; i < email.to.length; i++){
                        String cipher = CryptoManager.encryptEmail(email, i);
                        writeEmail(email.from, email.to[i], cipher);
                    }
                    callback.execute(Response.EMAIL_SENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public Email[] getEmails(final String myEmail, final Callback callback) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String emails = getEmails(myEmail);
                    int emailNumber = emails.length() - emails.replace(":", "").length();
                    int mark = 0, markEnd = 0;

                    String subject, body;

                    for(int i = 0; i < emailNumber; i++){
                        mark = markPosition(emails, mark);
                        markEnd = markPosition(emails, mark + 1);

                        subject = emails.substring(mark, markEnd);

                        mark = markEnd + 1;

                        mark = markPosition(emails, mark);
                        markEnd = markPosition(emails, mark + 1);

                        body = emails.substring(mark, markEnd);

                        mark = markEnd + 1;
                        //Email email = CryptoManager.decryptCipher()

                    }
                    callback.execute(Response.EMAIL_RECEIVED);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        return new Email[0];
    }

    @Override
    public void startTracking(final String userEmail, final Callback callback) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    System.out.println("ENTROU NO LOOP: " + userEmail);
                    tracking = true;
                    while(tracking){
                        if(hasNewEmails(userEmail)){
                            callback.execute(Response.EMAIL_RECEIVED);
                            //clearInbox(userEmail);
                        }
                    }
                    System.out.println("SAIU DO LOOP");

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
        return false;
    }

    private int markPosition(String s, int begin){
        for(int i = begin; i < s.length(); i++)
            if(s.charAt(i) == '\"') return i;
        return -1;
    }


    private static String getUser(String userEmail) {
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

    private void clearInbox(String userEmail) throws Exception {
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
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

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
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

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
