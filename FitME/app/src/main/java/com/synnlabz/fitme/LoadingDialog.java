package com.synnlabz.fitme;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    LoadingDialog(Activity myActivity){     //constructor
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();     //getting layout inflater
        builder.setView(inflater.inflate(R.layout.loading_screen,null));        //setting dialog view
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();       //disapprear dialog box
    }
}
