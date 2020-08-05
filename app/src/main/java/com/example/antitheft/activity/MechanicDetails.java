package com.example.antitheft.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.antitheft.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
//activity for entering mechanic details
public class MechanicDetails extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private FirebaseUser user;
    private String existingId;
    String phone;

    private Button btnSubmit;

    private TextInputEditText etName, etLocation, etPin;
    private TextInputLayout location_lay, name_lay, pin_lay;
    private ImageView image;
    //  private LottieAnimationView animationView;
    ProgressDialog progressDialog;

    String pin="not provided";
    String district="not provided";
    final int REQUEST_CODE_FINE_LOCATION = 12;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int UNIQUE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_details);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }


        rootRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        existingId=auth.getCurrentUser().getUid();

        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        name_lay = findViewById(R.id.name_lay);
        location_lay = findViewById(R.id.location_lay);
        btnSubmit = findViewById(R.id.btnSubmit);

        //image = findViewById(R.id.iv_dots);
        //animationView=findViewById(R.id.anim);
        etPin = findViewById(R.id.etPin);
        pin_lay = findViewById(R.id.pin_lay);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Entering Details....");



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {


                addToDatabase();
            }
        });


    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    private void addToDatabase() {
        String name = etName.getText().toString().trim();



        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(MechanicDetails.this, "Enter name", Toast.LENGTH_SHORT).show();
        }


        else {
            progressDialog.show();
            String uid = auth.getCurrentUser().getUid();
            HashMap<String, String> detailsMap = new HashMap<>();
            detailsMap.put("name", name);
            detailsMap.put("postal code", pin);
            detailsMap.put("district", district);
            detailsMap.put("phone",phone);


            rootRef.child("Mechanic").child(uid).setValue(detailsMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MechanicDetails.this, "You are registered successfully!!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(MechanicDetails.this,
                                        EnterCarDetails.class));
                            }
                        }
                    });
        }
    }
//to fetch location of user
    @SuppressLint("MissingPermission")
    private void getLocation() {

        if (ContextCompat.checkSelfPermission(MechanicDetails.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(MechanicDetails.this
                                    , Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            pin = addresses.get(0).getPostalCode();
                            district = addresses.get(0).getLocality();

                            etPin.setText(pin);
                            etLocation.setText(district);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

       else {
            ActivityCompat.requestPermissions(MechanicDetails.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, UNIQUE_REQUEST_CODE);
        }
    }


//check for permission available or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==UNIQUE_REQUEST_CODE)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getLocation();
                Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
            }
            else if(grantResults[0]==PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MechanicDetails.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This permission is important.");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MechanicDetails.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, UNIQUE_REQUEST_CODE);
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }



        }
    }
//if user already exists, the name field will be autofilled
    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference;
        reference=FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser;
        FirebaseAuth firebaseAuth;

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser !=null)
        {
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            reference.child("Mechanic").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(existingId))
                    {
                         phone=dataSnapshot.child(existingId).child("phone").getValue().toString();

                         if(dataSnapshot.child(existingId).hasChild("name")) {
                             String getName = dataSnapshot.child(existingId).child("name").getValue().toString();

                             etName.setText(getName.toString());
                         }

                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

     getLocation();

    }

}