package com.synnlabz.fitme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class Height extends Fragment implements View.OnClickListener {

    private static EditText Height;

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
        Button Next = (Button) view.findViewById(R.id.next);

        Next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();
        String Age = bundle.getString("age");
        int Gender = bundle.getInt("gender");
        Fragment fragment = new Fragment();
        Bundle i = new Bundle();
        switch (v.getId()) {
            case R.id.next:
                fragment = new Weight();
                String height = Height.getText().toString();
                i.putString("age", Age);
                i.putString("height",height);
                i.putInt("gender",Gender);
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
