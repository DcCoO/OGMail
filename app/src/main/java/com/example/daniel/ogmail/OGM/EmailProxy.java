package com.example.daniel.ogmail.OGM;

import android.os.Debug;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.application.InboxEmail;
import com.example.daniel.ogmail.application.MemoryManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailProxy extends ClientProxy implements EmailService {
    public static final int serviceID = 1;
    private static EmailProxy instance = null;

    public static EmailProxy getInstance(){
        if(instance == null) instance = new EmailProxy();
        return instance;
    }

    public EmailProxy(){
        super(serviceID);

    }

    @Override
    public String register(String myEmail, Callback callback) {
        Invocation inv = new Invocation(serviceID);
        Termination ter = new Termination();
        ArrayList<String> params = new ArrayList<>();
        String methodName = "register";
        Requestor requestor = new Requestor();
        params.add(myEmail);

        inv.setIp(this.ip);
        inv.setPort(this.port);
        inv.setOperationName(methodName);
        inv.setParams(params);

        ter = requestor.invoke(inv);
        return ter.getResult();
    }

    @Override
    public String sendEmail(Email email, Callback callback) {

        Invocation inv = new Invocation(serviceID);
        Termination ter = new Termination();
        ArrayList<String> params = new ArrayList<>();
        String methodName = "sendEmail";
        Requestor requestor = new Requestor();
        //params.add(CryptoManager.encrypt(email.toString(), email.from, email.to));
        //String emailString = email.toString();
        String emailString = email.to + "$" + email.from + "$" +
            CryptoManager.encrypt(email.subject, email.from, email.to) + "$" +
            CryptoManager.encrypt(email.body, email.from, email.to);

        System.out.println("RESULTADO = " + emailString);
        params.add(emailString);

        inv.setIp(this.ip);
        inv.setPort(this.port);
        inv.setOperationName(methodName);
        inv.setParams(params);

        ter = requestor.invoke(inv);
        System.out.println("[<o>] EmailProxy retorna " + ter.getResult());
        return ter.getResult();
    }

    @Override
    public String getEmails(String myEmail, Callback callback) {
        Invocation inv = new Invocation(serviceID);
        Termination ter = new Termination();
        ArrayList<String> params = new ArrayList<>();
        String methodName = "getEmails";
        Requestor requestor = new Requestor();
        params.add(myEmail);

        inv.setIp(this.ip);
        inv.setPort(this.port);
        inv.setOperationName(methodName);
        inv.setParams(params);

        ter = requestor.invoke(inv);
        String emails = ter.getResult();

        String pattern = "[^\"$]+\\$[^\"$]+\\$[^\"$]+\\$[^\"$]+";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(emails);

        String result = "";
        while (m.find()) {
            String match = m.group(0);
            Email email = new Email(match);
            email.subject = CryptoManager.decrypt(email.subject, email.from, email.to);
            email.body = CryptoManager.decrypt(email.body, email.from, email.to);
            result += email.toString() + "-";
        }
        ter.setResult(result);
        System.out.println(ter.getResult());
        return ter.getResult();
    }



    @Override
    public String searchUser(String userEmail, Callback callback) {
        Invocation inv = new Invocation(serviceID);
        Termination ter = new Termination();
        ArrayList<String> params = new ArrayList<>();
        String methodName = "searchUser";
        Requestor requestor = new Requestor();
        params.add(userEmail);

        inv.setIp(this.ip);
        inv.setPort(this.port);
        inv.setOperationName(methodName);
        inv.setParams(params);

        ter = requestor.invoke(inv);
        return ter.getResult();
    }
}
