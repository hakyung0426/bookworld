package com.example.bookworld;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookInfoReviewActivity extends AppCompatActivity {

    TextView title;
    RecyclerView recyclerView;
    BookreviewRvAdapter bookreviewadapter;
    ArrayList<Bookreview> bookreviewlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo_review);

        title = findViewById(R.id.review_booktitle);
        recyclerView = findViewById(R.id.recyclerview_bookreview);

        Bundle info = getIntent().getExtras();
        String name = info.getBundle("bundle").getString("name");
        String isbn = info.getBundle("bundle").getString("isbn");
        title.setText(name);

        bookreviewlist = new ArrayList<Bookreview>();


        String url = getString(R.string.server_address) + "bookreview.php";
        ContentValues values = new ContentValues();
        values.put("isbn", isbn);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;


        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            String iserror = String.valueOf(json.getBoolean("error"));
            JSONArray jsonarray = json.getJSONArray("reviewlist");

            for(int i = 0; i < jsonarray.length();  i++) {
                json = jsonarray.getJSONObject(i);
                MoodCategory moodCategory = new MoodCategory(json.getString("mood"));
                Double star = json.getDouble("star");
                bookreviewlist.add(new Bookreview(json.getString("userid"), json.getString("date_sav"),
                        String.format("%.1f", star), moodCategory.getMood1() + "\n" + moodCategory.getMood2(), json.getString("content") ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if(bookreviewlist.size() == 0) {
            Toast.makeText(this, "등록된 독후감이 없습니다.", Toast.LENGTH_LONG).show();
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookreviewadapter = new BookreviewRvAdapter(this, bookreviewlist, isbn);
        recyclerView.setAdapter(bookreviewadapter);


    }
}
