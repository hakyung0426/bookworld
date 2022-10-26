package com.example.bookworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;

public class BugView extends View  {


    Bitmap worm;
    int xPos, yPos;
    int xdis = 1;
    int ydis = 1;
    int viewWidth, viewHeight, flag;
    Random random = new Random();

    public BugView(Context context) {
        super(context);
        flag = 0;
    }

    public BugView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();

        if(flag == 0) {
            xPos = random.nextInt(viewWidth);
            yPos =  random.nextInt(viewHeight);
            flag=1;
        }



        if (xPos < 0 - worm.getWidth()/2 || xPos > viewWidth-worm.getWidth()/2)
            xdis = -xdis;
        if (yPos < 0 - worm.getHeight()/2 || yPos > viewHeight-worm.getHeight()/2)
            ydis = -ydis;

        // 이미지의 좌표를 갱신한다.
        xPos += xdis;
        yPos += ydis;
//        int gap = random.nextInt(100);
//        Log.d("gap", String.valueOf(gap));
//        switch(gap) {
//            case 0 : {
//                xPos -= 5;
//                yPos -= 5;
//            }
//            case 1 : {
//                xPos += 5;
//                yPos += 5;
//            }
//            case 2 : {
//                xPos += 5;
//                yPos -= 5;
//            }
//            case 3 : {
//                xPos -= 5;
//                yPos += 5;
//            }
//            case 4: {
//                xPos -= 5;
//            }
//            case 5 : {
//                xPos += 5;
//            }
//            case 6 : {
//                yPos += 5;
//            }
//            case 7 : {
//                yPos -= 5;
//            }
//        }


        canvas.drawBitmap(worm, xPos, yPos, null);

        invalidate();
    }


    public void setWorm(Bitmap bitmap) {
        worm = bitmap;
    }
}
