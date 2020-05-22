package com.synnlabz.fitme.bmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.synnlabz.fitme.R;


public class Age extends Fragment implements View.OnClickListener {

    private static EditText Age;
    private static int Gender;

    public Age() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age, container, false);  //please check main fragment . this is same as main
        Age = (EditText) view.findViewById(R.id.age);
        Button Next = (Button) view.findViewById(R.id.next);

        Next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();
        int Gender = bundle.getInt("gender", 0);
        Fragment fragment = new Fragment();
        Bundle i = new Bundle();
        switch (v.getId()) {
            case R.id.next:
                fragment = new Height();
                String age = Age.getText().toString();
                i.putString("age", age);
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
