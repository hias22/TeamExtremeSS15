package com.example.moja.pfa;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mathias on 09.05.2015.
 */
public class DataSet implements Parcelable {
    Integer dataBaseId;
    String amount;
    String description;
    String category;
    String date;
    String expanse;

    public DataSet(String amount, String description, String category, String date, String expanse){
        this.amount=amount;
        this.description=description;
        this.category=category;
        this.date=date;
        this.expanse=expanse;
        dataBaseId = -1;
    }


    //for getting parcelable datatype
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(dataBaseId);
        out.writeString(amount);
        out.writeString(description);
        out.writeString(category);
        out.writeString(date);
        out.writeString(expanse);
    }

    public static final Parcelable.Creator<DataSet> CREATOR = new Parcelable.Creator<DataSet>() {
        public DataSet createFromParcel(Parcel in) {
            return new DataSet(in);
        }

        public DataSet[] newArray(int size) {
            return new DataSet[size];
        }
    };

    private DataSet(Parcel in) {
        this.dataBaseId = in.readInt();
        this.amount=in.readString();
        this.description=in.readString();
        this.category=in.readString();
        this.date=in.readString();
        this.expanse=in.readString();
    }
}
