package com.synnlabz.fitme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

public class SetValue extends AppCompatActivity {

    FrameLayout frameLayout;        //Defining FrameLayout
    private Fragment mContentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_value);

        frameLayout = findViewById(R.id.container);     //Initializing Frame
        replaceFragments(Gender.class);       //Replace the FrameLayout with MainFragment
    }

    public void replaceFragments(Class fragmentClass) {     //Pass Fragment
        frameLayout.removeAllViews();
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();      //Get Fragment
        } catch (Exception e) {     //Handling Error
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment,null).commit();
        switchContent(fragment);        //Change Fragment
    }

    public void switchContent(Fragment fragment) {      //Switch Fragments
        FrameLayout fl = (FrameLayout) findViewById(R.id.container);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();
        fl.removeAllViews();
        mContentFragment = fragment;
    }
}
