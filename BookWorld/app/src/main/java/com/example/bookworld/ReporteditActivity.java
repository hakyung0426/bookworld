package com.example.bookworld;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReporteditActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0; ////얘는 사진 업로드할때의 requestcode

    ActionBar actionBar;

    ImageView bookimg_report, image_report;
    TextView bookcontent_report;
    EditText content_report;
    RatingBar ratingBar;
    Spinner spinner1, spinner2;
    Switch switch1;
    MoodCategory selected_mood = new MoodCategory("0");
    Bitmap selectedimg;
    int photoresultCode; ///flags:myreport에서 왔다는 뜻->수정해야한다.
    String id, isbn;
    Boolean check = true;
    NetworkTask networkTask;
    int mood1, mood2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportedit);

        actionBar = getSupportActionBar();
        bookimg_report = findViewById(R.id.bookimg_report);
        image_report = findViewById(R.id.image_report);
        bookcontent_report = findViewById(R.id.bookinfo_reportedit);
        content_report = findViewById(R.id.content_report2);
        ratingBar = findViewById(R.id.ratingBar);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        switch1 = findViewById(R.id.switch1);

        SharedPreferences loginInfo = getSharedPreferences("setting", 0);
        id = loginInfo.getString("ID", "null");


        Bundle info = getIntent().getExtras();

        String name = info.getBundle("bookinfo").getString("name");
        String author = info.getBundle("bookinfo").getString("author");
        String company = info.getBundle("bookinfo").getString("company");
        isbn = info.getBundle("bookinfo").getString("isbn");
        String bookreportinfo = name + "\n" + author + "\n" + company;

        Boolean is_changing = info.getBoolean("is_changing");


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{MoodCategory.m0, MoodCategory.m1, MoodCategory.m2, MoodCategory.m3, MoodCategory.m4});
        spinner1.setAdapter(adapter1);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String[] moodlist = null;
            ArrayAdapter<String> adapter2;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        moodlist = new String[]{MoodCategory.m0};
                        mood1 = 0;
                        break;
                    case 1:
                        moodlist = new String[]{MoodCategory.m1_1, MoodCategory.m1_2, MoodCategory.m1_3, MoodCategory.m1_4};
                        mood1 = 1;
                        break;
                    case 2:
                        moodlist = new String[]{MoodCategory.m2_1, MoodCategory.m2_2, MoodCategory.m2_3, MoodCategory.m2_4};
                        mood1 = 2;
                        break;
                    case 3:
                        moodlist = new String[]{MoodCategory.m3_1, MoodCategory.m3_2, MoodCategory.m3_3, MoodCategory.m3_4};
                        mood1 = 3;
                        break;
                    case 4:
                        moodlist = new String[]{MoodCategory.m4_1, MoodCategory.m4_2, MoodCategory.m4_3, MoodCategory.m4_4};
                        mood1 = 4;
                        break;
                }
                adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, moodlist);
                spinner2.setAdapter(adapter2);
                mood2 = spinner2.getSelectedItemPosition()+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                moodlist = new String[]{MoodCategory.m0};
                adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, moodlist);
                spinner2.setAdapter(adapter2);
            }
        });


        //book 정보는 항상 가져와야함.
        String endisbn = isbn.substring(10, 13);
        String bookimgurl = getString(R.string.kyobo_address) + endisbn + "/l" + isbn + ".jpg";
        Glide.with(this).load(bookimgurl).error(R.drawable.noimagebook).into(bookimg_report);
        bookcontent_report.setText(bookreportinfo);

        //report 수정일때만 하게되는
        if (is_changing) {
            String content = info.getString("content");
            double star = info.getDouble("star");
            String mood = info.getString("mood");
            selected_mood = new MoodCategory(mood);
            int is_pub = info.getInt("is_pub");

            String reportimgUrl = getString(R.string.server_address) + "reportimg/" + id + isbn + ".jpeg";
            Glide.with(this).load(reportimgUrl).error(R.drawable.noimage).override(900, 1200).into(image_report);


            content_report.setText(content);
            ratingBar.setRating(new Float(star));
            if (is_pub == 1) {
                switch1.setChecked(false);
            } else switch1.setChecked(true);
            spinner1.setSelection(selected_mood.getUpper_mood());
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reportedit_action, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        postingReport();
        return true;
    }

    public void postingPhoto(View v) {
        //앨범에서 사진을 선택해서 업로드 하는 메소드

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.photoresultCode = resultCode;
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    selectedimg = BitmapFactory.decodeStream(in);
                    in.close();

                    Toast.makeText(this, "사진이 선택되었습니다", Toast.LENGTH_SHORT).show();
                    image_report.setImageBitmap(selectedimg);
                    Glide.with(this).load(selectedimg).into(image_report);
                } catch (Exception e) {
                    //예외처리
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void postingReport() {
        //사용자가 설정한 값들을 받아 db에 보내는 메소드
        int moodcategory = (mood1 - 1) * 4 + mood2;
        int is_pub;
        if (switch1.isChecked()) {
            is_pub = 0;
        } else is_pub = 1;

        if (content_report.getText().toString() == null)
            Toast.makeText(getApplicationContext(), "내용을 채워주세요.", Toast.LENGTH_LONG).show();
        else if (mood1 == 0)
            Toast.makeText(getApplicationContext(), "무드카테고리를 선택해주세요.", Toast.LENGTH_LONG).show();
        else {
            ContentValues values = new ContentValues();
            values.put("isbn", isbn);
            values.put("id", id);
            values.put("star", ratingBar.getRating());
            values.put("is_pub", is_pub);
            values.put("mood", String.valueOf(moodcategory));
            values.put("content", content_report.getText().toString());

            String url = getString(R.string.server_address) + "uploadreport.php";
            networkTask = new NetworkTask(url, values);

            boolean iserror;
            String errortype;

            try {
                String result = networkTask.execute().get();
                JSONObject json = new JSONObject(result);
                iserror = json.getBoolean("error");
                if (iserror) {
                    errortype = json.getString("type");
                    Toast.makeText(getApplicationContext(), "다시 시도해주세요 " + errortype + "오류", Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                return;
            }//서버,json 문제 예외처리들

            if (photoresultCode == RESULT_OK) uploadImage();
            //image has been selected.

            Intent result = new Intent();

            result.putExtra("star", ratingBar.getRating());
            result.putExtra("is_pub", is_pub);
            result.putExtra("mood", String.valueOf(moodcategory));
            result.putExtra("content", content_report.getText().toString());

            setResult(MyreportActivity.POST_SUCCESS, result);
            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            finish();

        }
    }


    private void uploadImage() {    //imageuploading 새로 했을 때만 실행되는 메소드

        ByteArrayOutputStream byteArrayOutputStreamObject;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        selectedimg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                // Printing uploading success message coming from server on android app.
            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("image_name", id + isbn);
                HashMapParams.put("image_data", ConvertImage);
                String FinalData = imageProcessClass.ImageHttpRequest(getString(R.string.server_address) + "uploadphoto.php", HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();

    }


    private class ImageProcessClass {
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;

                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                        new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(bufferedWriterDataFN(PData));
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

}