package com.example.moja.pfa;


/**
 * Created by Mathias on 09.05.2015.
 */
public class DataSetResult{
    String amount_expanses;
    String amount_earnings;
    String category;
    String date_from;
    String date_to;

    public DataSetResult(String amount_expanses, String amount_earnings, String category, String date_from, String date_to){
        this.amount_expanses=amount_expanses;
        this.amount_earnings=amount_earnings;
        this.category=category;
        this.date_from=date_from;
        this.date_to=date_to;
    }

    public DataSetResult(){}
    

}
