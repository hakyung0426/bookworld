package com.example.bookworld;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FollowRvAdapter extends RecyclerView.Adapter<FollowRvAdapter.MyViewHolder> {
    ArrayList<Followid> followid;
    Context context;

    public FollowRvAdapter(Context context, ArrayList<Followid> followid) {
        this.context = context;
        this.followid = followid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_follow, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView1.setText(followid.get(position).getEdname());
    }

    @Override
    public int getItemCount() {
        return followid.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        View v;
        ImageButton button;

        public MyViewHolder(@NonNull View flwView) {
            super(flwView);
            textView1 = flwView.findViewById(R.id.edid);
            v = flwView;



            flwView.setOnClickListener(new View.OnClickListener() { //클릭한 edid를 userpage에 넘겨줌
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Followid flwed_id = followid.get(pos);
                    Intent intent;
                    intent = new Intent(context, UserpageActivity.class);
                    intent.putExtra("edname", flwed_id.getEdname());
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });


            button = itemView.findViewById(R.id.flw_del);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Followid flwed_id = followid.get(pos);
                    SharedPreferences loginInfo  = context.getSharedPreferences("setting", 0);
                    String id = loginInfo.getString("ID", "null");
                    String url = context.getString(R.string.server_address) + "tofollow.php";
                    ContentValues values = new ContentValues();
                    values.put("ing_id", id);
                    values.put("ed_id", flwed_id.getEdname());
                    NetworkTask networkTask = new NetworkTask(url, values);
                    String result;
                    Boolean iserror;


                    try {
                        result =  networkTask.execute().get();
                        JSONObject json = new JSONObject(result);
                        iserror=json.getBoolean("error");
                        if(iserror) {
                            Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_LONG).show();}
                        else {result = json.getString("result");}
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(result.equals("add")) {
                        Toast.makeText(context, "팔로우 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    }else if(result.equals("delete")) {
                        Toast.makeText(context, "팔로우 목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}



class Followid {//gridview에서 보여줄 부분, 가져오는데 필요한 부분을 추후에 추가한다.
    public String edname;
    public Followid(String txt1) {
        edname = txt1;
    }
    public String getEdname() {
        return edname;
    }
}