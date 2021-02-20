package com.example.meefy;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Pair;
import android.view.View;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private  static  int SPLASH_SCREEN=4000;
    //variables
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo,salogan;
    SharedPreferences sp;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();

        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.botton_animation);

        //hooks
        image=findViewById(R.id.image);
        logo=findViewById(R.id.text);
        salogan=findViewById(R.id.text2);

        //setting animation
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        salogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(image,"logo_image");
                pairs[1]=new Pair<View,String>(logo,"logo_text");
               // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                }
            }
        },SPLASH_SCREEN);
    }
   /* @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
           startActivity(new Intent(MainActivity.this,HomeActivity.class));
           finish();
        }
    }*/
}