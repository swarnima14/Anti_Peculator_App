package com.example.antitheft.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.antitheft.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
        private Button next,materialBtn;
        FirebaseUser user;
        FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }
        next = findViewById(R.id.next_btn);
        materialBtn=findViewById(R.id.materialButton);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


        //will be added in last
        /*if(user!=null)
        {
            Intent intent=new Intent(WelcomeActivity.this,EnterCarDetails.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
//for mechanic
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user!=null)
                {
                    Intent intent=new Intent(WelcomeActivity.this,EnterCarDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
            }
        });
//for authority
        materialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, PoliceLogin.class));
            }
        });
    }
}