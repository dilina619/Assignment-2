package com.synnlabz.fitme.otherworkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

import java.util.List;

public class otherworkoutAdapter extends RecyclerView.Adapter<otherworkoutViewHolder> {
    private List<otherworkoutObject> otherworkoutList;
    private Context context;

    public otherworkoutAdapter(List<otherworkoutObject> otherworkoutList, Context context){
        this.otherworkoutList = otherworkoutList;
        this.context = context;
    }

    @Override
    public otherworkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_otherworkout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        otherworkoutViewHolder rcv = new otherworkoutViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(otherworkoutViewHolder holder, int position) {
        holder.mOtherWorkoutName.setText(otherworkoutList.get(position).getName());
        holder.mOtherWorkoutsSets.setText(otherworkoutList.get(position).getSets());
        holder.mOtherWorkoutReps.setText(otherworkoutList.get(position).getReps());
        holder.mOtherWorkoutRest.setText(otherworkoutList.get(position).getRest());
    }

    @Override
    public int getItemCount() {
        return this.otherworkoutList.size();
    }
}
