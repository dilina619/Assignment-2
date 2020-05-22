package com.synnlabz.fitme.myworkout;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

public class workoutViewHolder extends RecyclerView.ViewHolder{

    public TextView mWorkoutName , mWorkoutsSets , mWorkoutReps;

    public workoutViewHolder(View itemView) {
        super(itemView);

        mWorkoutName = (TextView) itemView.findViewById(R.id.workoutName);
        mWorkoutsSets = (TextView) itemView.findViewById(R.id.workoutSets);
        mWorkoutReps = (TextView) itemView.findViewById(R.id.workoutReps);
    }
}
