package com.example.moja.pfa;

/**
 * Created by Mathias on 09.05.2015.
 */
public class DataSet {
    double amount;
    String description;
    String category;
    String date;
    boolean expanse;

    public DataSet(double amount, String description, String category, String date, boolean expanse){
        this.amount=amount;
        this.description=description;
        this.category=category;
        this.date=date;
        this.expanse=expanse;
    }
}
