package com.example.moja.pfa;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entering_screen);

        date=(TextView)findViewById(R.id.date);
        datepicker=(ImageView)findViewById(R.id.es_datepicker);
        datepicker.setOnClickListener(this);
        button_clear = (Button)findViewById(R.id.es_button_clear);
        button_store = (Button)findViewById(R.id.es_button_store);
        button_clear.setOnClickListener(this);
        button_store.setOnClickListener(this);

        editText_amount = (EditText) findViewById(R.id.es_input_transaction_amount);
        editText_description = (EditText) findViewById(R.id.es_input_description);


        //dropdown
        spinner = (Spinner) findViewById(R.id.es_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
            //Toast.makeText(EnteringScreen.this, dataSetToManipulate.category ,Toast.LENGTH_LONG).show();
        }

        //ImageButton
        imageButton = (ImageButton) findViewById(R.id.es_image_button);
        if(isEnteredAmountAnExpanse)
            imageButton.setBackgroundResource(R.mipmap.ic_minus);
        else
            imageButton.setBackgroundResource(R.mipmap.ic_plus);

        imageButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                if(isEnteredAmountAnExpanse) {
                    imageButton.setBackgroundResource(R.mipmap.ic_plus);
                    isEnteredAmountAnExpanse=false;
                }
                else {
                    imageButton.setBackgroundResource(R.mipmap.ic_minus);
                    isEnteredAmountAnExpanse=true;
                }
            }
        });

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

    void storePressed(){
        Log.d(TAG, "storePressed");
        if(amountInputValid(editText_amount.getText().toString())) {
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

            String enteredAmount = processEnteredAmount(editText_amount.getText().toString());
            DataSet dataSet = new DataSet(enteredAmount,
                    editText_description.getText().toString(),
                    spinner.getSelectedItem().toString(), selected_date, type);

            if (manipulateDataSet) {
                dataSet.dataBaseId=dataSetToManipulate.dataBaseId;
                databaseInterface.updateDataSet(dataSet);
                Toast.makeText(EnteringScreen.this, "data updated", Toast.LENGTH_LONG).show();
                manipulateDataSet = false;
                clearScreen();
                openDataOverviewScreen();
            } else {
                databaseInterface.insertDataSet(dataSet);
                Toast.makeText(EnteringScreen.this, "data stored", Toast.LENGTH_LONG).show();
                clearScreen();
            }


        }else{
            Toast.makeText(EnteringScreen.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
        }
    }

    void clearScreen(){
        date.setText("   today   ");
        editText_amount.setText("");
        editText_description.setText("");
        spinner.setSelection(0);
        imageButton.setBackgroundResource(R.mipmap.ic_minus);
        isEnteredAmountAnExpanse=true;
        if(manipulateDataSet) {
            databaseInterface.deleteDataSet(dataSetToManipulate);
            Toast.makeText(EnteringScreen.this, "data set deleted", Toast.LENGTH_LONG).show();
            manipulateDataSet=false;
            openDataOverviewScreen();
        }
    }
}
