package com.synnlabz.fitme.mymealplan;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.synnlabz.fitme.R;

public class mealViewHolder extends RecyclerView.ViewHolder{

    public TextView mMealHeader , mMealDesc;

    public mealViewHolder(View itemView) {
        super(itemView);

        mMealHeader = (TextView) itemView.findViewById(R.id.mealheading);
        mMealDesc = (TextView) itemView.findViewById(R.id.mealdesc);
    }
}
