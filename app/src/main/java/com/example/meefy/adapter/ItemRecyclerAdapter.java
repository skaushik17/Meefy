package com.example.meefy.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meefy.DetailActivity;
import com.example.meefy.Domain.Items;
import com.example.meefy.HomeActivity;
import com.example.meefy.R;

import org.w3c.dom.Text;

import java.util.List;

public class ItemRecyclerAdapter  extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {
    Context applicationContext;
    List<Items> mItemsList;
    public ItemRecyclerAdapter(Context applicationContext, List<Items> mItemsList) {
        this.applicationContext=applicationContext;
        this.mItemsList=mItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(applicationContext).inflate(R.layout.single_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCost.setText("$"+mItemsList.get(position).getPrice());
        holder.mName.setText(mItemsList.get(position).getName());
        if(!(applicationContext instanceof HomeActivity))
        {
            Glide.with(applicationContext).load(mItemsList.get(position).getImg_url()).into(holder.mItemImage);
        }
        else
        {
            holder.mItemImage.setVisibility(View.GONE);
        }

        holder.mItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("Detail", mItemsList.get(position));
                applicationContext.startActivity(intent);
            }
        });

        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("Detail", mItemsList.get(position));
                applicationContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mItemImage;
        private TextView mCost;
        private TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           mItemImage=itemView.findViewById(R.id.item_image);
           mCost=itemView.findViewById(R.id.item_cost);
           mName=itemView.findViewById(R.id.item_nam);
        }
    }
}
