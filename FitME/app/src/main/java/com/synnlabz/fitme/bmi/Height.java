package com.synnlabz.fitme.bmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.synnlabz.fitme.R;


public class Height extends Fragment implements View.OnClickListener {

    private static EditText Height;
    private int val=175;

    public Height() {
        // Required empty public constructor
    }

    public static Height newInstance(String param1, String param2) {
        Height fragment = new Height();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_height, container, false);  //please check main fragment . this is same as main
        Height = (EditText) view.findViewById(R.id.height);
        Button Next = (Button) view.findViewById(R.id.next);        //initializing components
        ImageButton Plus = (ImageButton)view.findViewById(R.id.plus);
        ImageButton Minus = (ImageButton)view.findViewById(R.id.minus);

        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //when plus btn clicks value increasing
                val = val+1;
                Height.setText(String.valueOf(val));
            }
        });

        Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //press - value decreasing
                val = val-1;
                Height.setText(String.valueOf(val));
            }
        });

        Next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();
        String Age = bundle.getString("age");       //get data from previous fragment
        int Gender = bundle.getInt("gender");
        Fragment fragment = new Fragment();
        Bundle i = new Bundle();
        switch (v.getId()) {
            case R.id.next:
                fragment = new Weight();
                String height = Height.getText().toString();
                i.putString("age", Age);        //pass data into next fragment
                i.putString("height",height);
                i.putInt("gender",Gender);
                fragment.setArguments(i);
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment nextFragment) {        //fragment replacing
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
