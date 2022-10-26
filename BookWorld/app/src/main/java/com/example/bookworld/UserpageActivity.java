package com.example.bookworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class UserpageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<BookItem> edbooklist;
    RecyclerviewAdapter edbookAdapter;
    String otheruser, id;
    ToggleButton toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);


        otheruser = getIntent().getStringExtra("edname");
        TextView flwedname = (TextView)findViewById(R.id.edid_page);
        flwedname.setText(otheruser);
        toggle = findViewById(R.id.userpage_flw);
        SharedPreferences loginInfo  = getSharedPreferences("setting", 0);
        id = loginInfo.getString("ID", "null");



        //edid인 getedname으로 독후감의 isbn을 불러오고, isbn으로 책정보 불러옴
        edbooklist = new ArrayList<>();


        String url = getString(R.string.server_address) + "userreportlist.php";
        ContentValues values = new ContentValues();
        values.put("id", otheruser);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;
        Boolean iserror;

        String url2 = getString(R.string.server_address) + "islikeuser.php";
        ContentValues values2 = new ContentValues();
        values2.put("ing_id", id);
        values2.put("ed_id", otheruser);
        NetworkTask networkTask2 = new NetworkTask(url2, values2);
        Boolean isfollowing;

        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            iserror = json.getBoolean("error");
            JSONArray jsonarray = json.getJSONArray("booklist");
            for(int i = 0; i< jsonarray.length(); i++) {
                json = jsonarray.getJSONObject(i);
                edbooklist.add(new BookItem(R.drawable.bookimg, json.getString("title"), json.getString("author"),
                        json.getString("company"), json.getString("date_rel"), json.getString("isbn")));
            }

            result = networkTask2.execute().get();
            json = new JSONObject(result);
            isfollowing = json.getBoolean("result");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }//서버,json 문제 예외처리들

        if(iserror) {
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;//mysql 문제
        }else if(edbooklist.size() ==0) {
            Toast.makeText(this, "등록한 독후감이 없습니다", Toast.LENGTH_LONG).show();
            return;
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_userpage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        edbookAdapter = new RecyclerviewAdapter(getApplicationContext(), edbooklist, otheruser, false, false);
        recyclerView.setAdapter(edbookAdapter);

        toggle.setChecked(isfollowing);

    }



    public void  to_follow(View v) {

        String url = getString(R.string.server_address) + "tofollow.php";
        ContentValues values = new ContentValues();
        values.put("ing_id", id);
        values.put("ed_id", otheruser);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;
        Boolean iserror;


        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            iserror=json.getBoolean("error");
            if(iserror) {
                Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_LONG).show();}
            else {result = json.getString("result");}
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if(result.equals("add")) {
            Toast.makeText(this, "팔로우 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
            toggle.setChecked(true);
        }else if(result.equals("delete")) {
            Toast.makeText(this, "팔로우 목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
            toggle.setChecked(false);
        }

    }
}
