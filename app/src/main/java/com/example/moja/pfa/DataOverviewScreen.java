package com.example.moja.pfa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mathias on 09.05.2015.
 */
public class DataOverviewScreen extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_overview_screen);

        //listview:

        final ListView listview = (ListView) findViewById(R.id.data_overview_list_view);

        ArrayList<DataSet> dataSetList = new ArrayList<DataSet>();
        DatabaseInterface databaseInterface = new DatabaseInterface(this);
        dataSetList = databaseInterface.getAllDataSets();

        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, dataSetList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object listViewObject = listview.getItemAtPosition(position);
                //DataSet selectedDataSet = (DataSet) listViewObject;
                reactOnListViewItemSelected((DataSet) listViewObject);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_overview_screen, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void openEnteringScreen() {
        Intent intent = new Intent(this, EnteringScreen.class);
        startActivity(intent);
    }

    public void openAnalyticScreenSearch() {
        Intent intent = new Intent(this, AnalyticScreenSearch.class);
        startActivity(intent);
    }

    public void reactOnListViewItemSelected(DataSet dataSet) {
        Intent intent = new Intent(this, EnteringScreen.class);
        intent.putExtra("manipulateDataSet", true);
        intent.putExtra("dataSet",(Parcelable) dataSet);
        startActivity(intent);
    }


    //listview
    private class StableArrayAdapter extends ArrayAdapter<DataSet> {

        HashMap<Integer, DataSet> mIdMap = new HashMap<Integer, DataSet>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<DataSet> objects) {
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
                v = vi.inflate(R.layout.data_overview_item, null);
            }

            DataSet dataSet = mIdMap.get(position);


            if (dataSet != null) {
                TextView description_category = (TextView) v.findViewById(R.id.data_overview_screen_description);
                TextView amount = (TextView) v.findViewById(R.id.data_overview_screen_amount);
                ImageView imageView = (ImageView)  v.findViewById(R.id.item_image);

                if (description_category != null) {
                    description_category.setText(dataSet.description + "["  + dataSet.category + "] - "+ dataSet.date);
                }

                if(amount != null) {
                    amount.setText(processEnteredAmount(dataSet.amount) + " EU" );
                }

                if(dataSet.expanse.toCharArray()[0] == 'T')
                    imageView.setBackgroundResource(R.mipmap.minus);
                else if(dataSet.expanse.toCharArray()[0] == 'F')
                    imageView.setBackgroundResource(R.mipmap.plus);
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
}
