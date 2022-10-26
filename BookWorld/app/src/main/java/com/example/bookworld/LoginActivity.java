package com.example.bookworld;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.regex.Pattern;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button login, join;
    EditText userid, userpw;
    SharedPreferences loginInfo;//로그인 정보 저장하는
    SharedPreferences.Editor editor;//입력된 loginInfo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_btn);
        join = (Button) findViewById(R.id.activity_join);
        userid = findViewById(R.id.id_edit);
        userpw = findViewById(R.id.pw_edit);
        userid.setFilters(new InputFilter[] {editFilter, new InputFilter.LengthFilter(10)});

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userid.length() == 0 || userpw.length() == 0) {//아이디 비번 공백일 때
                    Toast.makeText(getApplicationContext(), "아이디, 비밀번호를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                String id = userid.getText().toString();
                int password = Integer.parseInt(userpw.getText().toString());


                String url = getString(R.string.server_address )+ "login.php";
                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("password", password);

                NetworkTask networkTask = new NetworkTask(url, values);

                String result = "";  //서버 통해 얻어온 로그인 json 값이 될 예정
                Boolean iserror = true;  //서버에서 로그인 성공 여부를 받아올 변수

                try {
                    result =  networkTask.execute().get();
                    JSONObject json = new JSONObject(result);
                    iserror = json.getBoolean("error");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }//서버,json 문제 예외처리들



                if(iserror) {  //잘못된 정보를 입력하여 로그인 실패
                    Toast.makeText(getApplicationContext(), "잘못된 정보를 입력하였습니다.", Toast.LENGTH_SHORT).show();
                }else { //로그인 성공
                    loginInfo = getSharedPreferences("setting", 0);//"setting"이라는 이름으로 저장, 없으면 0이라는 value가 default
                    editor = loginInfo.edit();
                    editor.putString("ID", id);
                    editor.commit();//입력된 id, pw값을 sharedpreferences를 통해 기기에 저장하는 부분.

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Toast.makeText(getApplicationContext(), id + "님, 반갑습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
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

