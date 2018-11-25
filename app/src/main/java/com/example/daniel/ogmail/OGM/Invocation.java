package com.example.daniel.ogmail.OGM;

import java.io.Serializable;
import java.util.ArrayList;

public class Invocation implements Serializable {
    int serviceID;
    String operationName;
    ArrayList<String> params;
    String ip;
    int port;

    public Invocation(){this.serviceID = -1;}
    public Invocation(int serviceID){this.serviceID = serviceID;}

    public int getServiceID() { return this.serviceID; }
    public void setServiceID(int serviceID) { this.serviceID = serviceID; }

    public ArrayList<String> getParams() { return this.params; }
    public void setParams(ArrayList<String> p){
        params = p;
    }

    public String getIp() { return this.ip; }
    public void setIp(String ip){
        this.ip = ip;
    }

    public int getPort() { return this.port; }
    public void setPort(int port){
        this.port = port;
    }

    public String getOperationName() { return this.operationName; }
    public void setOperationName(String s){
        operationName = s;
    }

}
