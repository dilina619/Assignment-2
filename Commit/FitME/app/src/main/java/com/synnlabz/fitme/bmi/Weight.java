package com.synnlabz.fitme.bmi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.synnlabz.fitme.R;


public class Weight extends Fragment implements View.OnClickListener {

    private static EditText Weight;

    public Weight() {
        // Required empty public constructor
    }

    public static Weight newInstance(String param1, String param2) {
        Weight fragment = new Weight();
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
        View view = inflater.inflate(R.layout.fragment_weight, container, false);  //please check main fragment . this is same as main
        Weight = (EditText) view.findViewById(R.id.weight);
        Button Next = (Button) view.findViewById(R.id.next);

        Next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();
        String Age = bundle.getString("age");
        String Height = bundle.getString("height");
        int Gender = bundle.getInt("gender");
        Fragment fragment = new Fragment();
        switch (v.getId()) {
            case R.id.next:
                String weight = Weight.getText().toString();
                Intent i = new Intent(getActivity(), ShowBMI.class);
                i.putExtra("age", Age);
                i.putExtra("height",Height);
                i.putExtra("weight",weight);
                i.putExtra("gender",Gender);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
                break;
        }
    }

}
