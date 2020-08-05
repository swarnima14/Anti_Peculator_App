package com.example.antitheft.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.antitheft.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//fragment for authenticating mechanic where user enters contact number
public class Phone extends Fragment {
    TextInputLayout textInputLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_phone, container, false);
        textInputLayout = view.findViewById(R.id.tilPhoneNumber);
        Button submit = view.findViewById(R.id.btnGetCode);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phn_no = textInputLayout.getEditText().getText().toString();
                if (phn_no.length() != 10) {
                    textInputLayout.setError("Valid number is required");
                    textInputLayout.requestFocus();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenumber", phn_no);
                    VerifyFragment verifyFragment = new VerifyFragment();
                    verifyFragment.setArguments(bundle);
                    FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();
                    fr.replace(R.id.container, verifyFragment);
                    fr.addToBackStack(null).commit();
                }
            }
        });



        return view;
    }
}