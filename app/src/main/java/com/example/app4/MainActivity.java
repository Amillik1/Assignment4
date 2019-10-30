package com.example.app4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TargetView targetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        targetView = new TargetView(this);
        setContentView(targetView);
    }

    class TargetView extends SurfaceView implements Runnable{
        Thread targetThread = null;
        ArrayList<Target> targetArray = new ArrayList<Target>();
        int score = 0;
        int lives = 5;
        boolean running;

        public TargetView(Context context){
            super(context);
        }


        public void update(){
            //make new targets
            //move old targets
        }
        public void draw(){
            //draw background
            //draw lives
            //draw score
            for(Target i : targetArray){
                i.draw();
            }

        }

        public void breakTarget(Target target){
            score+=1;
            targetArray.remove(target);
        }

        public void loseTarget(Target target){
            lives -=1;
            targetArray.remove(target);
        }

        @Override
        public void run(){
            while (running){
                update();
                draw();
                //check if lost
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent){
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                //if tap on a target
                    //breakTarget(target)
            }
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
