package com.example.moja.pfa;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mathias on 09.05.2015.
 */
public class DataBaseRequest  implements Parcelable{
    String date_from;
    String date_to;
    String category;

    public DataBaseRequest(String date_from, String date_to){
        this.date_from = date_from;
        this.date_to = date_to;
        this.category = "";
    }

    public DataBaseRequest(){}
    //for getting parcelable datatype
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(date_from);
        out.writeString(date_to);
        out.writeString(category);
    }

    public static final Parcelable.Creator<DataBaseRequest> CREATOR = new Parcelable.Creator<DataBaseRequest>() {
        public DataBaseRequest createFromParcel(Parcel in) {
            return new DataBaseRequest(in);
        }

        public DataBaseRequest[] newArray(int size) {
            return new DataBaseRequest[size];
        }
    };

    private DataBaseRequest(Parcel in) {
        this.date_from=in.readString();
        this.date_to=in.readString();
        this.category=in.readString();
    }
}
