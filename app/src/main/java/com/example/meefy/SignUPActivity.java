package com.example.meefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Pattern;

public class SignUPActivity extends AppCompatActivity {
    /*private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");*/

    EditText regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);

        //Hooks to all xml elements in activity_sign_up.xml
        regName = findViewById(R.id.sig_name);
        regUsername = findViewById(R.id.sig_username);
        regEmail = findViewById(R.id.sig_email);
        regPhoneNo = findViewById(R.id.sig_phoneNo);
        regPassword = findViewById(R.id.sig_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);
        mStore=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        /*rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("user");*/

        AwesomeValidation awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(SignUPActivity.this,R.id.sig_name,RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(SignUPActivity.this,R.id.sig_username,RegexTemplate.NOT_EMPTY,R.string.invalid_username);
        awesomeValidation.addValidation(SignUPActivity.this,R.id.sig_email,Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(SignUPActivity.this,R.id.sig_phoneNo,"[5-9]{1}[0-9]{9}$",R.string.invalid_phoneNo);
        awesomeValidation.addValidation(SignUPActivity.this,R.id.sig_password,".{6,}",R.string.invalid_password);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(awesomeValidation.validate())
               {
                   String name = regName.getText().toString().trim();
                   String username = regUsername.getText().toString().trim();
                   String email = regEmail.getText().toString().trim();
                   String phoneNo = regPhoneNo.getText().toString().trim();
                   String password = regPassword.getText().toString().trim();
                   mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful())
                         {
                             Toast.makeText(SignUPActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                             Intent intent=new Intent(SignUPActivity.this,LoginActivity.class);
                             startActivity(intent);
                         }
                         else
                         {
                             Toast.makeText(SignUPActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                         }
                       }
                   });
                  /* UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
                   reference.child(username).setValue(helperClass);

                   Toast.makeText(SignUPActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                   Intent intent = new Intent(SignUPActivity.this, UserProfile.class);
                   startActivity(intent);*/
               }
               else
               {
                   Toast.makeText(SignUPActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
               }

            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUPActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

      /*  private Boolean validateName(){
            String val=regName.getEditText().getText().toString().trim();
            if(val.isEmpty())
            {
                regName.setError("Field cannot be Empty");
                return false;
            }
            else
            {
                regName.setError(null);
                regName.setErrorEnabled(false);
                return true;
            }
        }

        private Boolean validateUsername()
        {
            String val=regUsername.getEditText().getText().toString().trim();
            String noWhiteSpace="\\A\\w{4,20}\\z";
            if(val.isEmpty())
            {
                regUsername.setError("Field cannot be Empty");
                return false;
            }
            else if(val.length()>=15) {
                regUsername.setError("Username too long");
                return false;
            }
            else if(!val.matches(noWhiteSpace))
            {
                regUsername.setError("White spaces not allowed");
                return false;
            }
            else
            {
                regUsername.setError(null);
                regUsername.setErrorEnabled(false);
                return true;
            }
        }

        private Boolean validateEmail()
        {
            String val=regEmail.getEditText().getText().toString().trim();
            String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(val.isEmpty())
            {
                regEmail.setError("Field cannot be Empty");
                return false;
            }
            else if(!val.matches(emailPattern))
            {
                regEmail.setError("Invalid email address");
                return false;
            }
            else
            {
                regEmail.setError(null);
                return true;
            }
        }

        private Boolean validatePhoneNo()
        {
            String val=regName.getEditText().getText().toString().trim();
            if(val.isEmpty())
            {
                regName.setError("Field cannot be Empty");
                return false;
            }
            else
            {
                regName.setError(null);
                return true;
            }
        }

        private Boolean validatePassword()
        {
            String val=regPassword.getEditText().getText().toString().trim();
            String passwordVal="^"+
                    "(?=.*[a-zA-Z])"+
                    "(?=.*[@#$%^&+=])"+
                    "(?=\\s+$)"+
                    "{4,}"+
                    "$";
            if(val.isEmpty())
            {
                regPassword.setError("Field cannot be Empty");
                return false;
            }
            else if(!val.matches(passwordVal))
            {
                regPassword.setError("Password is too weak");
                return false;
            }
            else
            {
                regPassword.setError(null);
                return true;
            }
        }


    //save data Firebase on button click
     public void registerUser()
    {
        if(!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername())
        {
           return;
        }

            String name = regName.getEditText().getText().toString().trim();
            String username = regUsername.getEditText().getText().toString().trim();
            String email = regEmail.getEditText().getText().toString().trim();
            String phoneNo = regPhoneNo.getEditText().getText().toString().trim();
            String password = regPassword.getEditText().getText().toString().trim();

            UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
            reference.child(username).setValue(helperClass);

            Toast.makeText(SignUPActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUPActivity.this, UserProfile.class);
            startActivity(intent);
    }*/
}
