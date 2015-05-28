package com.example.moja.pfa;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mathias on 25.05.2015.
 */
public class Utils {
    private static Utils ourInstance = new Utils();
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String CATEGORIES = "Categories";

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    public String processEnteredAmount(String inputString){

        char[] inputStringArray =  inputString.toCharArray();
        int pos = -1;
        for(int i = 0; i < inputStringArray.length; i++) {
            if(inputStringArray[i] == '.') {
                pos = i;
                break;
            }
        }

        char[] outputStringArray;
        if(pos == -1){
            outputStringArray = new char[inputString.length()+3];
            for(int j = 0; j < inputString.length(); ++j)
                outputStringArray[j]=inputStringArray[j];
            outputStringArray[inputString.length()+0]='.';
            outputStringArray[inputString.length()+1]='0';
            outputStringArray[inputString.length()+2]='0';
        }else {
            outputStringArray = new char[pos + 3];
            for(int j = 0; j < pos+1; ++j)
                outputStringArray[j]=inputStringArray[j];
            if(inputStringArray.length < pos+2)
                outputStringArray[pos+1]='0';
            else
                outputStringArray[pos+1]=inputStringArray[pos+1];
            if(inputStringArray.length < pos+3)
                outputStringArray[pos+2]='0';
            else
                outputStringArray[pos+2]=inputStringArray[pos+2];
        }

        String outputString=new String(outputStringArray);

        return outputString;
    }


    boolean amountInputValid(String amount){

        if(amount.length()==0)
            return false;

        char[] amountArray =  amount.toCharArray();

        if(amountArray[0]=='0'){
            if(amountArray.length<3){
                return false;
            }else {
                if (amountArray[1] != '.')
                    return false;
                if(amountArray.length == 3) {
                    if (amountArray[2] == '0')
                        return false;
                }else{
                    if((amountArray[2] == '0') && (amountArray[3] == '0') )
                        return false;
                }
            }
        }

        if(amountArray[0]=='.'){
            if(amountArray.length<2){
                return false;
            }else {
                if(amountArray.length == 2) {
                    if (amountArray[1] == '0')
                        return false;
                }else{
                    if((amountArray[1] == '0') && (amountArray[2] == '0') )
                        return false;
                }
            }
        }

        return true;
    }

    public ArrayList<String> createCategoryList(Context context, boolean addNew) {
        String[] stringArray = context.getResources().getStringArray(R.array.category_array);

        ArrayList<String> categoryList = new ArrayList<>();

        for(int iterator=0; iterator <stringArray.length; iterator++) {
            categoryList.add(stringArray[iterator]);
            if(iterator == 0) {
                SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
                String custom_categories = settings.getString(CATEGORIES, "");

                if(custom_categories != "") {
                    String[] array_custom_categories = custom_categories.split(";");
                    for(String cate : array_custom_categories) {
                        if(cate != "")
                            categoryList.add(cate);
                    }
                }
                if(addNew)
                    categoryList.add(context.getResources().getString(R.string.spinner_manage_category));
            }
        }

        return categoryList;
    }

    public ArrayList<String> createCustomerCategoryList(Context context) {

        ArrayList<String> categoryList = new ArrayList<>();

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String custom_categories = settings.getString(CATEGORIES, "");

        if(custom_categories != "") {
            String[] array_custom_categories = custom_categories.split(";");
            for(String cate : array_custom_categories) {
                if(cate != "")
                    categoryList.add(cate);
            }
        }


        return categoryList;
    }

    public void saveCustomCategory(Context context, String category) {

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String custom_categories = category + ";" + settings.getString(CATEGORIES, "");


        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CATEGORIES, custom_categories);

