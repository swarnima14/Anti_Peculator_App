package com.example.antitheft.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.antitheft.R;

import com.example.antitheft.adapter.DetailsAdapter;
import com.example.antitheft.model.Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//activity for displaying car details
public class CarDetails extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference reference;
    ArrayList<Details> list;
    DetailsAdapter adapter;
    Toolbar toolbar;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        recyclerView=findViewById(R.id.carList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        reference= FirebaseDatabase.getInstance().getReference().child("Details");
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        //fetching values from database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Details>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {


                    for(DataSnapshot ds:dataSnapshot1.getChildren())
                    {

                        String carNum=ds.child("car number").getValue().toString();
                        String district=ds.child("district").getValue().toString();
                        String name=ds.child("name").getValue().toString();
                        String pin=ds.child("pin").getValue().toString();
                        String phone=ds.child("phone").getValue().toString();
                        Details details=new Details(name,district,pin,phone,carNum);
                        list.add(details);

                    }

                }
               adapter = new DetailsAdapter(CarDetails.this,list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CarDetails.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
//implemented search option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}