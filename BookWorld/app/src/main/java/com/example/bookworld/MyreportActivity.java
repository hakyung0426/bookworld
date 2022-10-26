package com.example.bookworld;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;


public class MyreportActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    public static final int POST_SUCCESS = 1;
    TextView content_report, txt1, txt2, txt3, txt4, txt5, txt6, txt7;
    ImageView iv, image_report;
    RatingBar rv;
    ActionBar actionBar;

    int is_pub;
    String date, content, mood;
    Bundle info;
    double star;


    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_myreport);

        actionBar=getActionBar();
        iv = findViewById(R.id.bookimg_report);
        rv = findViewById(R.id.ratingbar_report);
        content_report = findViewById(R.id.content_myreport);
        image_report = findViewById(R.id.image_report);
        txt1 = findViewById(R.id.txt1_rpt);
        txt2 = findViewById(R.id.txt2_rpt);
        txt3 = findViewById(R.id.txt3_rpt);
        txt4 = findViewById(R.id.txt4_rpt);
        txt5 = findViewById(R.id.txt5_rpt);
        txt6 = findViewById(R.id.txt6_rpt);
        txt7 = findViewById(R.id.txt7_rpt);

        //fragment에서 받은 정보들 이곳에서 풀기
        info = getIntent().getExtras();
        String name = info.getString("name");
        String author = info.getString("author");
        String company = info.getString("company");
        String isbn = info.getString("isbn");
        String id = info.getString("id");



        String url = getString(R.string.server_address) + "myreport.php";
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("isbn", isbn);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;
        Boolean iserror = false;

        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            iserror = json.getBoolean("error");
            date = json.getString("date_sav").substring(0, 10);
            mood = json.getString("mood");
            is_pub = json.getInt("is_pub");
            star = json.getDouble("star");
            content = json.getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }//서버,json 문제 예외처리들


        if(iserror) {
            Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }//mysql 문제




        MoodCategory moodcat = new MoodCategory(mood);//무드 카테고리 숫자 -> string 변경
        String pub;//공개여부 boolean -> string으로 변경
        if(is_pub==1) {pub = "공개";}else pub = "비공개";

        String reportimgUrl = getString(R.string.server_address) + "reportimg/" + id + isbn + ".jpeg" ;
        Glide.with(this).load(reportimgUrl).error(R.drawable.noimage).override(900, 1200).into(image_report);


        txt1.setText(name);
        txt2.setText(author);
        txt3.setText(company);
        txt4.setText(date);
        txt5.setText(moodcat.getMood1()+",");
        txt6.setText(moodcat.getMood2());
        txt7.setText(pub);
        rv.setRating(new Float(star));
        content_report.setText(content);
        String endisbn = isbn.substring(10,13);
        String bookimgurl = getString(R.string.kyobo_address)+ endisbn + "/l" + isbn + ".jpg";
        Glide.with(this).load(bookimgurl).error(R.drawable.noimagebook).into(iv);


        //일단 report 이미지는 jpeg 파일만..
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean editicon = getIntent().getExtras().getBoolean("editicon");
        getMenuInflater().inflate(R.menu.menu_myreport_action, menu);
        if(editicon == true){//여기 조건을 조절하면 수정아이콘 없앨 수 있음
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //app bar에 있는 수정 눌렀을 시에 수정 ReportEdit으로 정보 전달하며 넘어가는 부분

        if(item.getItemId() == R.id.edit_report) {
            Intent intent = new Intent(getApplicationContext(), ReporteditActivity.class);
            intent.putExtra("bookinfo", info);
            intent.putExtra("content", content);
            intent.putExtra("star", star);
            intent.putExtra("mood", mood);
            intent.putExtra("is_pub", is_pub);
            intent.putExtra("is_changing", true);
            startActivityForResult(intent,REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(requestCode == REQUEST_CODE && resultCode == POST_SUCCESS) {
                MoodCategory moodcate = new MoodCategory(data.getStringExtra("mood"));
                txt5.setText(moodcate.getMood1()+",");
                txt6.setText(moodcate.getMood2());
                if(data.getIntExtra("is_pub", 0) == 0) {
                    txt7.setText("비공개");
                } else txt7.setText("공개");

                rv.setRating(data.getFloatExtra("star", 0));
                content_report.setText(data.getStringExtra("content"));
            }
        }
    }
}
