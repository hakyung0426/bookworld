package com.example.bookworld;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Menu1Fragment extends Fragment {

    ArrayList<BookItem> mybooklist;
    RecyclerView recyclerView;
    RecyclerviewAdapter mybookAdapter;
    FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_menu1, container, false);

        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerview_mybook);
        fab = viewGroup.findViewById(R.id.fab);
        final MainActivity activity = (MainActivity) getActivity();

        fab.setOnClickListener(new View.OnClickListener() { //글쓰기 버튼
            @Override


            public void onClick(View v) { //search-title로 이동
                Intent intent = new Intent(viewGroup.getContext(), SearchResultActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences loginInfo  = viewGroup.getContext().getSharedPreferences("setting", 0);
        String id = loginInfo.getString("ID", "null");

        mybooklist = new ArrayList<BookItem>();

        String url = getString(R.string.server_address) + "myreportlist.php";
        ContentValues values = new ContentValues();
        values.put("id", id);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;
        Boolean iserror;


        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            iserror = json.getBoolean("error");
            JSONArray jsonarray = json.getJSONArray("booklist");
            for(int i = jsonarray.length()-1; i >=0; i--) {
                json = jsonarray.getJSONObject(i);
                mybooklist.add(new BookItem(R.drawable.noimagebook, json.getString("title"), json.getString("author"),
                        json.getString("company"), json.getString("date_rel"), json.getString("isbn")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(viewGroup.getContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return viewGroup;
        }//서버,json 문제 예외처리들



        if(iserror) {
            Toast.makeText(viewGroup.getContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return viewGroup;//mysql 문제
        }else if(mybooklist.size() ==0) {
            Toast.makeText(viewGroup.getContext(), "등록한 독후감이 없습니다", Toast.LENGTH_LONG).show();
            return viewGroup;
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        mybookAdapter = new RecyclerviewAdapter(viewGroup.getContext(), mybooklist, id, true, false);
        recyclerView.setAdapter(mybookAdapter);

        return viewGroup;
    }
}