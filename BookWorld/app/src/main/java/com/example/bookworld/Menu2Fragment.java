package com.example.bookworld;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Menu2Fragment extends Fragment {

    ViewGroup viewGroup;
    TextView worm_cdt;
    TextView fd_num, time_ticking;
    ImageView button;
    String id;
    int feed_num;
    BugView bugView;
    Bitmap happyworm, hungryworm;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_menu2, container, false);
        worm_cdt = (TextView)viewGroup.findViewById(R.id.worm_cdt);
        fd_num = viewGroup.findViewById(R.id.fd_num);
        button = viewGroup.findViewById(R.id.give_fd);
        time_ticking = viewGroup.findViewById(R.id.time_ticking);
        bugView = viewGroup.findViewById(R.id.bug_home);
        button.setTag("FEED");
        bugView.setOnDragListener(new DragListener());


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 3;
        happyworm =  BitmapFactory.decodeResource(getContext().getResources(), R.drawable.happyworm, options);
        hungryworm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.hungryworm, options);



        SharedPreferences loginInfo  = viewGroup.getContext().getSharedPreferences("setting", 0);
        id = loginInfo.getString("ID", "");


        String url = getString(R.string.server_address) + "bugstate.php";
        ContentValues values = new ContentValues();
        values.put("id", id);
        NetworkTask networkTask = new NetworkTask(url, values);
        String result;
        Boolean iserror;
        int second;


        try {
            result =  networkTask.execute().get();
            JSONObject json = new JSONObject(result);
            iserror = json.getBoolean("error");
            json= json.getJSONObject("user");
            feed_num = json.getInt("fd_num");
            second = json.getInt("time");
            fd_num.setText(String.valueOf(feed_num));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(viewGroup.getContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
            return viewGroup;
        }//??????,json ?????? ???????????????

        if(iserror) {  //user ????????? ?????? ??? ??????
            Toast.makeText(viewGroup.getContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
        }



        button.setOnLongClickListener(new LongClickListener() );


        if(second<1209600) {
            worm_cdt.setText("??????");
            BugTimer thread = new BugTimer(second);
            thread.start();
            bugView.setWorm(happyworm);
        }else {
            worm_cdt.setText("?????????");
            time_ticking.setText("???????????? ?????????\n??? ????????????");
            bugView.setWorm(hungryworm);
        }

        return viewGroup;
    }




    public class BugTimer extends CountDownTimer{
        int feedtime;

        public BugTimer(int second) {
            super((1209600-second)*1000, 1000);
            feedtime = 1209600-second;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            feedtime = feedtime - 1;
            if(feedtime <= 0) {
                bugView.setWorm(hungryworm);
                worm_cdt.setText("?????????");
                time_ticking.setText("???????????? ?????????\n??? ????????????");
                cancel();
                onFinish();
                return;
            }
            int day = feedtime/60/60/24;
            int hour = feedtime/60/60 - 24*day;
            int minute = feedtime/60-day*24*60 - hour*60;
            int sec = feedtime%60;
            time_ticking.setText(day + "??? " + hour+ "?????? " + minute+ "??? " + sec+"???"
                    + " ??????\n???????????? ????????????");
        }

        @Override
        public void onFinish() {
            return;
        }
    }


    private final class LongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

    class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
                switch(action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Toast.makeText(getContext(), "??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                        break;
                    case DragEvent.ACTION_DROP:
                        if(v ==bugView) {
                            Log.d("this", "here");
                            feedBug();
                        }
                        break;
                    default:
                        break;
                }
                return true;

        }
    }

    public void feedBug( ) {

        if (worm_cdt.getText().toString() == "??????") {
            Log.d("here", "ok");
            Toast.makeText(viewGroup.getContext(), "???????????? ?????? ????????????", Toast.LENGTH_SHORT).show();
            return;
        }else if(feed_num==0) {
            Toast.makeText(viewGroup.getContext(), "????????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            String url = getString(R.string.server_address)+"bugfeed.php";
            ContentValues values = new ContentValues();
            values.put("id", id);
            NetworkTask networkTask = new NetworkTask(url, values);
            networkTask.execute();
            Toast.makeText(viewGroup.getContext(), "???????????? ????????? ????????????", Toast.LENGTH_SHORT).show();
            worm_cdt.setText("??????");
            bugView.setWorm(happyworm);
            feed_num = feed_num-1;
            fd_num.setText(String.valueOf(feed_num));
            BugTimer thread = new BugTimer(0);
            thread.start();
        }

    }
}
