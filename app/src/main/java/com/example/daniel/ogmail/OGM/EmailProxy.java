package com.example.daniel.ogmail.OGM;

import android.os.Debug;

import com.example.daniel.ogmail.Callback;

import java.util.ArrayList;


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
        params.add(email.toString());

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
