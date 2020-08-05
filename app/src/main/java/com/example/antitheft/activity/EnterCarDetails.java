package com.example.antitheft.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.antitheft.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
//activity for entering car details
public class EnterCarDetails extends AppCompatActivity {

    TextInputEditText etNum,etModel;
    Button btnAdd;
    FirebaseAuth auth;
    DatabaseReference carRef;
    String uid,name,pin,district,phone;
    public static String key;
    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_car_details);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        etNum=findViewById(R.id.et_number);
        etModel=findViewById(R.id.et_model);
        btnAdd=findViewById(R.id.btn_details_submit);

        carRef=FirebaseDatabase.getInstance().getReference();

        auth=FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();
        progressDialog=new ProgressDialog(this);




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading....");
                progressDialog.show();
                saveToDatabase();
            }
        });

    }
//save car number and model to database
    private void saveToDatabase()
    {
        String saveDate;

        String num=etNum.getText().toString().trim();
        String model=etModel.getText().toString().trim();

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy");
        saveDate=sdfDate.format(calendar.getTime());


        if(TextUtils.isEmpty(num) || TextUtils.isEmpty(model))
        {
            Toast.makeText(this, "Enter all credentials", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> carDetails=new HashMap<>();
            carDetails.put("name",name);
            carDetails.put("district",district);
            carDetails.put("pin",pin);
            carDetails.put("date",saveDate);
            carDetails.put("car number",num);
            carDetails.put("model",model);
            carDetails.put("phone",phone);

            carRef.child("Details").child(uid).push().setValue(carDetails)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                //key ?????
                                progressDialog.dismiss();
                                Toast.makeText(EnterCarDetails.this, "Data stored.", Toast.LENGTH_SHORT).show();
                                etModel.setText("");
                                etNum.setText("");
                            }
                            else {
                                Toast.makeText(EnterCarDetails.this, "Data not store.. please try again later..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }
//retreiving mechanic details so as to store all the required fields together
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth1;
        auth1=FirebaseAuth.getInstance();
        DatabaseReference reference;
        reference=FirebaseDatabase.getInstance().getReference();
        String id;
        id=auth1.getCurrentUser().getUid();
        reference.child("Mechanic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(id)) {
                    name = snapshot.child(id).child("name").getValue().toString();
                    district = snapshot.child(id).child("district").getValue().toString();
                    pin = snapshot.child(id).child("postal code").getValue().toString();
                    phone = snapshot.child(id).child("phone").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}