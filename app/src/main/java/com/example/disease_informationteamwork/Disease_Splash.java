package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Disease_Splash extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    // onAnimationEnd Intent 부분에 메인으로 갈 부분 지정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease__splash);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        splashAnimation();
    }

    private void splashAnimation(){
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_appname);
        textView.startAnimation(textAnim);
        Animation imageAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_desease_image);
        imageView.startAnimation(imageAnim);

        imageAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(Disease_Splash.this, MapsActivity.class/*메인으로 가는 부분*/));
                overridePendingTransition(R.anim.anim_splash_show_main, R.anim.anim_splash_show_main_2);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }


        });
    }
}
