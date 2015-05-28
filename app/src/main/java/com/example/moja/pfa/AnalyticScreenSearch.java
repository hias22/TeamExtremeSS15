package com.example.moja.pfa;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class AnalyticScreenSearch extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "AnalyticScreenSearch";

    TextView dateFrom;
    TextView dateTo;
    Button button_ok;
    ImageView datepickerFrom;
    ImageView datepickerTo;
    DataBaseRequest dataBaseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytic_screen_search);

        setTitle(R.string.ass_title);

        dateFrom=(TextView)findViewById(R.id.date_from);
        dateFrom.setText(Utils.getInstance().getDateFromNMonthAgo(3));
        dateTo=(TextView)findViewById(R.id.date_to);
        datepickerFrom=(ImageView)findViewById(R.id.ass_datepicker_from);
        datepickerFrom.setOnClickListener(this);
        datepickerTo=(ImageView)findViewById(R.id.ass_datepicker_to);
        datepickerTo.setOnClickListener(this);
        button_ok = (Button)findViewById(R.id.ass_ok_button);
        button_ok.setOnClickListener(this);

        dataBaseRequest = new DataBaseRequest();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analytic_screen_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_overview) {
            openDataOverviewScreen();
        }
        if (id == R.id.action_entering) {
            openEnteringScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openEnteringScreen() {
        Intent intent = new Intent(this, EnteringScreen.class);
        startActivity(intent);
    }

    public void openAnalyticScreenResult() {
        Intent intent = new Intent(this, AnalyticScreenResult.class);
        intent.putExtra("dataBaseRequest",dataBaseRequest);
        startActivity(intent);
    }

    public void openDataOverviewScreen() {
        Intent intent = new Intent(this, DataOverviewScreen.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        switch(v.getId()) {
            case R.id.ass_datepicker_from:
                createDialog(1).show();
                break;
            case R.id.ass_datepicker_to:
                createDialog(2).show();
                break;
            case R.id.ass_ok_button:
                okButtonPressed();
                break;
        }
    }

    public void okButtonPressed(){
        dataBaseRequest.date_from=dateFrom.getText().toString();
        dataBaseRequest.date_to=dateTo.getText().toString();

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (dataBaseRequest.date_from.equals("   today   ")) {
            dataBaseRequest.date_from = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        }
        if (dataBaseRequest.date_to.equals("   today   ")) {
            dataBaseRequest.date_to = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        }

        openAnalyticScreenResult();
    }

    public Dialog createDialog(int id)
    {
        Dialog dialog=null;

        Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int monthOfYear=c.get(Calendar.MONTH);
        int dayOfMonth=c.get(Calendar.DAY_OF_MONTH);

        switch(id)
        {
            case 1:
                dialog = new DatePickerDialog(AnalyticScreenSearch.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String selecteddate=(String.valueOf(dayOfMonth)).concat("/").concat(String.valueOf(monthOfYear+1)).concat("/").concat(String.valueOf(year));
                        dateFrom.setText(selecteddate);
                    }
                }, year, monthOfYear, dayOfMonth);
                break;
            case 2:
                dialog = new DatePickerDialog(AnalyticScreenSearch.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String selecteddate=(String.valueOf(dayOfMonth)).concat("/").concat(String.valueOf(monthOfYear+1)).concat("/").concat(String.valueOf(year));
                        dateTo.setText(selecteddate);
                    }
                }, year, monthOfYear, dayOfMonth);
                break;
        }
        return dialog;
    }


}
