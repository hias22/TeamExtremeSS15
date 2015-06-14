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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyticScreenResultMonthly extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytic_screen_result);

        setTitle(R.string.asrm_title);

        Intent intent = getIntent();
        DataBaseRequest dataBaseRequest = intent.getParcelableExtra("dataBaseRequest");

        ArrayList<DataSetResult> dataSetResultList;
        dataSetResultList = Utils.getInstance().transformDatasetToMonthlyDataset(this, dataBaseRequest);

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
        getMenuInflater().inflate(R.menu.menu_analytic_screen_result, menu);
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
    }

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
                if(position%2==0)
                    v = vi.inflate(R.layout.analytic_screen_result_monthly_item_even, null);
                else
                    v = vi.inflate(R.layout.analytic_screen_result_monthly_item_odd, null);

            }

            DataSetResult dataSetResult = mIdMap.get(position);


            if (dataSetResult != null) {
                TextView amount_earnings = (TextView) v.findViewById(R.id.analytic_screen_result_amount_earnings);
                TextView amount_expanses = (TextView) v.findViewById(R.id.analytic_screen_result_amount_expanses);
                TextView date = (TextView) v.findViewById(R.id.analytic_screen_result_description);
                TextView category = (TextView) v.findViewById(R.id.analytic_screen_result_date);
                ImageView imageView = (ImageView) v.findViewById(R.id.asr_item_image);

                if (category != null) {
                    category.setText(dataSetResult.category );
                }

                String earnings;
                String expanses;
                String buffer="";
                earnings = "Earnings: " + Utils.getInstance().processEnteredAmount(dataSetResult.amount_earnings) + " EU";
                expanses = "Expanses: " + Utils.getInstance().processEnteredAmount(dataSetResult.amount_expanses) + " EU";
                Integer differenceInLength = earnings.length()-1-expanses.length();
                Integer absDifferenceInLength = (differenceInLength < 0) ? -differenceInLength : differenceInLength;
                for(Integer iterator=0; iterator<(absDifferenceInLength*2); iterator++)
                    buffer=buffer+" ";

                if(differenceInLength > 0){
                    expanses = "Expanses: " + buffer + Utils.getInstance().processEnteredAmount(dataSetResult.amount_expanses) + " EU";
                }else if(differenceInLength < 0){
                    earnings = "Earnings: " + buffer + Utils.getInstance().processEnteredAmount(dataSetResult.amount_earnings) + " EU";
                }

                if(date != null) {
                    if(!dataSetResult.date_from.equals(dataSetResult.date_to))
                        date.setText(dataSetResult.date_from + " - " + dataSetResult.date_to);
                    else
                        date.setText(dataSetResult.date_from.substring(3));
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
                    imageView.setBackgroundResource(R.mipmap.user);
            }

            return v;
        }


    }


}
