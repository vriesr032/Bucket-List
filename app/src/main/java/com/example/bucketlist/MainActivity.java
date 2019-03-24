package com.example.bucketlist;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, BucketListAdapter.ItemClickListner {

    private BucketListAdapter bucketListAdapter;
    private RecyclerView rvBucketList;
    private List<Item> bucketList = new ArrayList<>();
    private GestureDetector gestureDetector;
    private ItemRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView text1;
    private TextView text2;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = ItemRoomDatabase.getDatabase(this);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        checkBox = findViewById(R.id.checkBox);

        initRecyclerView();
        getAllItems();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        bucketListAdapter = new BucketListAdapter(bucketList, this);
        rvBucketList = findViewById(R.id.bucketList);
        rvBucketList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBucketList.setAdapter(bucketListAdapter);
        rvBucketList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        // Delete an item from the shopping list on long press.
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = rvBucketList.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = rvBucketList.getChildAdapterPosition(child);
                    deleteItem(bucketList.get(adapterPosition));
                }
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (e1.getY() < e2.getY()) {
                    getAllItems();
                }

                return true;
            }
        });

        rvBucketList.addOnItemTouchListener(this);
        getAllItems();
    }

    private void updateUI(List<Item> items) {
        bucketList.clear();
        bucketList.addAll(items);
        bucketListAdapter.notifyDataSetChanged();
    }

    public void getAllItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Item> items = db.itemDao().getAllItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(items);
                    }
                });
            }
        });
    }

    private void deleteItem(final Item item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().delete(item);
                getAllItems();
            }
        });
    }

    private void deleteAllItems(final List<Item> items) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().delete(items);
                getAllItems();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteAllItems(bucketList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public void onCheckboxClick(Item item) {
        if(checkBox.isChecked()){
            text1.setPaintFlags(text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            text2.setPaintFlags(text2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            text1.setPaintFlags(text1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            text2.setPaintFlags(text2.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
