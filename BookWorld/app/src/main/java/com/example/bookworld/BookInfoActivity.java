package com.example.bookworld;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BookInfoActivity extends AppCompatActivity {

    TextView tv, book_intro, count, txt1, txt2, txt3, txt4, txt5, txt6, txt7;
    ImageView iv;
    RatingBar rv;
    Button re_search;
    Button re_write;
    ToggleButton toggle_like;

    int  rpt_num;
    double star;
    String name, author, company, date, intro, category, content, mood, isbn, id;
    Bundle info;
    Boolean is_like;
    Boolean is_uploaded;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_bookinfo);

        iv = findViewById(R.id.bookimg_report);
        count = findViewById(R.id.count_review);
        rv = findViewById(R.id.ratingbar_book);
        book_intro = findViewById(R.id.book_intro);
        toggle_like = findViewById(R.id.favor_toggle);
        txt1 = findViewById(R.id.txt1_book);
        txt2 = findViewById(R.id.txt2_book);
        txt3 = findViewById(R.id.txt3_book);
        txt4 = findViewById(R.id.txt4_book);
        txt5 = findViewById(R.id.txt5_book);
        txt6 = findViewById(R.id.txt6_book);
        txt7 = findViewById(R.id.txt7_book);

        //fragment에서 넘어온 정보
        info = getIntent().getExtras();
        isbn = info.getString("isbn");
        SharedPreferences loginInfo  = getSharedPreferences("setting", 0);
        id = loginInfo.getString("ID", "null");

        String url = getString(R.string.server_address) + "bookinfo.php";
        ContentValues values = new ContentValues();
        values.put("isbn", isbn);
        values.put("id", id);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;

        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            Boolean iserror = json.getBoolean("error");
            if(iserror) {
                String type = json.getString("type");
                if(type.equals("existence")) {
                    Toast.makeText(this, "책이 존재하지 않아요.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            name = json.getString("title");
            author = json.getString("author");
            company = json.getString("company");
            date = json.getString("date_rel");
            category = json.getString("category");
            intro = json.getString("intro");
            star = json.getDouble("star_av");
            rpt_num = json.getInt("rpt_num");
            mood= json.getString("mood");
            is_like = json.getBoolean("like");
            is_uploaded = json.getBoolean("isuploaded");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }//서버,json 문제 예외처리들


        MoodCategory moodcategory = new MoodCategory(mood);

        String endisbn = isbn.substring(10,13);
        String bookurl = getString(R.string.kyobo_address) + endisbn + "/l" + isbn + ".jpg";
        Glide.with(this).load(bookurl).error(R.drawable.noimagebook).into(iv);

        txt1.setText(name);
        txt2.setText(author);
        txt3.setText(company);
        txt4.setText(date);
        txt5.setText(category);
        txt6.setText(moodcategory.getMood1());
        txt7.setText(moodcategory.getMood2());
        rv.setRating(new Float(star));
        book_intro.setText(intro);
        count.setText(String.valueOf(rpt_num)); //나중에 독후감 계산해서 받아와야 함

        if(is_uploaded) {
            toggle_like.setVisibility(View.INVISIBLE);
        }else  {
            toggle_like.setVisibility(View.VISIBLE);
            toggle_like.setChecked(is_like);
        }



        re_search = (Button) findViewById(R.id.review_search);
        re_write = (Button) findViewById(R.id.review_write);
    }



    public void to_like(View view)  {
        String url = getString(R.string.server_address) + "tolike.php";
        ContentValues values = new ContentValues();
        values.put("isbn", isbn);
        values.put("id", id);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;

        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            result = json.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if(result.equals("add")) {
            Toast.makeText(getApplicationContext(), "찜목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
        }else if(result.equals("delete")) {
            Toast.makeText(getApplicationContext(), "찜목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void toReview(View v) {
        Intent intent = new Intent(getApplicationContext(), BookInfoReviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("isbn", isbn);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void toPost(View v) {
        if(is_uploaded) {
            Toast.makeText(getApplicationContext(), "이미 독후감이 등록된 책입니다.", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getApplicationContext(), ReporteditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("author", author);
        bundle.putString("company", company);
        bundle.putString("isbn", isbn);
        intent.putExtra("bookinfo", bundle);
        intent.putExtra("is_changing", false);
        startActivity(intent);
        finish();
    }

}
