package com.example.tasklist;

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

public class DeleteOrEditTaskActivity extends Activity implements View.OnClickListener {
    private EditText textTitle;
    private Button update, delete;
    private EditText descText;
    private RadioGroup RadioProgress;
    private RadioGroup RadioUrgency;
    private long _id;
    DatePickerDialog datePicker;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Delete or Edit Task");
        setContentView(R.layout.activity_delete_or_edit_task);

        dbManager = new DBManager(this);
        dbManager.open();

        textTitle = (EditText) findViewById(R.id.add_your_task);
        descText = (EditText) findViewById(R.id.date_to_perform);
        descText.setInputType(InputType.TYPE_NULL);
        descText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalender(v);
            }
        });

        update = (Button) findViewById(R.id.btn_update);
        delete = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String done = intent.getStringExtra("checkbox");
        String urgency = intent.getStringExtra("urgent");

        _id = Long.parseLong(id);
        textTitle.setText(name);
        descText.setText(desc);

        RadioProgress = (RadioGroup) findViewById(R.id.progressRadio);
        setDone(done);
        RadioUrgency = (RadioGroup) findViewById(R.id.urgencyRadio);
        setUrgency(urgency);

        update.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
            case R.id.btn_update:
                updateBtn();
                break;
        }
    }

    //Return to the main screen
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    //set the calendar
    private void setCalender(View v){
        final Calendar calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH);
        int year = calender.get(Calendar.YEAR);
        datePicker = new DatePickerDialog(DeleteOrEditTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        descText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePicker.show();
    }
    //task update
    private void updateBtn(){
        String title = textTitle.getText().toString();
        String desc = descText.getText().toString();
        int status = RadioProgress.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton)findViewById(status);
        String checked = rb.getText().toString();

        status = RadioUrgency.getCheckedRadioButtonId();
        rb = (RadioButton)findViewById(status);
        String urgencyTask = rb.getText().toString();

        if(title.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter task title", Toast.LENGTH_LONG).show();
        }else if(desc.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter date to preform", Toast.LENGTH_LONG).show();
        }
        else{
            dbManager.update(_id, title, desc, checked, urgencyTask);
            this.returnHome();
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
