package com.synnlabz.fitme.myworkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

import java.util.List;

public class workoutAdapter extends RecyclerView.Adapter<workoutViewHolder>{
    private List<WorkoutObject> workoutList;
    private Context context;

    public workoutAdapter(List<WorkoutObject> workoutList, Context context){
        this.workoutList = workoutList;
        this.context = context;
    }

    @NonNull
    @Override
    public workoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        workoutViewHolder rcv = new workoutViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(workoutViewHolder holder, int position) {
        holder.mWorkoutName.setText(workoutList.get(position).getName());
        holder.mWorkoutsSets.setText(workoutList.get(position).getSets());
        holder.mWorkoutReps.setText(workoutList.get(position).getReps());
    }

    @Override
    public int getItemCount() {
        return this.workoutList.size();
    }
}
