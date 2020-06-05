package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.coen268project.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_screen extends AppCompatActivity {
    private  static  int SPLASH_SCREEN =2500;

    ImageView imageView;
    TextView textView1;
    Animation top, bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.textView);



        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        imageView.setAnimation(top);
        textView1.setAnimation(bottom);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_screen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}
