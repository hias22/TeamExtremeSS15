package com.example.moja.pfa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Mathias on 09.05.2015.
 */
public final class DatabaseInterface extends SQLiteOpenHelper {

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
    private static final String TEXT_REAL = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DatabaseEntry.TABLE_NAME + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_NAME_AMOUNT + TEXT_REAL + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_TYPE + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pfa.db";


    public DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        sql_db=db;
    }

    public void insertDataSet(DataSet ds) {
         String SQL_INSERT ="INSERT INTO " + DatabaseEntry.TABLE_NAME + " VALUES (1," +
                ds.amount + COMMA_SEP +
                ds.category + COMMA_SEP +
                ds.date + COMMA_SEP +
                ds.description + COMMA_SEP +
                ds.expanse +
        ");";
        sql_db.execSQL(SQL_INSERT);
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




}