        editor.commit();
    }

    public void deleteCustomCategory(Context context, ArrayList<Integer> positionOfItemsToDelete) {

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String custom_categories_old = settings.getString(CATEGORIES, "");
        String custom_categories_new = "";

        String[] array_custom_categories = custom_categories_old.split(";");
        for(Integer iterator = 0; iterator < array_custom_categories.length; iterator++) {
            if(!positionOfItemsToDelete.contains(iterator))
                custom_categories_new += array_custom_categories[iterator] + ";";
        }


        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CATEGORIES, custom_categories_new);

        editor.commit();
    }


    public ArrayList<DataSetResult> getResultsFromData(Context context, ArrayList<DataSet> dataSetList, DataBaseRequest dataBaseRequest){
        ArrayList<DataSetResult> dataSetResultList = new ArrayList<DataSetResult>();

        ArrayList<String> stringArray = Utils.getInstance().createCategoryList(context, false);

        Integer stringArrayLength = stringArray.size();
        Double[] amountExpanses = new Double[stringArrayLength];
        Double[] amountEarnings = new Double[stringArrayLength];
        int iterator;
        for(iterator=0; iterator<stringArrayLength; iterator++){
            amountExpanses[iterator]=0.0;
            amountEarnings[iterator]=0.0;
        }
        int iteratorCategory;
        for(iterator=0; iterator<dataSetList.size(); iterator++){
            for(iteratorCategory=0; iteratorCategory < stringArrayLength; iteratorCategory++){
                if(stringArray.get(iteratorCategory).equals(dataSetList.get(iterator).category)){
                    if(dataSetList.get(iterator).expanse.toCharArray()[0] == 'T'){
                        amountExpanses[iteratorCategory]=amountExpanses[iteratorCategory]+ Double.valueOf(dataSetList.get(iterator).amount);
                    }else if(dataSetList.get(iterator).expanse.toCharArray()[0] == 'F'){
                        amountEarnings[iteratorCategory]=amountEarnings[iteratorCategory]+Double.valueOf(dataSetList.get(iterator).amount);
                    }else
                        assert(false);

                }
            }

        }

        Double overAllSumExpanses = 0.0;
        Double overAllSumEarnings = 0.0;
        for(iterator=0; iterator < stringArray.size(); iterator++){
            DataSetResult dataSetResult = new DataSetResult();
            dataSetResult.date_from=dataBaseRequest.date_from;
            dataSetResult.date_to=dataBaseRequest.date_to;
            dataSetResult.category=stringArray.get(iterator);
            overAllSumExpanses=overAllSumExpanses+amountExpanses[iterator];
            overAllSumEarnings=overAllSumEarnings+amountEarnings[iterator];
            dataSetResult.amount_earnings=String.valueOf(amountEarnings[iterator]);
            dataSetResult.amount_expanses=String.valueOf(amountExpanses[iterator]);
            dataSetResultList.add(dataSetResult);
        }

        DataSetResult dataSetResultOverallSum = new DataSetResult();
        dataSetResultOverallSum.date_from=dataBaseRequest.date_from;
        dataSetResultOverallSum.date_to=dataBaseRequest.date_to;
        dataSetResultOverallSum.category=context.getResources().getString(R.string.asr_string_overall_sum);
        dataSetResultOverallSum.amount_earnings=String.valueOf(overAllSumEarnings);
        dataSetResultOverallSum.amount_expanses=String.valueOf(overAllSumExpanses);
        dataSetResultList.add(dataSetResultOverallSum);

        return dataSetResultList;
    }

    public ArrayList<DataSetResult> transformDatasetToMonthlyDataset(Context context, DataBaseRequest dataBaseRequest) {

        ArrayList<DataSet> dataSetList = new ArrayList<DataSet>();
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        dataSetList = databaseInterface.getDataFromDataBaseRequest(dataBaseRequest);


        Date startingDate = new Date(dataBaseRequest.date_from);
        Date endDate = new Date(dataBaseRequest.date_to);

        ArrayList<DataSetResult> dataSetResultList = new ArrayList<DataSetResult>();

        String requestedCategory = dataBaseRequest.category;

        Integer noOfMonth = endDate.getNoOfMonthBetween(startingDate, endDate);
        Double[] amountExpanses = new Double[noOfMonth];
        Double[] amountEarnings = new Double[noOfMonth];
        int iterator;
        for(iterator=0; iterator<noOfMonth; iterator++){
            amountExpanses[iterator]=0.0;
            amountEarnings[iterator]=0.0;
        }
        Integer position=0;
        Date currentDate;
        for(iterator=0; iterator<dataSetList.size(); iterator++){
            if(requestedCategory.equals(dataSetList.get(iterator).category)){
                if(dataSetList.get(iterator).expanse.toCharArray()[0] == 'T'){
                    currentDate = new Date(dataSetList.get(iterator).date);
                    position = currentDate.noOfMonthInRange(startingDate);
                    amountExpanses[position]=amountExpanses[position]+ Double.valueOf(dataSetList.get(iterator).amount);
                }else if(dataSetList.get(iterator).expanse.toCharArray()[0] == 'F'){
                    currentDate = new Date(dataSetList.get(iterator).date);
                    position = currentDate.noOfMonthInRange(startingDate);
                    amountEarnings[position]=amountEarnings[position]+ Double.valueOf(dataSetList.get(iterator).amount);
                }else
                    assert(false);
            }
        }

        Double overAllSumExpanses = 0.0;
        Double overAllSumEarnings = 0.0;
        for(iterator=0; iterator < noOfMonth; iterator++){
            DataSetResult dataSetResult = new DataSetResult();
            dataSetResult.date_from = startingDate.addMonth(iterator);
            dataSetResult.date_to = startingDate.addMonth(iterator);
            dataSetResult.category=requestedCategory;
            overAllSumExpanses=overAllSumExpanses+amountExpanses[iterator];
            overAllSumEarnings=overAllSumEarnings+amountEarnings[iterator];
            dataSetResult.amount_earnings=String.valueOf(amountEarnings[iterator]);
            dataSetResult.amount_expanses=String.valueOf(amountExpanses[iterator]);
            dataSetResultList.add(dataSetResult);
        }

        DataSetResult dataSetResultOverallSum = new DataSetResult();
        dataSetResultOverallSum.date_from=startingDate.addMonth(0);
        dataSetResultOverallSum.date_to=endDate.addMonth(0);
        dataSetResultOverallSum.category=requestedCategory;
        dataSetResultOverallSum.amount_earnings=String.valueOf(overAllSumEarnings);
        dataSetResultOverallSum.amount_expanses=String.valueOf(overAllSumExpanses);
        dataSetResultList.add(dataSetResultOverallSum);

        return dataSetResultList;

    }

    public String getDateFromNMonthAgo(int i) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1-i;
        int year = c.get(Calendar.YEAR);
        while(month<1){
            month=month+12;
            year--;
        }
        int day = c.get(Calendar.DAY_OF_MONTH);
        Date today = new Date(day, month, year);

        return today.toString();
    }

}
