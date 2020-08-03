package com.example.antitheft.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.antitheft.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PoliceLogin extends AppCompatActivity {

    TextInputLayout pass_lay, mail_lay;
    TextInputEditText etMail, etPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_login);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        pass_lay = findViewById(R.id.pass_lay);
        mail_lay = findViewById(R.id.mail_lay);
        etPass = findViewById(R.id.etPass);
        etMail = findViewById(R.id.etMail);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = etMail.getText().toString().trim();
                String pass = etPass.getText().toString().trim();


                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)) {

                    Toast.makeText(PoliceLogin.this, "Enter all credentials.", Toast.LENGTH_SHORT).show();
                } else if (!(mail.equals("abc@gmail.com")) || !(pass.equals("123"))) {
                    Toast.makeText(PoliceLogin.this, "Incorrect mail or password.", Toast.LENGTH_SHORT).show();
                } else {

                  startActivity(new Intent(PoliceLogin.this, CarDetails.class));
                }
            }
        });
    }
}