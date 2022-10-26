package com.example.bookworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Menu3Fragment extends Fragment {

    Button btn_mood, btn_barcode, btn_search, rec_btn;
    Intent intent, intent_barcode, intent_search;
    View view;

    //search page
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu3, container, false);
        btn_mood = (Button) view.findViewById(R.id.mood_btn);
        rec_btn = (Button) view.findViewById(R.id.rec_btn);
        btn_search = (Button) view.findViewById(R.id.search_btn);


        btn_mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(view.getContext(), MoodCategoryActivity.class);
                startActivity(intent);
            }
        });

        btn_barcode = (Button) view.findViewById(R.id.barcode_btn);

        btn_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_barcode = new Intent(view.getContext(), BarcodeActivity.class);
                startActivity(intent_barcode);
            }
        });

        rec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(view.getContext(), RecListActivity.class);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_search = new Intent(view.getContext(), SearchResultActivity.class);
                startActivity(intent_search);
            }
        });


   return view;
    }

}