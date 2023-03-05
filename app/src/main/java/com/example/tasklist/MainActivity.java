package com.example.tasklist;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    final String[] from = new String[] {
            DatabaseHelper._ID,
            DatabaseHelper.SUBJECT,
            DatabaseHelper.DESC,
            DatabaseHelper.CHECK,
            DatabaseHelper.URGENT
    };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc, R.id.todoCheckBox, R.id.emergency };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.read();

        listView = (ListView) findViewById(R.id.list_view);

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_task, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, viewId) -> {
            TextView idTextView = (TextView) view.findViewById(R.id.id);
            TextView titleTextView = (TextView) view.findViewById(R.id.title);
            TextView descTextView = (TextView) view.findViewById(R.id.desc);
            TextView checkTextView = (TextView) view.findViewById(R.id.todoCheckBox);
            TextView emergencyTextView = (TextView) view.findViewById(R.id.emergency);

            String id = idTextView.getText().toString();
            String title = titleTextView.getText().toString();
            String desc = descTextView.getText().toString();
            String checkbox = checkTextView.getText().toString();
            String emergency = emergencyTextView.getText().toString();

            Intent intent = new Intent(getApplicationContext(), DeleteOrEditTaskActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("desc", desc);
            intent.putExtra("id", id);
            intent.putExtra("checkbox", checkbox);
            intent.putExtra("urgent", emergency);
            startActivity(intent);
        });
    }

    //Creating the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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
