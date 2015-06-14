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

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class DataOverviewScreen extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_overview_screen);

        setTitle(R.string.os_title);

        final ListView listview = (ListView) findViewById(R.id.data_overview_list_view);

        ArrayList<DataSet> dataSetList;
        DatabaseInterface databaseInterface = new DatabaseInterface(this);
        dataSetList = databaseInterface.getAllDataSets();

        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, dataSetList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object listViewObject = listview.getItemAtPosition(position);
                reactOnListViewItemSelected((DataSet) listViewObject);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_overview_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        intent.putExtra("dataSet", dataSet);
        startActivity(intent);
    }

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
            DataSet dataSet = mIdMap.get(position);
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(dataSet.expanse.toCharArray()[0] == 'T')
                    v = vi.inflate(R.layout.data_overview_item_red, null);
                else if(dataSet.expanse.toCharArray()[0] == 'F')
                    v = vi.inflate(R.layout.data_overview_item_green, null);
                else
                    v = vi.inflate(R.layout.data_overview_item, null);
            }


            if (dataSet != null) {
                TextView description1 = (TextView) v.findViewById(R.id.data_overview_screen_description1);
                TextView description2 = (TextView) v.findViewById(R.id.data_overview_screen_description2);
                TextView amount = (TextView) v.findViewById(R.id.data_overview_screen_amount);
                TextView category = (TextView) v.findViewById(R.id.data_overview_screen_category);
                TextView date = (TextView) v.findViewById(R.id.data_overview_screen_date);
                ImageView imageView = (ImageView)  v.findViewById(R.id.item_image);

                String[] descriptionArray = Utils.getInstance().createDescriptionOutput(dataSet.description);

                if (description1 != null) {
                    description1.setText(descriptionArray[0]);
                }
                if (description2 != null) {
                    description2.setText(descriptionArray[1]);
                }

                if(amount != null) {
                    amount.setText(Utils.getInstance().processEnteredAmount(dataSet.amount) + " EUR" );
                }

                if (category != null) {
                    category.setText(dataSet.category);
                }

                if(date != null) {
                    date.setText(dataSet.date);
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


    }
}
