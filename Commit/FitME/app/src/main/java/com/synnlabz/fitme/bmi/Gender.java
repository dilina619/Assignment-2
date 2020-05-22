package com.synnlabz.fitme.bmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.synnlabz.fitme.R;


public class Gender extends Fragment implements View.OnClickListener{

    public Gender() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gender, container, false);  //please check main fragment . this is same as main
        Button Male = (Button) view.findViewById(R.id.male);
        Button Female = (Button) view.findViewById(R.id.female);

        Male.setOnClickListener(this);
        Female.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int Gender;
        Fragment fragment = new Fragment();
        Bundle i = new Bundle();
        switch (v.getId()) {
            case R.id.male:
                Gender=1;
                fragment = new Age();
                i.putInt("gender", Gender);
                fragment.setArguments(i);
                replaceFragment(fragment);
                break;

            case R.id.female:
                Gender=2;
                fragment = new Age();
                i.putInt("gender", Gender);
                fragment.setArguments(i);
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment nextFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
