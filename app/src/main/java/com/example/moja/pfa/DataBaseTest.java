package com.example.moja.pfa;

import android.util.Log;


public class DataBaseTest{
    private static final String TAG = "DataBaseTest";

    DatabaseInterface databaseInterface;

    public DataBaseTest(DatabaseInterface setDatabaseInterface) {
        databaseInterface = setDatabaseInterface;
    }


     public void testAll(){
    }

    public void testAddingDBEntryWithCount(){
        DataSet testDataSet = new DataSet("34.5",TAG,"Beer","date","T");
        int oldNumberOfElementsInDB = databaseInterface.getDataCount();
        databaseInterface.insertDataSet(testDataSet);
        int newNumberOfElementsInDB = databaseInterface.getDataCount();
        if(oldNumberOfElementsInDB+1 == newNumberOfElementsInDB) {
            Log.d(TAG, "testAddingDBEntryWithCount: OK");
        }
        else {
            Log.d(TAG, "testAddingDBEntryWithCount: FAIL");
            assert (false);
        }
    }

}