package com.example.moja.pfa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by Mathias on 09.05.2015.
 */
public final class DatabaseInterface extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseInterface";

    SQLiteDatabase sql_db;

    /* Inner class that defines the table contents */
    public static abstract class DatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "datasets";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TYPE = "type";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DatabaseEntry.TABLE_NAME + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_NAME_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_TYPE + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "pfa.db";


    public DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(SQL_CREATE_TABLE);
        sql_db=db;
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

// CRUD Operations (Create, Read, Update and Delete)

    public void insertDataSet(DataSet dataSet) {
        Log.d(TAG, "insertDataSet");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Date date = new Date(dataSet.date);
        values.put(DatabaseEntry.COLUMN_NAME_AMOUNT, dataSet.amount);
        values.put(DatabaseEntry.COLUMN_NAME_CATEGORY, dataSet.category);
        values.put(DatabaseEntry.COLUMN_NAME_DATE, date.toString());
        values.put(DatabaseEntry.COLUMN_NAME_DESCRIPTION, dataSet.description);
        values.put(DatabaseEntry.COLUMN_NAME_TYPE, dataSet.expanse);

        db.insert(DatabaseEntry.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<DataSet> getAllDataSets() {
        ArrayList<DataSet> dataSetList = new ArrayList<DataSet>();
        String selectAll = "SELECT  * FROM " + DatabaseEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                String amount = cursor.getString(1);
                String description = cursor.getString(4);
                String category = cursor.getString(2);
                String date = cursor.getString(3);
                String expanse = cursor.getString(5);
                DataSet dataSet = new DataSet(amount, description, category, date, expanse);
                dataSet.dataBaseId = Integer.parseInt(cursor.getString(0));
                dataSetList.add(dataSet);
            } while (cursor.moveToNext());
        }

        return reverse(dataSetList);
    }

    public ArrayList<DataSet> getDataFromDataBaseRequest(DataBaseRequest dataBaseRequest) {
        ArrayList<DataSet> dataSetList = new ArrayList<DataSet>();
        String selectAll = "SELECT  * FROM " + DatabaseEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);

        Date dateFrom = new Date(dataBaseRequest.date_from);
        Date dateTo = new Date(dataBaseRequest.date_to);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(3);
                Date dateOfEntry = new Date(date);
                if(dateOfEntry.isDateInRange(dateFrom, dateTo)) {
                    String amount = cursor.getString(1);
                    String description = cursor.getString(4);
                    String category = cursor.getString(2);
                    String expanse = cursor.getString(5);
                    DataSet dataSet = new DataSet(amount, description, category, date, expanse);
                    dataSet.dataBaseId = Integer.parseInt(cursor.getString(0));
                    dataSetList.add(dataSet);
                }
            } while (cursor.moveToNext());
        }

        return reverse(dataSetList);
    }

    public ArrayList<DataSet> reverse(ArrayList<DataSet> oldDataSetList) {
        ArrayList<DataSet> newDataSetList = new ArrayList<DataSet>();

        for (int i = oldDataSetList.size()-1; i > 0; --i) {
            newDataSetList.add(oldDataSetList.get(i));
        }

        return newDataSetList;
    }


    public int getDataCount() {
        Log.d(TAG, "getDataCount");
        String countQuery = "SELECT  * FROM " + DatabaseEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void deleteDatabaseEntries(){
        sql_db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sql_db);
    }

    public int updateDataSet(DataSet dataSet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseEntry.COLUMN_NAME_AMOUNT, dataSet.amount);
        values.put(DatabaseEntry.COLUMN_NAME_CATEGORY, dataSet.category);
        values.put(DatabaseEntry.COLUMN_NAME_DATE, dataSet.date);
        values.put(DatabaseEntry.COLUMN_NAME_DESCRIPTION, dataSet.description);
        values.put(DatabaseEntry.COLUMN_NAME_TYPE, dataSet.expanse);

        return db.update(DatabaseEntry.TABLE_NAME, values, DatabaseEntry._ID + "=" + String.valueOf(dataSet.dataBaseId), null);
    }

    public void deleteDataSet(DataSet dataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseEntry.TABLE_NAME, DatabaseEntry._ID + " = ?",
                new String[]{String.valueOf(dataSet.dataBaseId)});
        db.close();
    }

}
