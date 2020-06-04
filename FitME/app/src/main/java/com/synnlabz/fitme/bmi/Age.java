package com.synnlabz.fitme.bmi;

import android.media.Image;
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


public class Age extends Fragment implements View.OnClickListener {     //Fragment with click listener

    private static EditText Age;        //Initializing
    private static int Gender;
    private int val=22;

    public Age() {          //constructor
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {       //oncreate method
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age, container, false);  //please check main fragment . this is same as main
        Age = (EditText) view.findViewById(R.id.age);       //defining components
        Button Next = (Button) view.findViewById(R.id.next);
        ImageButton Plus = (ImageButton)view.findViewById(R.id.plus);
        ImageButton Minus = (ImageButton)view.findViewById(R.id.minus);

        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //increase value when user presses +
                val = val+1;
                Age.setText(String.valueOf(val));
            }
        });

        Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //decrease value when user presses -
                val = val-1;
                Age.setText(String.valueOf(val));
            }
        });

        Next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();        //creating bundle object
        int Gender = bundle.getInt("gender", 0);    //put values into bundle
        Fragment fragment = new Fragment();
        Bundle i = new Bundle();
        switch (v.getId()) {
            case R.id.next:     //when user clicks next btn
                fragment = new Height();
                String age = Age.getText().toString();      //getting users input
                i.putString("age", age);        //put values into bundle
                i.putInt("gender",Gender);
                fragment.setArguments(i);
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment nextFragment) {        //moving to next fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, nextFragment);      //replace the container fragment
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
