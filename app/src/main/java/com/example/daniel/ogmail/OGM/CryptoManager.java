package com.example.daniel.ogmail.OGM;

import java.util.Date;

public class CryptoManager {

    static char[] sigma = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' '};

    private static int indexOf(char c){
        for(int i = 0; i < sigma.length; i++) {
            if(c == sigma[i]) return i;
        }
        return -1;
    }

    public static String getID(String s){
        StringBuilder id = new StringBuilder("AAAAAAAAAA");
        int len = Math.max(10, s.length());
        for(int i = 0; i < len; i++){
            int pos1 = indexOf(s.charAt(i % s.length()));
            int pos2 = indexOf(id.charAt(i % 10));
            char c = sigma[ (pos1 + pos2) % sigma.length ];
            id.setCharAt(i % 10, c);
        }
        return id.toString();
    }

    public static String encrypt(String message, String senderEmail, String receiverEmail){
        String key = senderEmail + receiverEmail;
        int len = message.length(), keyLen = key.length();
        String cipher = "";
        int si, ki;
        for(int i = 0; i < len; i++){
            si = indexOf(message.charAt(i));
            ki = indexOf(key.charAt(i % keyLen));
            cipher += sigma[ (si + ki) % sigma.length ];
        }
        return cipher;
    }

    public static String decrypt(String cipher, String senderEmail, String receiverEmail){
        String key = senderEmail + receiverEmail;
        int len = cipher.length(), keyLen = key.length();
        String message = "";
        int ci, ki;
        for(int i = 0; i < len; i++){
            ci = indexOf(cipher.charAt(i));
            ki = indexOf(key.charAt(i % keyLen));
            message += sigma[ ((ci - ki) + sigma.length) % sigma.length ];
        }
        return message;
    }

    public static String encryptEmail(Email email, int destinyIndex){
        //subject, body
        String cipher = encrypt(email.subject, email.from, email.to[destinyIndex]) + "$" +
                        encrypt(email.body, email.from, email.to[destinyIndex]);
        return cipher;
    }

    public static Email decryptCipher(String cipher, String senderEmail, String receiverEmail){
        String subject, body;
        int firstIndex = cipher.indexOf("$");
        subject = decrypt(cipher.substring(0, firstIndex), senderEmail, receiverEmail);
        body = decrypt(cipher.substring(firstIndex + 1, cipher.length()), senderEmail, receiverEmail);
        return new Email(new Date(), senderEmail, new String[]{receiverEmail}, subject, body);
    }

}
