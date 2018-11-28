package com.example.daniel.ogmail.OGM;

public class Requestor {


    public Termination invoke(Invocation inv){
        try {

            CRH crh = new CRH(inv.ip, inv.port);
            Marshaller marshaller = new Marshaller();
            Termination term = new Termination();

            byte[] msgMarshalled, msgToBeUnmarshalled;
            String responseMsg;

            msgMarshalled = marshaller.marshall(inv);

            crh.send(msgMarshalled);

            msgToBeUnmarshalled = crh.receive();
            responseMsg = marshaller.unmarshallAnswer(msgToBeUnmarshalled);

            System.out.println("CHEGOU DO SERVER: " + responseMsg);
            term.setResult(responseMsg);
            return term;
        }
        catch(Exception e){
            return new Termination();
        }
    }

}
