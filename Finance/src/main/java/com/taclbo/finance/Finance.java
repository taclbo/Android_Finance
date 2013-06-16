package com.taclbo.finance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Calendar;

import static android.app.DatePickerDialog.OnDateSetListener;

public class Finance extends FragmentActivity {

    //Main Content
    private SharedPreferences savedTable; //saved bill table
    private TableLayout billsTableLayout;
    private EditText selectedDateEditText;

    //Menu Content
    private MenuItem save;
    private MenuItem clear;

    //billsTableLayout index
    private int index;

    //Debug tag
    String TAG = "finance logger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //get LayoutInflater
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.
                LAYOUT_INFLATER_SERVICE);

        //get the SharedPreferences that contains the user's bills table
        savedTable = getSharedPreferences("bills", MODE_PRIVATE);

        //get a reference to the billsTableLayout
        billsTableLayout = (TableLayout) findViewById(R.id.billsTableLayout);

        //set billsTableLayout index
        index = 0;

        //add the first blank bill row
        addBillRow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finance, menu);

        return true;
    }

    //add a row to the billsTableLayout
    private void addBillRow(){
        Log.d(TAG, "Adding first row");
        //get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.
            LAYOUT_INFLATER_SERVICE);

        //inflate bill_list.xml
        View newBillRow = inflater.inflate(R.layout.bill_list, null);

        //add new row to the bills table
        billsTableLayout.addView(newBillRow, index);
        addAddRow();
        index++;
    }

    //add a greyed out row to thebillsTableLayout
    private void addAddRow(){
        Log.d(TAG, "addGreyedBillRow");
        //get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.
                LAYOUT_INFLATER_SERVICE);

        //Get the layout with the add button
        View newAddRow = inflater.inflate(R.layout.bill_list_addnew, null);

        //get a reference to the button and set the onClickListener to the
        //addBillRowOnClickListener method
        Button addRowBtn = (Button)newAddRow.findViewById(R.id.add_button);
        ViewGroup.LayoutParams tableParams =  billsTableLayout.getLayoutParams();
        TableRow.LayoutParams rowParams = (TableRow.LayoutParams)
                addRowBtn.getLayoutParams();
        Log.d(TAG, "tableParams = " + tableParams.toString());
        rowParams.width = tableParams.width;
        addRowBtn.setLayoutParams(rowParams);

        addRowBtn.setOnClickListener(addBillRowOnClickListener);

        //Add the row to the table
        billsTableLayout.addView(newAddRow, index+1);
    }

    //OnClickListener for the addButton. Adds a new row to the table.
    public View.OnClickListener addBillRowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //get a reference to the LayoutInflater service
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);

            //inflate a BillRow and an AddRow to add to the end of the table
            View newBillRow = inflater.inflate(R.layout.bill_list, null);
            View newAddRow = inflater.inflate(R.layout.bill_list_addnew, null);

            //set addButton's onClickListener
            Button addBtn = (Button) newAddRow.findViewById(R.id.add_button);
            addBtn.setOnClickListener(addBillRowOnClickListener);


            //remove AddRow that was just clicked.
            billsTableLayout.removeViewAt(index);

            //Add the two new rows to the end of the table.
            billsTableLayout.addView(newBillRow);
            billsTableLayout.addView(newAddRow);
            index++;
        }
    };

    /*
    public View.OnClickListener removeBillRowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View v = (View) view.getParent();
            int tmp = billsTableLayout.indexOfChild(v);
            billsTableLayout.removeViewAt(tmp);
            Log.d(TAG, String.valueOf(tmp));
            index--;
        }
    };
    */

    //--------------------------------------DatePickerDialog--------------------------------------//
    //When the date EditText is tapped, show the DatePickerDialog
    public void selectDate(View view){
        DialogFragment newFragment = new SelectDateFragment();
        TableRow selectedRow = (TableRow)view.getParent();
        EditText mEditText = (EditText)selectedRow.findViewById(R.id.date_text);
        selectedDateEditText = mEditText;
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    //Method used to set the date in the dateEditText
    public void populateSetDate(int year, int month, int day){
        selectedDateEditText.setText(month + "/" + day + "/" + year);
    }

    //creates the dialog and sets the date that the dialog displays to the current date.
    public class SelectDateFragment extends DialogFragment implements OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int yy = c.get(Calendar.YEAR);
            int mm = c.get(Calendar.MONTH);
            int dd = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        //When the date is set, send the date that has been set to the populateSetDate method
        public void onDateSet(DatePicker view, int yy, int mm, int dd){
            populateSetDate(yy, mm, dd);
        }
    }
}
