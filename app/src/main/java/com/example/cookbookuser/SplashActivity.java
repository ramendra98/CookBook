package com.example.cookbookuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;



public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN=5000;
Animation topAnim,buttomAnim;
ImageView img;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        //animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        buttomAnim=AnimationUtils.loadAnimation(this,R.anim.button_animation);

        img.setAnimation(topAnim);
        textView.setAnimation(buttomAnim);
        //splash start and go to new activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
        },SPLASH_SCREEN);
    }
}