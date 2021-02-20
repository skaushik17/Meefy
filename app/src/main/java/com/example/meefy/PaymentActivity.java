package com.example.meefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    TextView mTotal;
    Button payBtn;
    double amount=0.0;
    String name="";
    String img_url="";
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //double amount=0.0;
        amount=getIntent().getDoubleExtra("amount",0.0);
        name=getIntent().getStringExtra("img_url");
        img_url=getIntent().getStringExtra("name");
        mStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_payment);
        mTotal=findViewById(R.id.textsub);
        payBtn=findViewById(R.id.pay_btn);
        mTotal.setText(amount+"");
        Checkout.preload((getApplicationContext()));
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
                //Toast.makeText(PaymentActivity.this, "payment Successfull", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void startPayment()
    {
        Checkout checkout=new Checkout();

        final Activity activity=PaymentActivity.this;

        try{
            JSONObject options=new JSONObject();
            options.put("name","Meefy");
            options.put("description","Refrence No. #123456");
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency","INR");
            //double total=Double.parseDouble(mAmountText.getText().toString());
            amount=amount*100;
            options.put("amount",amount);
            JSONObject preFill=new JSONObject();
            preFill.put("email","kumarsatender458@gmail.com");
            preFill.put("contact","8279818398");
            options.put("prefill",preFill);

            checkout.open(activity,options);
        }catch (Exception e)
        {
            Log.e("TAG","Error in starting razorpay Checkout",e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "payment Successful", Toast.LENGTH_SHORT).show();
        Map<String,Object> mMap=new HashMap<>();
        mMap.put("item_name",name);
        mMap.put("img_url",img_url);
        mMap.put("payment_id",s);
        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
               Intent intent=new Intent(PaymentActivity.this,HomeActivity.class);
               startActivity(intent);
               finish();
            }
        });
    }
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
    }
}