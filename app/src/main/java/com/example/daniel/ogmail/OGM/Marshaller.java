package com.example.daniel.ogmail.OGM;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Marshaller {

    public byte[] marshall(Invocation inv){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(inv);
            return bos.toByteArray();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new byte[0];

        //return SerializationUtils.serialize(inv);
    }

    public byte[] marshallAnswer(String s){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(s);
            return bos.toByteArray();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new byte[0];
        //return SerializationUtils.serialize(s);
    }

    public Invocation unmarshall(byte[] data){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            return (Invocation) in.readObject();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new Invocation();
        //return (Invocation) SerializationUtils.deserialize(data);
    }

    public String unmarshallAnswer(byte[] data){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            return (String) in.readObject();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
        //return (String) SerializationUtils.deserialize(data);
    }
}
