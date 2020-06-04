package com.synnlabz.fitme.mymealplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.synnlabz.fitme.R;

import java.util.List;

public class mealAdapter extends RecyclerView.Adapter<mealViewHolder>{      //Recylerview Adapter
    private List<mealObject> mealList;      //defining varibles
    private Context context;

    public mealAdapter(List<mealObject> mealList, Context context){
        this.mealList = mealList;       //constructor
        this.context = context;
    }

    @Override
    public mealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mealplan, null, false);        //initializing layout
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        mealViewHolder rcv = new mealViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(mealViewHolder holder, int position) {
        holder.mMealHeader.setText(mealList.get(position).getHeading());  //showing details
        holder.mMealDesc.setText(mealList.get(position).getDesc());
        if(!mealList.get(position).getMealImage().equals("default")){
            Glide.with(context).load(mealList.get(position).getMealImage()).into(holder.mMealImage);
        }

    }

    @Override
    public int getItemCount() {
        return this.mealList.size();        //return the list count
    }
}
