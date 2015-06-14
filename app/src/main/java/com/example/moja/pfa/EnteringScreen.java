package com.example.moja.pfa;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;


public class EnteringScreen extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "EnteringScreen";

    TextView date;
    ImageView datepicker;
    Spinner spinner;
    DatabaseInterface databaseInterface;
    EditText editText_amount;
    EditText editText_description;
    Button button_store, button_clear;
    boolean manipulateDataSet;
    DataSet dataSetToManipulate;
    boolean isEnteredAmountAnExpanse;
    ImageButton imageButton;
    Context this_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entering_screen);

        setTitle(R.string.es_title);

        date=(TextView)findViewById(R.id.date);
        datepicker=(ImageView)findViewById(R.id.es_datepicker);
        datepicker.setOnClickListener(this);
        button_clear = (Button)findViewById(R.id.es_button_clear);
        button_store = (Button)findViewById(R.id.es_button_store);
        button_clear.setOnClickListener(this);
        button_store.setOnClickListener(this);

        editText_amount = (EditText) findViewById(R.id.es_input_transaction_amount);
        editText_description = (EditText) findViewById(R.id.es_input_description);


        setSpinner();

        databaseInterface= new DatabaseInterface(this);
        databaseInterface.getDataCount();
        DataBaseTest dataBaseTest = new DataBaseTest(databaseInterface);
        dataBaseTest.testAll();

        isEnteredAmountAnExpanse=true;

        Intent intent = getIntent();
        manipulateDataSet = intent.getBooleanExtra("manipulateDataSet",false);
        if(manipulateDataSet){
            dataSetToManipulate = intent.getParcelableExtra("dataSet");
            editText_amount.setText(dataSetToManipulate.amount);
            editText_description.setText(dataSetToManipulate.description);
            String[] categories = getResources().getStringArray(R.array.category_array);
            if(dataSetToManipulate.expanse.toCharArray()[0] == 'F')
                isEnteredAmountAnExpanse=false;
            for(int position = 0; position < categories.length; ++position) {
                if(categories[position].equals(dataSetToManipulate.category)) {
                    spinner.setSelection(position);
                }
            }
            date.setText(dataSetToManipulate.date);
        }

        imageButton = (ImageButton) findViewById(R.id.es_image_button);
        if(isEnteredAmountAnExpanse)
            imageButton.setBackgroundResource(R.mipmap.minus);
        else
            imageButton.setBackgroundResource(R.mipmap.plus);

        imageButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                if(isEnteredAmountAnExpanse) {
                    imageButton.setBackgroundResource(R.mipmap.plus);
                    isEnteredAmountAnExpanse=false;
                }
                else {
                    imageButton.setBackgroundResource(R.mipmap.minus);
                    isEnteredAmountAnExpanse=true;
                }
            }
        });



    }

    private void setSpinner() {
        spinner = (Spinner) findViewById(R.id.es_category_spinner);
        ArrayList<String> categoryList = Utils.getInstance().createCategoryList(this, true);
        ArrayAdapter<String> adapterString = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,categoryList);
        adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterString);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (String.valueOf(spinner.getSelectedItem()).equals(parent.getResources().getString(R.string.spinner_manage_category)))
                    dropdownManageCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entering_screen, menu);
        return true;
    }

    public void dropdownManageCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new category name");
        this_context = this;
        spinner.setSelection(0);

        builder.setPositiveButton("Add new category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addNewCategory();
            }
        });
        builder.setNegativeButton("Delete category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCategory();
            }
        });

        builder.show();
    }

    public void deleteCategory(){
        ArrayList<String> items =Utils.getInstance().createCustomerCategoryList(this);
        CharSequence[] csItems = items.toArray(new CharSequence[items.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList<Integer> deleteCategoryList = new ArrayList<Integer>();
        builder.setTitle("Select categories to delete");
        builder.setMultiChoiceItems(csItems, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                           deleteCategoryList.add(which);
                        } else if (deleteCategoryList.contains(which)) {
                            deleteCategoryList.remove(Integer.valueOf(which));
                        }
                    }
                });

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Utils.getInstance().deleteCustomCategory(this_context, deleteCategoryList);
                setSpinner();
                spinner.setSelection(0);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                spinner.setSelection(0);
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addNewCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new category name");
        this_context = this;
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category_name = input.getText().toString();
                if(category_name.contains(";")){
                    Toast.makeText(EnteringScreen.this, "Category must not contain a ';'" ,Toast.LENGTH_LONG).show();
                    return;
                }
                Utils.getInstance().saveCustomCategory(this_context, category_name);
                setSpinner();
                spinner.setSelection(1);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                spinner.setSelection(0);
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_overview) {
            openDataOverviewScreen();
        }
        if (id == R.id.action_analytic) {
            openAnalyticScreenSearch();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDataOverviewScreen() {
        Intent intent = new Intent(this, DataOverviewScreen.class);
        startActivity(intent);
    }

    public void openAnalyticScreenSearch() {
        Intent intent = new Intent(this, AnalyticScreenSearch.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        switch(v.getId()) {
            case R.id.es_datepicker:
                createDialog(1).show();
                break;
            case R.id.es_button_store:
                storePressed();
                break;
            case R.id.es_button_clear:
                clearScreen();
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
                final Date dateEntered = new Date(dayOfMonth,monthOfYear,year);
                dialog=new DatePickerDialog(EnteringScreen.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String selecteddate=(dateEntered.toString());
                        date.setText(selecteddate);
                    }
                }, year, monthOfYear, dayOfMonth);
                break;
        }
        return dialog;
    }





    void storePressed(){
        Log.d(TAG, "storePressed");
        if(Utils.getInstance().amountInputValid(editText_amount.getText().toString())) {
            String selected_date = "";
            if (date.getText().toString().equals("   today   ") == true) {
                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                int day = c.get(Calendar.DAY_OF_MONTH);
                selected_date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
            } else
                selected_date = date.getText().toString();

            String type;
            if(isEnteredAmountAnExpanse)
                type="T";
            else
                type="F";

            String enteredAmount = Utils.getInstance().processEnteredAmount(editText_amount.getText().toString());
            DataSet dataSet = new DataSet(enteredAmount,
                    editText_description.getText().toString(),
                    spinner.getSelectedItem().toString(), selected_date, type);

            if (manipulateDataSet) {
                dataSet.dataBaseId=dataSetToManipulate.dataBaseId;
                databaseInterface.updateDataSet(dataSet);
                Toast.makeText(EnteringScreen.this, "Data updated.", Toast.LENGTH_LONG).show();
                manipulateDataSet = false;
                clearScreen();
                openDataOverviewScreen();
            } else {
                databaseInterface.insertDataSet(dataSet);
                Toast.makeText(EnteringScreen.this, "Data stored.", Toast.LENGTH_LONG).show();
                clearScreen();
            }


        }else{
            Toast.makeText(EnteringScreen.this, "Please enter a valid amount.", Toast.LENGTH_LONG).show();
        }
    }

    void clearScreen() {
        date.setText("   today   ");
        editText_amount.setText("");
        editText_description.setText("");
        spinner.setSelection(0);
        imageButton.setBackgroundResource(R.mipmap.minus);
        isEnteredAmountAnExpanse = true;
        if (manipulateDataSet) {
            databaseInterface.deleteDataSet(dataSetToManipulate);
            Toast.makeText(EnteringScreen.this, "Data set deleted.", Toast.LENGTH_LONG).show();
            manipulateDataSet = false;
            openDataOverviewScreen();
        }
    }



}
