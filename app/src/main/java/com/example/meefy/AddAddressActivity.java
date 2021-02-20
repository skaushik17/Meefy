package com.example.meefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mAddress;
    private EditText mCity;
    private EditText mCode;
    private EditText mNumber;
    private Button   mAddAddress;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mName=findViewById(R.id.ad_name);
        mAddress=findViewById(R.id.ad_address);
        mCity=findViewById(R.id.ad_city);
        mCode=findViewById(R.id.ad_code);
        mNumber=findViewById(R.id.ad_phone);
        mAddAddress=findViewById(R.id.ad_add_address);
        mStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        mAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name=mName.getText().toString().trim();
               String city=mCity.getText().toString().trim();
               String address=mAddress.getText().toString().trim();
               String code=mCode.getText().toString().trim();
               String number=mNumber.getText().toString().trim();
               String final_address="";
               if(!name.isEmpty())
               {
                   final_address+=name+", ";
               }
                if(!city.isEmpty())
                {
                    final_address+=city+", ";
                }
                if(!address.isEmpty())
                {
                    final_address+=address+", ";
                }
                if(!code.isEmpty())
                {
                    final_address+=code+", ";
                }
                if(!number.isEmpty())
                {
                    final_address+=number+", ";
                }
                Map<String,String> mMap=new HashMap<>();
                mMap.put("address",final_address);

                mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                        .collection("Address").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                      if(task.isSuccessful())
                      {
                          Toast.makeText(AddAddressActivity.this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                          finish();
                      }
                    }
                });

                    finish();
                 }
        });


    }
}