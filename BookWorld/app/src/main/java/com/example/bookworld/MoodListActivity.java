package com.example.bookworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoodListActivity extends AppCompatActivity {

    ArrayList<BookItem> moodbooklist;
    RecyclerView recyclerView;
    RecyclerviewAdapter moodbookAdapter;
    int mood1, mood2;
    String moodcategory, uppercategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);

        mood1 = getIntent().getIntExtra("mood1", 0);
        mood2 = getIntent().getIntExtra("mood2", 0);
        uppercategory = getIntent().getStringExtra("category");
        moodcategory = "mood"+Integer.toString(mood1)+Integer.toString(mood2);

        String url = getString(R.string.server_address) + "moodlist.php";
        ContentValues values = new ContentValues();
        values.put("mood", moodcategory);
        values.put("category", uppercategory);

        NetworkTask networkTask = new NetworkTask(url, values);
        String result;

        moodbooklist = new ArrayList<BookItem>();

        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            JSONArray jsonarray = json.getJSONArray("moodlist");
            int length;
            if(jsonarray.length() > 10)
                length = 10;
            else
                length = jsonarray.length();
            for(int i=0; i < length; i++) {
                json = jsonarray.getJSONObject(i);
                moodbooklist.add(new BookItem(R.drawable.bookimg, json.getString("title"), json.getString("author"),
                        json.getString("company"), json.getString("date_rel"), json.getString("isbn")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }//서버,json 문제 예외처리들

        if(moodbooklist.size() ==0) {
            Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_LONG).show();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviw_moodcategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MoodListActivity.this));
        moodbookAdapter = new RecyclerviewAdapter(getApplicationContext(), moodbooklist, null, false, true);
        recyclerView.setAdapter(moodbookAdapter);
    }

}
