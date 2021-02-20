package com.example.meefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.steelkiwi.library.SlidingSquareLoaderView;

import java.util.Objects;

import static android.view.View.ROTATION;

public class LoginActivity extends AppCompatActivity {

    private Button callSignUp, login_btn;
    private ImageView image;
    private CheckBox remember;
    private TextView logoText, sloganText;
    private EditText memail, mpassword;
    SlidingSquareLoaderView spinner;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_text);
        memail = findViewById(R.id.Login_Email);
        mpassword = findViewById(R.id.Login_Password);
        login_btn = findViewById(R.id.login_btn);
        remember=findViewById(R.id.check_login);
        mStore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
        }else if(checkbox.equals("false"))
        {
            Toast.makeText(this, "Please Sign In", Toast.LENGTH_SHORT).show();
        }

       // spinner = (SlidingSquareLoaderView)findViewById(R.id.view);
        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(LoginActivity.this, R.id.Login_Email, RegexTemplate.NOT_EMPTY, R.string.invalid_email);
        awesomeValidation.addValidation(LoginActivity.this, R.id.Login_Password, ".{6,}", R.string.invalid_pass);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUPActivity.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(sloganText, "logo_desc");
                pairs[3] = new Pair<View, String>(memail, "username_tran");
                pairs[4] = new Pair<View, String>(mpassword, "password_tran");
                pairs[5] = new Pair<View, String>(login_btn, "button_tran");
                pairs[6] = new Pair<View, String>(callSignUp, "login_signup_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }

            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(awesomeValidation.validate()) {
                 final String userEnteredEmail = memail.getText().toString().trim();
                 final String userEnteredPassword = mpassword.getText().toString().trim();
                 if(!userEnteredEmail.isEmpty() && !userEnteredPassword.isEmpty())
                 {
                     mAuth.signInWithEmailAndPassword(userEnteredEmail,userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful())
                             {
                                 Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                 Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                 startActivity(intent);
                             }
                             else
                             {
                                 Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                             }

                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     });

                 }
                 else
                 {
                     Toast.makeText(LoginActivity.this, "Please fill Empty field", Toast.LENGTH_SHORT).show();
                 }
                /* DatabaseReference reference=FirebaseDatabase.getInstance().getReference("user");
                 Query checkUser =reference.orderByChild("username").equalTo(userEnteredUsername);

                 checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists())
                       {
                           email.setError(null);
                           email.setErrorEnabled(false);
                           String passwordFromDB=snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                           if(passwordFromDB.equals(userEnteredPassword))
                           {
                               password.setError(null);
                               password.setErrorEnabled(false);

                              /* String nameFromDB=snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                               String UsernameFromDB=snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                               String phoneNoFromDB=snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                               String emailFromDB=snapshot.child(userEnteredUsername).child("email").getValue(String.class);*/
         //                      spinner.setVisibility(View.VISIBLE);
                               /*Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                               Toast.makeText(LoginActivity.this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show();
                               startActivity(intent);*/
           //                    spinner.setVisibility(View.GONE);
                               /*intent.putExtra("name",nameFromDB);
                               intent.putExtra("username",UsernameFromDB);
                               intent.putExtra("email",emailFromDB);
                               intent.putExtra("phoneNo",phoneNoFromDB);
                               intent.putExtra("password",passwordFromDB);*/
                         /*  }
                           else
                           {
                              password.setError("Wrong Password");
                              password.requestFocus();
                           }
                       }
                       else
                       {
                           email.setError("No such User Exist");
                           email.requestFocus();
                       }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });*/
             }
             else
             {
                 Toast.makeText(LoginActivity.this, "Invalid Email-Id or Password", Toast.LENGTH_SHORT).show();
             }

            }
        });
      remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(buttonView.isChecked())
              {
                  SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                  SharedPreferences.Editor editor=preferences.edit();
                  editor.putString("remember","true");
                  editor.apply();
                  Toast.makeText(LoginActivity.this, "Checked", Toast.LENGTH_SHORT).show();
              }else if(!buttonView.isChecked())
              {
                  SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                  SharedPreferences.Editor editor=preferences.edit();
                  editor.putString("remember","false");
                  editor.apply();
                  Toast.makeText(LoginActivity.this, "UnChecked", Toast.LENGTH_SHORT).show();
              }

          }
      });



    /*private void isUser()
    {
      String userEnteredUsername=username.getEditText().getText().toString().trim();
        String userEnteredPassword=password.getEditText().getText().toString().trim();


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");

        Query checkUser=reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError("No such User exist");
                    username.requestFocus();
                } else {
                    username.setError(null);
                    username.setErrorEnabled(false);

                   String passwordFromDB=snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                   if(passwordFromDB.equals(userEnteredPassword))
                   {
                       username.setError(null);
                       username.setErrorEnabled(false);

                       String nameFromDB=snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                       String usernameFromDB=snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                       String phoneNoFromDB=snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                       String emailFromDB=snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                       Intent intent=new Intent(getApplicationContext(),UserProfile.class);

                       intent.putExtra("name",nameFromDB);
                       intent.putExtra("username",usernameFromDB);
                       intent.putExtra("email",emailFromDB);
                       intent.putExtra("PhoneNo",phoneNoFromDB);
                       intent.putExtra("password",passwordFromDB);

                       startActivity(intent);
                   }
                   else
                   {
                     password.setError("Wrong Password");
                     password.requestFocus();
                   }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }*/

    }
}