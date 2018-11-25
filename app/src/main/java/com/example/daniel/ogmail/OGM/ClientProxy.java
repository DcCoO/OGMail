package com.example.daniel.ogmail.OGM;

import com.example.daniel.ogmail.Callback;
import com.example.daniel.ogmail.application.Internet;

import java.io.Serializable;

public class ClientProxy implements Serializable {
    protected int serviceID;
    protected int port;
    protected String ip;

    public ClientProxy(int id){
        serviceID = id;
        this.port = Internet.port;
        this.ip = Internet.ip;
    }
}
