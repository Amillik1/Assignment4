package com.example.app4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        int score = getIntent().getIntExtra("Score", -1);
        TextView score1 = findViewById(R.id.score1);
        score1.setText(Integer.toString(score));

    }

    //Called from clicking the button
    public void goBack(View view){

        Intent intent = new Intent(ActivityTwo.this, MainActivity.class);
        startActivity(intent);
    }
}
