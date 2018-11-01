package com.example.daniel.ogmail.OGM;

import java.util.Comparator;

public class EmailComparator implements Comparator<Email> {
    @Override
    public int compare(Email e1, Email e2){
        return e2.date.compareTo(e1.date);
    }
}
