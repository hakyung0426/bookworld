package com.example.bookworld;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Menu4Fragment extends Fragment {

        ArrayList<BookItem> mylikelist;
        RecyclerView recyclerView;
        LikeRvAdapter likebookAdapter;
        TextView mp_userid;

        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_menu4, container, false);
            //사용자id 받아와서 보여주기
            SharedPreferences loginInfo = viewGroup.getContext().getSharedPreferences("setting", 0);
            String id = loginInfo.getString("ID", "안됨");
            mp_userid = (TextView) viewGroup.findViewById(R.id.mp_userid);
            mp_userid.setText(id);

            //팔로우목록 버튼->id를 키로 FLWDB에서 팔로우목록 불러와서 창 전환
            Button follow = (Button)viewGroup.findViewById(R.id.fwlist_btn);

            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FollowListActivity.class);
                    startActivity(intent);}
            });

            //찜목록 스크롤
            mylikelist = new ArrayList<BookItem>();

            String url = getString(R.string.server_address) + "likelist.php";
            ContentValues values = new ContentValues();
            values.put("id", id);
            NetworkTask networkTask = new NetworkTask(url, values);
            String result;
            Boolean iserror;

            try {
                result =  networkTask.execute().get();
                JSONObject json = new JSONObject(result);
                iserror = json.getBoolean("error");
                JSONArray jsonarray = json.getJSONArray("likelist");
                for(int i = 0;i<jsonarray.length();i++) {
                    json = jsonarray.getJSONObject(i);
                    mylikelist.add(new BookItem(R.drawable.noimagebook, json.getString("title"), json.getString("author"),
                            json.getString("company"), json.getString("date_rel"), json.getString("isbn")));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(viewGroup.getContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                return viewGroup;
            }//서버,json 문제 예외처리들


            recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerview_likebook);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
            likebookAdapter = new LikeRvAdapter(viewGroup.getContext(), mylikelist);
            recyclerView.setAdapter(likebookAdapter);

            //삭제버튼 누르면 userid로 likedb가서 해당 isbn 삭제

            return viewGroup;
        }
    }

