
package com.example.bookworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MoodCategoryActivity extends AppCompatActivity {
    String category;
    Spinner spinner1, spinner2, spinner3;
    ArrayAdapter<String> adapter1, adapter2, adapter3;
    MoodCategory selected_mood  = new MoodCategory("0");
    int mood1, mood2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodcategory);

        spinner1 = findViewById(R.id.spinner_one);
        spinner2 = findViewById(R.id.spinner_two);
        spinner3 = findViewById(R.id.spinner_category);

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{MoodCategory.m0, MoodCategory.m1, MoodCategory.m2, MoodCategory.m3, MoodCategory.m4});
        spinner1.setAdapter(adapter1);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String[] moodlist = null;

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

        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"분야 없음", "소설", "시/에세이", "자기계발", "인문"});
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        category = "분야 없음";
                        break;
                    case 1:
                        category = "소설";
                        break;
                    case 2:
                        category = "시/에세이";
                        break;
                    case 3:
                        category = "자기계발";
                        break;
                    case 4:
                        category = "인문";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "분야 없음";
            }
        });

        Button button = (Button) findViewById(R.id.move_moodlist);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mood1 == 0){
                    Toast.makeText(getApplicationContext(), "무드카테고리를 선택해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MoodListActivity.class);
                    intent.putExtra("mood1", mood1);
                    intent.putExtra("mood2", mood2);
                    intent.putExtra("category", category);
                    startActivity(intent);
                }
            }
        });
    }
}
