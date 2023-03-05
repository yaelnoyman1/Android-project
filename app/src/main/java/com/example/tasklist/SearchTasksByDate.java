package com.example.tasklist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasklist.R;

import java.util.Calendar;
import java.util.Objects;

public class SearchTasksByDate extends AppCompatActivity implements View.OnClickListener {
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private EditText date;
    private Button search;
    private DBManager dbManager;

    final String[] from = new String[] {
            DatabaseHelper._ID,
            DatabaseHelper.SUBJECT,
            DatabaseHelper.DESC ,
            DatabaseHelper.CHECK
    };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc, R.id.todoCheckBox };

    @Override
    public void onClick(View v) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_search_by_date);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Search by Date");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.listViewDate);
        listView.setEmptyView(findViewById(R.id.empty));

        date = (EditText) findViewById(R.id.date_to_search);
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(SearchTasksByDate.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateText = date.getText().toString();
                dbManager = new DBManager(SearchTasksByDate.this);
                dbManager.open();
                cursor = dbManager.read();
                cursor = dbManager.searchByDate(dateText);

                adapter = new SimpleCursorAdapter(SearchTasksByDate.this, R.layout.activity_view_task, cursor, from, to, 0);
                adapter.notifyDataSetChanged();

                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.add_task:
                startActivity(new Intent(this, AddNewTaskActivity.class));
                return true;
            case R.id.search_by_date:
                startActivity(new Intent(this, SearchTasksByDate.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
