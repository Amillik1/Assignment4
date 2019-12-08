package com.example.app4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TargetView targetView;
    int height;
    int width;
    int targetSize = 100;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        targetView = new TargetView(this);
        setContentView(targetView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    class TargetView extends SurfaceView implements Runnable{
        Thread targetThread = null;
        ArrayList<Target> targetArray = new ArrayList<Target>();
        int score = 0;
        int lives = 5;
        boolean running = true;
        SurfaceHolder ourHolder;
        Canvas canvas;
        Paint paint;
        Target currTarget;

        //take 2
        float tarX;
        float tarY;
        float speed = 10;
        boolean horizontal = true;
        boolean startLeftOrTop = true;
        boolean exists = false;

        public TargetView(Context context){
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
        }


        public void update(){
            if (exists){
                //move old targets
                if (horizontal){
                    if (startLeftOrTop) {
                        tarX += speed;
                    }else{
                        tarX -= speed;
                    }
                }else{
                    if(startLeftOrTop) {
                        tarY += speed;
                    }else{
                        tarY -= speed;
                    }
                }
                if(tarX < 0 || tarY < 0 || tarX > width || tarY > height){
                    lives -=1;
                    if (lives != 0) {
                        exists = false;
                    }
                }


            }else{
                //make new targets
                exists = true;
                if(rand.nextInt(2) == 0){
                    horizontal = true;
                }else{
                    horizontal = false;
                }
                if(rand.nextInt(2)== 0){
                    startLeftOrTop = true;
                }else{
                    startLeftOrTop = false;
                }

                if(horizontal && startLeftOrTop){
                    //Starting at the left side
                    tarX = 0;
                    tarY = rand.nextInt(height);
                }
                if(!horizontal && startLeftOrTop){
                    //Starting top
                    tarY = 0;
                    tarX = rand.nextInt(width);
                }
                if(horizontal && !startLeftOrTop){
                    //starting right
                    tarX = width;
                    tarY = rand.nextInt(height);
                }
                if(!horizontal && !startLeftOrTop){
                    tarY = height;
                    tarX = rand.nextInt(width);
                }
                speed +=1;
            }
        }
        public void draw(){
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                //draw background
                canvas.drawColor(Color.BLUE);

                //paint.setColor(Color.GREEN);
                //canvas.drawRect(0, 500, 800, 1000, paint);
                //draw lives
                paint.setColor(Color.GREEN);
                paint.setTextSize(36);
                String liveStr = "Lives: " + Integer.toString(lives);
                canvas.drawText(liveStr, 15, 36, paint);
                //draw score
                String scoreStr = "Score: " + Integer.toString(score);
                canvas.drawText(scoreStr, 15, 80, paint);
                /*for (Target i : targetArray) {
                    i.draw();
                }*/

                paint.setColor(Color.RED);
                canvas.drawCircle(tarX, tarY, targetSize, paint);

                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        /*public void breakTarget(Target target){
            score+=1;
            //targetArray.remove(target);
        }

        public void loseTarget(Target target){
            lives -=1;
            targetArray.remove(target);
        }*/

        @Override
        public void run(){
            while (running){
                update();
                draw();
                //check if lost
                if (lives == 0){
                    //change window to leaderboard screen
                    Intent intent = new Intent(MainActivity.this, ActivityTwo.class);
                    intent.putExtra("Score", score);
                    startActivity(intent);
                }

                try{
                    targetThread.sleep(20);

                }catch (InterruptedException e){

                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent){
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                //if tap on a target
                    //breakTarget(target)

                if(Math.hypot(Math.abs(motionEvent.getX() - tarX), Math.abs(motionEvent.getY() - tarY)) < targetSize){
                    score +=1;
                    exists = false;
                }

            }
            return true;
        }


        public void resume(){
            targetThread = new Thread(this);
            targetThread.start();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        targetView.resume();
    }
}
