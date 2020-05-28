package com.synnlabz.fitme.otherworkouts;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

public class otherworkoutViewHolder extends RecyclerView.ViewHolder{

    public TextView mOtherWorkoutName , mOtherWorkoutsSets , mOtherWorkoutReps , mOtherWorkoutRest;

    public otherworkoutViewHolder(View itemView) {
        super(itemView);

        mOtherWorkoutName = (TextView) itemView.findViewById(R.id.workoutName);
        mOtherWorkoutsSets = (TextView) itemView.findViewById(R.id.workoutSets);
        mOtherWorkoutReps = (TextView) itemView.findViewById(R.id.workoutReps);
        mOtherWorkoutRest = (TextView) itemView.findViewById(R.id.workoutRest);
    }
}
