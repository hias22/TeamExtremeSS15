package com.example.moja.pfa;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


public class EnteringScreen extends ActionBarActivity implements View.OnClickListener {

    TextView date;
    ImageView datepicker;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entering_screen);

        date=(TextView)findViewById(R.id.date);
        datepicker=(ImageView)findViewById(R.id.datepicker);
        datepicker.setOnClickListener(this);
        // TODO set current date in


        Spinner spinner = (Spinner) findViewById(R.id.es_category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entering_screen, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void openDataOverviewScreen() {
        Intent intent = new Intent(this, DataOverviewScreen.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.datepicker:
                createDialog(1).show();
                break;
        }
    }

    public Dialog createDialog(int id)
    {
        Dialog dialog=null;
        switch(id)
        {
            case 1:
                Calendar c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int monthOfYear=c.get(Calendar.MONTH);
                int dayOfMonth=c.get(Calendar.DAY_OF_MONTH);
                dialog=new DatePickerDialog(EnteringScreen.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String selecteddate=(String.valueOf(dayOfMonth)).concat("/").concat(String.valueOf(monthOfYear+1)).concat("/").concat(String.valueOf(year));
                        date.setText(selecteddate);
                    }
                }, year, monthOfYear, dayOfMonth);
                break;
        }
        return dialog;
    }
}
