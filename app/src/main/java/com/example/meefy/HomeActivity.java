package com.example.meefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meefy.Domain.Items;
import com.example.meefy.adapter.ItemRecyclerAdapter;
import com.example.meefy.fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Fragment homeFragment;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private EditText mSearchText;
    private FirebaseFirestore mStore;
    private List<Items> mItemsList;
    private RecyclerView mItemRecyclerView;
    private ItemRecyclerAdapter mItemRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        mToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        mAuth = FirebaseAuth.getInstance();
        mStore=FirebaseFirestore.getInstance();
        mSearchText=findViewById(R.id.search_text);
        mItemsList=new ArrayList<>();
        mItemRecyclerView=findViewById(R.id.search_recycler);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mItemRecyclerAdapter=new ItemRecyclerAdapter(this,mItemsList);
        mItemRecyclerView.setAdapter(mItemRecyclerAdapter);
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty())
                {
                    mItemsList.clear();
                    mItemRecyclerAdapter.notifyDataSetChanged();
                }else {
                    searchItem(s.toString());
                }
            }
        });
       /* Button btn=findViewById(R.id.LogoutBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });*/
    }

    private void searchItem(String text) {
        if(!text.isEmpty())
        {
            mStore.collection("All").whereGreaterThanOrEqualTo("name",text).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() && task.getResult()!=null)
                    {
                        for(DocumentSnapshot doc:task.getResult().getDocuments())
                        {
                            Items items=doc.toObject(Items.class);
                            mItemsList.add(items);
                            mItemRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                }
            });
        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_btn) {
            SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("remember","false");
            editor.apply();
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}