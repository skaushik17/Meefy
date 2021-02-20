package com.example.meefy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fullname ,email,phoneNo,password;
    TextView fullNameLabel,usernameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //Hooks
        fullname=findViewById(R.id.full_name_profile);
        email=findViewById(R.id.email_profile);
        phoneNo=findViewById(R.id.phone_no_profile);
        password=findViewById(R.id.password_profile);
        fullNameLabel=findViewById(R.id.fullname_field);
        usernameLabel=findViewById(R.id.username_field);

        //Show All data
        showAllUsersData();
    }
    private void showAllUsersData()
    {
        Intent intent=getIntent();
        String user_username=intent.getStringExtra("username");
        String user_name=intent.getStringExtra("name");
        String user_email=intent.getStringExtra("email");
        String user_phoneNo=intent.getStringExtra("phoneNo");
        String user_password=intent.getStringExtra("password");

        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullname.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phoneNo.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);

    }

}