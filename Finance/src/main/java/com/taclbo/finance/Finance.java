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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Calendar;

import static android.app.DatePickerDialog.OnDateSetListener;

public class Finance extends FragmentActivity {

    //Main Content
    private SharedPreferences savedTable; //saved bill table
    private ScrollView scrollView;
    private TableRow billsRow;
    private TableRow addRow;
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

        //get a reference to the scroll view
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        //get a reference to the bill_list layout
        billsTableLayout = (TableLayout)inflater.inflate(R.layout.bill_list, null);

        //get a reference to billsRow
        billsRow = (TableRow)billsTableLayout.findViewById(R.id.billRow);

        //get a reference to add_row
        addRow = (TableRow)billsTableLayout.findViewById(R.id.addRow);

        //get a reference to the add row button
        Button addButton = (Button)billsTableLayout.findViewById(R.id.addButton);
        addButton.setOnClickListener(addBillRowOnClickListener);

        //get a reference to the remove button
        ImageButton removeButton = (ImageButton)billsTableLayout.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(removeBillRowOnClickListener);

        //add the billList to the scroll view
        scrollView.addView(billsTableLayout);

        //set billsTableLayout index
        index = 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finance, menu);
        return true;
    }

    //OnClickListener for the addButton. Adds a new row to the table.
    public View.OnClickListener addBillRowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //get a reference to the LayoutInflater service
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);

            //inflate a BillRow and an AddRow to add to the end of the table
            View newBillRow = inflater.inflate(R.layout.bill_row, null);
            View newAddRow = inflater.inflate(R.layout.add_row, null);

            //set addButton's onClickListener
            Button addBtn = (Button) newAddRow.findViewById(R.id.addButton);
            addBtn.setOnClickListener(addBillRowOnClickListener);

            //set remove onClickListener
            ImageButton removeButton = (ImageButton)newBillRow.findViewById(R.id.removeButton);
            removeButton.setOnClickListener(removeBillRowOnClickListener);

            //remove AddRow that was just clicked.
            billsTableLayout.removeViewAt(index);

            //Add the two new rows to the end of the table.
            billsTableLayout.addView(newBillRow);
            Log.d(TAG, "number of children in table: " + String.valueOf(billsTableLayout.getChildCount()));
            Log.d(TAG, "index: " + String.valueOf(index));
            billsTableLayout.addView(newAddRow);
            index++;
        }
    };

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
