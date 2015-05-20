package com.example.moja.pfa;

/**
 * Created by Mathias on 09.05.2015.
 */
public class DataBaseRequest {
    String date_from;
    String date_to;


    public DataBaseRequest(String date_from, String date_to){
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public DataBaseRequest(){}

}
