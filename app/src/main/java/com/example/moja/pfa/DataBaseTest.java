package com.example.moja.pfa;


import android.util.Log;




/**
 * Created by Mathias on 02.05.2015.
 */
public class DataBaseTest{
    private static final String TAG = "DataBaseTest";

    DatabaseInterface databaseInterface;

    public DataBaseTest(DatabaseInterface setDatabaseInterface) {
        databaseInterface = setDatabaseInterface;
    }


     public void testAll(){
       //  testAddingDBEntryWithCount();
    }

    public void testAddingDBEntryWithCount(){
        DataSet testDataSet = new DataSet("34.5",TAG,"Bier","date","T");
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