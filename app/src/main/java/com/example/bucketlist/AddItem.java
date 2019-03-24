package com.example.bucketlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddItem extends AppCompatActivity {

    private EditText Title;
    private EditText Description;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ItemRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        Title = findViewById(R.id.editTitle);
        Description = findViewById(R.id.editDescription);
        db = ItemRoomDatabase.getDatabase(this);

        initCreateButton();
    }

    private void initCreateButton() {
        Button create = findViewById(R.id.createButton);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = Title.getText().toString();
                String description = Description.getText().toString();
                final Item item = new Item(title, description);
                insertItem(item);
                finish();
            }
        });
    }

    private void insertItem(final Item item){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().insert(item);
                runOnUiThread(new Runnable() { // Optionally clear the text from the input field
                    @Override
                    public void run() {
                        Title.setText("");
                        Description.setText("");
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
