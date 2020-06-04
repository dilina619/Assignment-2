package com.synnlabz.fitme.mymealplan;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

public class mealViewHolder extends RecyclerView.ViewHolder{

    public TextView mMealHeader , mMealDesc;        //defining variables
    public ImageView mMealImage;

    public mealViewHolder(View itemView) {
        super(itemView);

        mMealHeader = (TextView) itemView.findViewById(R.id.mealheading);           //initializing components
        mMealDesc = (TextView) itemView.findViewById(R.id.mealdesc);
        mMealDesc = (TextView) itemView.findViewById(R.id.mealdesc);
        mMealImage = (ImageView) itemView.findViewById(R.id.mealitem);
    }
}
