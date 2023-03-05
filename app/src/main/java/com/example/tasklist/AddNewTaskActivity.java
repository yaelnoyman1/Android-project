package com.example.tasklist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class AddNewTaskActivity extends Activity implements View.OnClickListener {
    private Button add;
    private Button cancel;
    private EditText textTitle;
    private EditText descText;
    private RadioGroup RadioProgress;
    private RadioGroup RadioUrgency;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add New Task");
        setContentView(R.layout.activity_add_new_task);

        dbManager = new DBManager(this);
        dbManager.open();

        textTitle = (EditText) findViewById(R.id.title_task);
        descText = (EditText) findViewById(R.id.date_to_perform);
        descText.setInputType(InputType.TYPE_NULL);
        descText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalender(v);
            }
        });

        RadioProgress = (RadioGroup) findViewById(R.id.urgencyRadio);
        RadioUrgency = (RadioGroup) findViewById(R.id.progressRadio);
        add = (Button) findViewById(R.id.add_task);
        cancel = (Button) findViewById(R.id.cancel);

        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task:
                setAddTask();
                break;
            case R.id.cancel:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    //set the calendar
    private void setCalender(View v){
        final Calendar calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH);
        int year = calender.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(AddNewTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        descText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    //add task
    private void setAddTask(){
        final String name = textTitle.getText().toString();
        final String desc = descText.getText().toString();
        int status = RadioProgress.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton)findViewById(status);
        String checked = rb.getText().toString();
        setDone(checked);

        status = RadioUrgency.getCheckedRadioButtonId();
        rb = (RadioButton)findViewById(status);
        String urgencyTask = rb.getText().toString();
        setUrgency(urgencyTask);
        if(name.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter task title", Toast.LENGTH_LONG).show();
        }else if(desc.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter date to preform", Toast.LENGTH_LONG).show();
        }
        else{
            dbManager.insert(name, desc, checked, urgencyTask);
            Intent main = new Intent(AddNewTaskActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }
    }
    private void setUrgency(String s){
        RadioButton normal = (RadioButton)findViewById(R.id.normal);
        RadioButton urgent = (RadioButton)findViewById(R.id.urgent);
        switch (s){
            case "No pressure":
                normal.setChecked(true);
                urgent.setChecked(false);
                break;
            case "Urgent":
                normal.setChecked(false);
                urgent.setChecked(true);
                break;
        }
    }

    private void setDone(String s){
        RadioButton Start = (RadioButton)findViewById(R.id.in_progress);
        RadioButton Done = (RadioButton)findViewById(R.id.tasks_done);
        switch (s){
            case "In progress":
                Start.setChecked(true);
                Done.setChecked(false);
                break;
            case "Done":
                Start.setChecked(false);
                Done.setChecked(true);
                break;
        }
    }
}