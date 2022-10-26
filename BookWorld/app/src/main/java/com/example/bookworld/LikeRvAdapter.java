package com.example.bookworld;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LikeRvAdapter extends RecyclerView.Adapter<LikeRvAdapter.MyViewHolder> {

    ArrayList<BookItem> likeitems;
    Context context;


    View v;
    ImageButton delete_btn;


    public LikeRvAdapter(Context context, ArrayList<BookItem> likeitems) {
        this.context = context;
        this.likeitems = likeitems;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_like, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String endisbn = likeitems.get(position).getBookIsbn().substring(10, 13);
        String url = context.getString(R.string.kyobo_address) + endisbn + "/l" + likeitems.get(position).getBookIsbn() + ".jpg";
        Glide.with(context).load(url).error(R.drawable.noimagebook).into(holder.imageView);
        holder.textView1.setText(likeitems.get(position).getBookname());
        holder.textView2.setText(likeitems.get(position).getBookauthor());
        holder.textView3.setText(likeitems.get(position).getBookcompany());
        holder.textView4.setText(likeitems.get(position).getBookdate());
    }

    @Override
    public int getItemCount() {
        return likeitems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1, textView2, textView3, textView4;
        ImageButton button;
        View v;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bookimage);
            textView1 = itemView.findViewById(R.id.booktitle);
            textView2 = itemView.findViewById(R.id.bookauthor);
            textView3 = itemView.findViewById(R.id.bookcompany);
            textView4 = itemView.findViewById(R.id.bookdate);
            v = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    BookItem item = likeitems.get(pos);

                    Intent intent;
                    intent = new Intent(context, BookInfoActivity.class); //bookinfo로 넘어가야함

                    intent.putExtra("isbn", item.getBookIsbn());
                    context.startActivity(intent);
                }
            });

            button = itemView.findViewById(R.id.likeDel);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    BookItem likeitem = likeitems.get(pos);
                    SharedPreferences loginInfo  = context.getSharedPreferences("setting", 0);
                    String id = loginInfo.getString("ID", "null");
                    String url = context.getString(R.string.server_address) + "tolike.php";
                    ContentValues values = new ContentValues();
                    values.put("isbn", likeitem.getBookIsbn());
                    values.put("id", id);
                    NetworkTask networkTask = new NetworkTask(url, values);
                    String result;

                    try {
                        result =  networkTask.execute().get();
                        JSONObject json = new JSONObject(result);
                        result = json.getString("result");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(result.equals("add")) {
                        Toast.makeText(context, "찜목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    }else if(result.equals("delete")) {
                        Toast.makeText(context, "찜목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
