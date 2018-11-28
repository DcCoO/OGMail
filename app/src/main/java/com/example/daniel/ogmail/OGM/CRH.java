package com.example.daniel.ogmail.OGM;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CRH {

    Socket client;

    public CRH(String ip, int port) {
        try {
            client = new Socket("192.168.0.19", 5000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send(byte[] data) throws IOException {
        DataOutputStream dOut = new DataOutputStream(client.getOutputStream());

        dOut.writeInt(data.length); // write length of the message
        dOut.write(data);
    }

    public byte[] receive() throws IOException {
        DataInputStream dIn = new DataInputStream(client.getInputStream());

        int length = dIn.readInt();                    // read length of incoming message
        if(length > 0) {
            byte[] message = new byte[length];
            dIn.readFully(message,0, message.length); // read the message
            return message;
        }
        return new byte[0];
    }


}
