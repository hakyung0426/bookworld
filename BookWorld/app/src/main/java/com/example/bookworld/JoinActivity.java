package com.example.bookworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    EditText join_id;
    EditText join_pw;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_id= findViewById(R.id.join_id);
        join_pw= findViewById(R.id.join_pw);
        join_id.setFilters(new InputFilter[] {editFilter, new InputFilter.LengthFilter(10)});

        button = findViewById(R.id.joinsuc);
        button.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {

                String id = join_id.getText().toString();
                String password = join_pw.getText().toString();

                if (id.length() == 0 || password.length() == 0) {//아이디 비번 공백일 때
                    Toast.makeText(getApplicationContext(), "아이디, 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                } else if (password.length() != 4) {//비번이 4자리가 아닐 때
                    Toast.makeText(getApplicationContext(), "비밀번호를 4자리로 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                //아이디, 비번이 형식에 맞으면


                String url = getString(R.string.server_address)+"/register.php";
                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("password", password);
                NetworkTask networkTask = new NetworkTask(url, values);

                String result = "";  //서버 통해 얻어온 회원가입 결과 json 값이 될 예정
                String failure = "";
                Boolean iserror = true;  //서버에서 회원가입 성공 여부를 받아올 변수

                try {
                    result = networkTask.execute().get();
                    JSONObject json = new JSONObject(result);
                    iserror = json.getBoolean("error");
                    failure = json.getString("type");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(JoinActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }



                if(iserror) {
                    if(failure.equals("exist")) {
                        Toast.makeText(JoinActivity.this, "이미 등록된 이름입니다.", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(JoinActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "가입을 축하합니다.", Toast.LENGTH_LONG).show();
                    finish();

                }
            }
        });
    }

    protected InputFilter editFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");        // 영문,숫자
            if(!pattern.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

}

