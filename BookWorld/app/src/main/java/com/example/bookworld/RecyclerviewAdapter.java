package com.example.bookworld;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {

    ArrayList<BookItem> items;
    Context context;
    boolean editicon;
    boolean goBookinfo;
    String id;

    public RecyclerviewAdapter(Context context, ArrayList<BookItem> items, String id, boolean editicon, boolean goBookinfo) {
        this.id = id;
        this.context = context;
        this.items = items;
        this.editicon = editicon;
        this.goBookinfo = goBookinfo;
    }

    @NonNull
    @Override //ViewHolder가 초기화될 때 혹은 ViewHolder를 초기화할 때 실행되는 메서드
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_mybook,parent,false);
        //Context를 부모로부터 받아와서 받은 Context를 기반으로 LayoutInflater를 생성,
        //생성된 LayoutInflater로 어떤 Layout을 가져와서 어떻게 View를 그릴지 결정
        MyViewHolder holder = new MyViewHolder(v);//View 생성 후, 이 View를 관리하기 위한 ViewHolder를 생성
        return holder;//생성된 ViewHolder를 OnBindViewHolder로 넘겨줌
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String endisbn = items.get(position).getBookIsbn().substring(10,13);
        String url = context.getString(R.string.kyobo_address) + endisbn + "/l" + items.get(position).getBookIsbn() + ".jpg";
        Glide.with(context).load(url).error(R.drawable.noimagebook).into(holder.imageView);
        holder.textView1.setText(items.get(position).getBookname());
        holder.textView2.setText(items.get(position).getBookauthor());
        holder.textView3.setText(items.get(position).getBookcompany());
        holder.textView4.setText(items.get(position).getBookdate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView1, textView2, textView3, textView4;
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
                public void onClick(View v) { //userid/edid로 독후감db에 있는 책의 isbn을 통해 책정보를 가져와 보여준다.

                    int pos = getAdapterPosition();
                    BookItem item = items.get(pos);

                    if(goBookinfo == true){
                        Intent infointent = new Intent(context, BookInfoActivity.class);
                        infointent.putExtra("isbn", item.getBookIsbn());
                        context.startActivity(infointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        //context.startActivity(infointent);

                    }else {
                        Intent intent = new Intent(context, MyreportActivity.class);
                        intent.putExtra("editicon", editicon);
                        //isbn을 report page로 넘겨준다.
                        intent.putExtra("name", item.getBookname());
                        intent.putExtra("author", item.getBookauthor());
                        intent.putExtra("company", item.getBookcompany());
                        intent.putExtra("isbn", item.getBookIsbn());
                        intent.putExtra("id", id);
                        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        //context.startActivity(intent);//가 팔로워report에선 안 돌아갔음
                        //likelist처럼 bookitem을 기반으로 한 recyclerviewadapter를 새로 만들어야 할 듯
                    }

                }
            });

        }
    }
}



class BookItem {

    //gridview에서 보여줄 부분
    //가져오는데 필요한 부분을 추후에 추가한다.
    public int bookimg;
    public String bookname;
    public String bookauthor;
    public String bookcompany;
    public String bookdate;
    public String isbn;

    public BookItem(int img, String txt1, String txt2, String txt3, String txt4, String isbn) {
        bookimg = img;
        bookname = txt1;
        bookauthor = txt2;
        bookcompany = txt3;
        bookdate = txt4;
        this.isbn = isbn;
    }

    public int getBookimg() {
        return bookimg;
    }

    public String getBookname() {
        return bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public String getBookcompany() {
        return bookcompany;
    }

    public String getBookdate() {
        return bookdate;
    }

    public String getBookIsbn() {return isbn;}

}

