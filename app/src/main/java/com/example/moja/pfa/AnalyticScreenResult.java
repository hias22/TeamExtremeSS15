package com.example.moja.pfa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mathias on 09.05.2015.
 */
public class AnalyticScreenResult extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytic_screen_result);

        setTitle(R.string.asr_title);

        Intent intent = getIntent();
        DataBaseRequest dataBaseRequest = intent.getParcelableExtra("dataBaseRequest");

        ArrayList<DataSet> dataSetList = new ArrayList<DataSet>();
        DatabaseInterface databaseInterface = new DatabaseInterface(this);
        dataSetList = databaseInterface.getDataFromDataBaseRequest(dataBaseRequest);

        ArrayList<DataSetResult> dataSetResultList = new ArrayList<DataSetResult>();
        dataSetResultList = getResultsFromData(dataSetList, dataBaseRequest);

        //listview:

        final ListView listview = (ListView) findViewById(R.id.analytic_screen_result_list_view);


        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, dataSetResultList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object listViewObject = listview.getItemAtPosition(position);
                reactOnListViewItemSelected((DataSetResult) listViewObject);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analytic_screen_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_entering) {
            openEnteringScreen();
        }
        if (id == R.id.action_analytic) {
            openAnalyticScreenSearch();
        }
        if (id == R.id.action_overview) {
            openOverviewScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openEnteringScreen() {
        Intent intent = new Intent(this, EnteringScreen.class);
        startActivity(intent);
    }

    public void openOverviewScreen() {
        Intent intent = new Intent(this, DataOverviewScreen.class);
        startActivity(intent);
    }

    public void openAnalyticScreenSearch() {
        Intent intent = new Intent(this, AnalyticScreenSearch.class);
        startActivity(intent);
    }

    public void reactOnListViewItemSelected(DataSetResult dataSetResult) {
        Toast.makeText(AnalyticScreenResult.this, "Functionality not implemented jet.", Toast.LENGTH_LONG).show();
    }


    //listview
    private class StableArrayAdapter extends ArrayAdapter<DataSetResult> {

        HashMap<Integer, DataSetResult> mIdMap = new HashMap<Integer, DataSetResult>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<DataSetResult> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(i, objects.get(i));
            }
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.analytic_screen_result_item, null);
            }

            DataSetResult dataSetResult = mIdMap.get(position);


            if (dataSetResult != null) {
                TextView amount_earnings = (TextView) v.findViewById(R.id.analytic_screen_result_amount_earnings);
                TextView amount_expanses = (TextView) v.findViewById(R.id.analytic_screen_result_amount_expanses);
                TextView description = (TextView) v.findViewById(R.id.analytic_screen_result_description);
                TextView date = (TextView) v.findViewById(R.id.analytic_screen_result_date);
                ImageView imageView = (ImageView) v.findViewById(R.id.asr_item_image);

                if (description != null) {
                    description.setText(dataSetResult.category );
                }

                String earnings;
                String expanses;
                String buffer="";
                earnings = "Earnings: " + processEnteredAmount(dataSetResult.amount_earnings) + " EU";
                expanses = "Expanses: " + processEnteredAmount(dataSetResult.amount_expanses) + " EU";
                Integer differenceInLength = earnings.length()-1-expanses.length();
                Integer absDifferenceInLength = (differenceInLength < 0) ? -differenceInLength : differenceInLength;
                for(Integer iterator=0; iterator<(absDifferenceInLength*2); iterator++)
                    buffer=buffer+" ";

                if(differenceInLength > 0){
                    expanses = "Expanses: " + buffer + processEnteredAmount(dataSetResult.amount_expanses) + " EU";
                }else if(differenceInLength < 0){
                    earnings = "Earnings: " + buffer + processEnteredAmount(dataSetResult.amount_earnings) + " EU";
                }

                if(date != null) {
                    date.setText(dataSetResult.date_from + " - " + dataSetResult.date_to);
                }

                if(amount_earnings != null) {
                    amount_earnings.setText(earnings);
                }

                if(amount_expanses != null) {
                    amount_expanses.setText(expanses);
                }

                String[] stringArray = this.getContext().getResources().getStringArray(R.array.category_array);


                if(dataSetResult.category.equals(stringArray[0]))
                    imageView.setBackgroundResource(R.mipmap.other);
                else if(dataSetResult.category.equals(stringArray[1]))
                    imageView.setBackgroundResource(R.mipmap.car);
                else if(dataSetResult.category.equals(stringArray[2]))
                    imageView.setBackgroundResource(R.mipmap.food);
                else if(dataSetResult.category.equals(stringArray[3]))
                    imageView.setBackgroundResource(R.mipmap.sport);
                else if(dataSetResult.category.equals(stringArray[4]))
                    imageView.setBackgroundResource(R.mipmap.work);
                else if(dataSetResult.category.equals(stringArray[5]))
                    imageView.setBackgroundResource(R.mipmap.living);
                else if(dataSetResult.category.equals(this.getContext().getResources().getString(R.string.asr_string_overall_sum)))
                    imageView.setBackgroundResource(R.mipmap.sum);
                else
                    imageView.setBackgroundResource(R.mipmap.ic_launcher);
            }

            return v;
        }

        String processEnteredAmount(String inputString){

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

    }

    public ArrayList<DataSetResult> getResultsFromData(ArrayList<DataSet> dataSetList, DataBaseRequest dataBaseRequest){
        ArrayList<DataSetResult> dataSetResultList = new ArrayList<DataSetResult>();

        String[] stringArray = this.getResources().getStringArray(R.array.category_array);

        Double[] amountExpanses = new Double[stringArray.length];
        Double[] amountEarnings = new Double[stringArray.length];
        int iterator;
        for(iterator=0; iterator<stringArray.length; iterator++){
            amountExpanses[iterator]=0.0;
            amountEarnings[iterator]=0.0;
        }
        int iteratorCategory;
        for(iterator=0; iterator<dataSetList.size(); iterator++){
            for(iteratorCategory=0; iteratorCategory < stringArray.length; iteratorCategory++){
                if(stringArray[iteratorCategory].equals(dataSetList.get(iterator).category)){
                    if(dataSetList.get(iterator).expanse.toCharArray()[0] == 'T'){
                        amountExpanses[iteratorCategory]=amountExpanses[iteratorCategory]+ Double.valueOf(dataSetList.get(iterator).amount);
                    }else if(dataSetList.get(iterator).expanse.toCharArray()[0] == 'F'){
                        amountEarnings[iteratorCategory]=amountEarnings[iteratorCategory]+Double.valueOf(dataSetList.get(iterator).amount);
                    }else{
                        assert(true);
                        assert(false);
                    }

                }
            }

        }

        Double overAllSumExpanses = 0.0;
        Double overAllSumEarnings = 0.0;
        for(iterator=0; iterator < stringArray.length; iterator++){
            DataSetResult dataSetResult = new DataSetResult();
            dataSetResult.date_from=dataBaseRequest.date_from;
            dataSetResult.date_to=dataBaseRequest.date_to;
            dataSetResult.category=stringArray[iterator];
            overAllSumExpanses=overAllSumExpanses+amountExpanses[iterator];
            overAllSumEarnings=overAllSumEarnings+amountEarnings[iterator];
            dataSetResult.amount_earnings=String.valueOf(amountEarnings[iterator]);
            dataSetResult.amount_expanses=String.valueOf(amountExpanses[iterator]);
            dataSetResultList.add(dataSetResult);
        }

        DataSetResult dataSetResultOverallSum = new DataSetResult();
        dataSetResultOverallSum.date_from=dataBaseRequest.date_from;
        dataSetResultOverallSum.date_to=dataBaseRequest.date_to;
        dataSetResultOverallSum.category=this.getResources().getString(R.string.asr_string_overall_sum);
        dataSetResultOverallSum.amount_earnings=String.valueOf(overAllSumEarnings);
        dataSetResultOverallSum.amount_expanses=String.valueOf(overAllSumExpanses);
        dataSetResultList.add(dataSetResultOverallSum);

        return dataSetResultList;
    }
}
