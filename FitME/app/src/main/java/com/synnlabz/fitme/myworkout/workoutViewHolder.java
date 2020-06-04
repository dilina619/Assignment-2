package com.synnlabz.fitme.myworkout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

public class workoutViewHolder extends RecyclerView.ViewHolder{

    public TextView mWorkoutName , mWorkoutsSets , mWorkoutReps;        //defining variables
    public ImageView mWorkoutImage;

    public workoutViewHolder(View itemView) {
        super(itemView);

        mWorkoutName = (TextView) itemView.findViewById(R.id.workoutName);      //initializing components
        mWorkoutsSets = (TextView) itemView.findViewById(R.id.workoutSets);
        mWorkoutReps = (TextView) itemView.findViewById(R.id.workoutReps);
        mWorkoutImage = (ImageView)itemView.findViewById(R.id.workoutImage);
    }
}
