package com.example.bookworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecListActivity extends AppCompatActivity {

    ArrayList<BookItem> reclist;
    RecyclerView recyclerView;
    RecyclerviewAdapter recAdapter;
    SharedPreferences loginInfo;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_list);

        loginInfo = getApplication().getSharedPreferences("setting", 0);
        id = loginInfo.getString("ID", "null");

        Button bestrec_btn = (Button) findViewById(R.id.bestrec_btn);

       bestrec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclist.clear();

                String url = getString(R.string.server_address) + "bestlist.php";
                ContentValues values = new ContentValues();
                NetworkTask networkTask = new NetworkTask(url, values);
                String result;

                try {
                    result =  networkTask.execute().get();
                    JSONObject json = new JSONObject(result);
                    JSONArray jsonarray = json.getJSONArray("bestlist");
                    for(int i=0; i<10; i++) {
                        json = jsonarray.getJSONObject(i);
                        reclist.add(new BookItem(R.drawable.noimagebook, json.getString("title"), json.getString("author"),
                                json.getString("company"), json.getString("date_rel"), json.getString("isbn")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
                if(reclist.size()==0) {
                    Toast.makeText(getApplicationContext(), "추천할 책이 없어요!\n독후감을 입력해주세요", Toast.LENGTH_LONG).show();
                }
                setAdapter();
            }
        });

        reclist = new ArrayList<BookItem>();

        String url = getString(R.string.server_address) + "reclist.php";
        ContentValues values = new ContentValues();
        values.put("id", id);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;

        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            JSONArray jsonarray = json.getJSONArray("reclist");
            int length = jsonarray.length();
            if(length>10)
                length = 10;
            for(int i=0; i<length; i++) {
                json = jsonarray.getJSONObject(i);
                reclist.add(new BookItem(R.drawable.noimagebook, json.getString("title"), json.getString("author"),
                        json.getString("company"), json.getString("date_rel"), json.getString("isbn")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }

        if(reclist.size()==0) {
            Toast.makeText(getApplicationContext(), "추천할 책이 없어요!", Toast.LENGTH_LONG).show();
        }

        setAdapter();

    }

    private void setAdapter(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_recbook);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecListActivity.this));
        recAdapter = new RecyclerviewAdapter(getApplicationContext(), reclist, null, false, true);
        recyclerView.setAdapter(recAdapter);
    }
}
