package com.example.meefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.meefy.Domain.Items;
import com.example.meefy.adapter.ItemRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
     private FirebaseFirestore mStore;
     List<Items> mItemsList;
     private RecyclerView itemRecyclerView;
     private ItemRecyclerAdapter itemRecyclerAdapter;
     private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        String type=getIntent().getStringExtra("type");
        mStore=FirebaseFirestore.getInstance();
        mItemsList=new ArrayList<>();
        itemRecyclerView=findViewById(R.id.items_recycler);
        mToolBar=findViewById(R.id.item_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Items");
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        itemRecyclerAdapter=new ItemRecyclerAdapter(this,mItemsList);
        itemRecyclerView.setAdapter(itemRecyclerAdapter);
        if(type==null ||type.isEmpty())
        {
            mStore.collection("All").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot doc:task.getResult())
                        {
                            Items item=doc.toObject(Items.class);
                            mItemsList.add(item);
                            itemRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

        }
        if(type!=null && type.equalsIgnoreCase("man"))
        {
            mStore.collection("All").whereEqualTo("type","man").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot doc:task.getResult())
                        {
                            Items item=doc.toObject(Items.class);
                            mItemsList.add(item);
                            itemRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("Woman"))
        {
            mStore.collection("All").whereEqualTo("type","Woman").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot doc:task.getResult())
                        {
                            Items item=doc.toObject(Items.class);
                            mItemsList.add(item);
                            itemRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("kids"))
        {
            mStore.collection("All").whereEqualTo("type","kids").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot doc:task.getResult())
                        {
                            Log.i("TAG","onComplete: "+doc.toString());
                            Items item=doc.toObject(Items.class);
                            mItemsList.add(item);
                            itemRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.search_it);
        androidx.appcompat.widget.SearchView searchView= (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchItem(String newText) {
        mItemsList.clear();
        mStore.collection("All").whereGreaterThanOrEqualTo("name",newText).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        Log.i("TAG","onComplete: "+doc.toString());
                        Items item=doc.toObject(Items.class);
                        mItemsList.add(item);
                        itemRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}