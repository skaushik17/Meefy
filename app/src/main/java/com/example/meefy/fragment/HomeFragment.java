package com.example.meefy.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meefy.Domain.BestSell;
import com.example.meefy.Domain.Category;
import com.example.meefy.Domain.Feature;
import com.example.meefy.ItemActivity;
import com.example.meefy.ItemsActivity;
import com.example.meefy.R;
import com.example.meefy.adapter.BestSellAdapter;
import com.example.meefy.adapter.CategoryAdapter;
import com.example.meefy.adapter.FeatureAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
  private FirebaseFirestore mStore;
  //category tab
  private List<Category> mCategoryList;
  private CategoryAdapter mCategoryAdapter;
  private RecyclerView mCatRecyclerView;
  //Feature tab
  private List<Feature> mFeatureList;
  private FeatureAdapter mFeatureAdapter;
  private RecyclerView mFeatureRecyclerView;
  //BestSell
  private List<BestSell> mBestSellList;
  private BestSellAdapter mBestSellAdapter;
  private RecyclerView mBestSellRecyclerView;
  private TextView mSeeAll;
  private TextView mFeature;
  private TextView mBestSell;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        mStore=FirebaseFirestore.getInstance();
        mSeeAll=view.findViewById(R.id.see_all);
        mFeature=view.findViewById(R.id.fea_see_all);
        mBestSell=view.findViewById(R.id.best_sell);
        mCatRecyclerView=view.findViewById(R.id.category_recycler);
        mFeatureRecyclerView=view.findViewById(R.id.feature_recycler);
        mBestSellRecyclerView=view.findViewById(R.id.bestsell_recycler);
        // for category
        mCategoryList=new ArrayList<>();
        mCategoryAdapter=new CategoryAdapter(getContext(),mCategoryList);
        mCatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mCatRecyclerView.setAdapter(mCategoryAdapter);

        //for feature
        mFeatureList=new ArrayList<>();
        mFeatureAdapter=new FeatureAdapter(getContext(),mFeatureList);
        mFeatureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mFeatureRecyclerView.setAdapter(mFeatureAdapter);
        // for bestSell
        mBestSellList=new ArrayList<>();
        mBestSellAdapter=new BestSellAdapter(getContext(),mBestSellList);
        mBestSellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mBestSellRecyclerView.setAdapter(mBestSellAdapter);


        final Task<QuerySnapshot> category = mStore.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                mCategoryList.add(category);
                                mCategoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
         // feature..
        final Task<QuerySnapshot> feature = mStore.collection("Feature")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Feature feature = document.toObject(Feature.class);
                                mFeatureList.add(feature);
                                mFeatureAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // BestSeller

        final Task<QuerySnapshot> bestsell = mStore.collection("BestSeller")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BestSell bestSell = document.toObject(BestSell.class);
                                mBestSellList.add(bestSell);
                                mBestSellAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        mSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });
        mBestSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });
        mFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}