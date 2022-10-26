package com.example.bookworld;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class SearchResultActivity extends AppCompatActivity {
    ArrayList<BookItem> bookList;
    SearchView TextSearchKeyword;
    String result;

    RecyclerView recyclerView;
    RecyclerviewAdapter bookAdapter;
    NetworkTask networkTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        recyclerView = (RecyclerView) findViewById(R.id.searchbooklist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextSearchKeyword = (SearchView) findViewById(R.id.textsearch);
        TextSearchKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextSearchKeyword.setIconified(false);
            }
        });

        TextSearchKeyword.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bookList.clear();

                String url = getString(R.string.server_address) + "searchresult.php";
                ContentValues values = new ContentValues();
                values.put("searchkey", query);

                networkTask = new NetworkTask(url, values);
                showResult();

                return false;
            }
            @Override
            public boolean onQueryTextChange(String key) {
                return false;
            }
        });

        bookList = new ArrayList<>();

        ImageButton barcode = (ImageButton) findViewById(R.id.barcode);
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_barcode = new Intent(getApplicationContext(), BarcodeActivity.class);
                startActivity(intent_barcode);
            }
        });

    }


    private void showResult(){
        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            JSONArray jsonArray = json.getJSONArray("response");

            for(int i=0;i<jsonArray.length();i++){
                json = jsonArray.getJSONObject(i);
                bookList.add(new BookItem(R.drawable.bookimg, json.getString("title"), json.getString("author"),
                        json.getString("company"), json.getString("date_rel"), json.getString("isbn")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }

        bookAdapter = new RecyclerviewAdapter(this, bookList, null, false, true);
        recyclerView.setAdapter(bookAdapter);

    }

}
