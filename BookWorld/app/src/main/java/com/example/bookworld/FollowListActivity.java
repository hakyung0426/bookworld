package com.example.bookworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        ArrayList<Followid> followlist;
        RecyclerView recyclerView;
        FollowRvAdapter followAdapter;

        followlist = new ArrayList<Followid>();
        SharedPreferences loginInfo  = getSharedPreferences("setting", 0);
        String id = loginInfo.getString("ID", "null");


        String url = getString(R.string.server_address) + "followlist.php";
        ContentValues values = new ContentValues();
        values.put("id", id);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;

        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            JSONArray jsonarray = json.getJSONArray("followlist");
            for(int i = 0; i<jsonarray.length() ; i++) {
                String ed_id = jsonarray.getString(i);
                followlist.add(new Followid(ed_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }//서버,json 문제 예외처리들


        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_follow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FollowListActivity.this));
        followAdapter = new FollowRvAdapter(getApplicationContext(), followlist);
        recyclerView.setAdapter(followAdapter);

        //삭제버튼을 누르면 userid를 통해 flwdb로 가서 해당 edid를 삭제

    }
}
