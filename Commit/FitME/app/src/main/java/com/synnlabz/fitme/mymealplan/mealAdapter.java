package com.synnlabz.fitme.mymealplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

import java.util.List;

public class mealAdapter extends RecyclerView.Adapter<mealViewHolder>{
    private List<mealObject> mealList;
    private Context context;

    public mealAdapter(List<mealObject> mealList, Context context){
        this.mealList = mealList;
        this.context = context;
    }

    @Override
    public mealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mealplan, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        mealViewHolder rcv = new mealViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(mealViewHolder holder, int position) {
        holder.mMealHeader.setText(mealList.get(position).getHeading());
        holder.mMealDesc.setText(mealList.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return this.mealList.size();
    }
}
