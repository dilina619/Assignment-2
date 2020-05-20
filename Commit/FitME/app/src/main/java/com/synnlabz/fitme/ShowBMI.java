package com.synnlabz.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ShowBMI extends AppCompatActivity {

    private String Age , Height , Weight;
    private int Gender;
    private Float age,height,weight;

    private Button mContinue;
    private TextView Results , Output;
    private ImageView Icon;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_b_m_i);

        mContinue = (Button)findViewById(R.id.next);
        Results = (TextView)findViewById(R.id.bmiresults);
        Icon = (ImageView)findViewById(R.id.result);
        Output = (TextView) findViewById(R.id.output);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(ShowBMI.this, MainActivity.class);
                    startActivity(intent);
                    //finish();  window leaked error fixed
                    return;
                }
            }
        };

        try {
            //get intent to get account id
            Age = getIntent().getStringExtra("age");
            Height = getIntent().getStringExtra("height");
            Weight = getIntent().getStringExtra("weight");
            Gender = getIntent().getIntExtra("gender",0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        weight = Float.parseFloat(Weight);
        height = Float.parseFloat(Height);

        float bmi = (100*100*weight)/(height*height);
        final double result = Math.round(bmi * 100.0) / 100.0;
        Results.setText(String.valueOf(result));

        checkBMI(result);

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                Map userInfo = new HashMap<>();
                userInfo.put("gender", Gender);
                userInfo.put("age", Age);
                userInfo.put("height", Height);
                userInfo.put("weight", Weight);
                userInfo.put("bmi", result);
                currentUserDb.updateChildren(userInfo);
                Intent intent = new Intent(ShowBMI.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void checkBMI(double results){
        if (results < 18.5 ) {
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.underweight));
            Output.setText("Underweight");
        }

        else if (results >= 18.5 && results < 25) {
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.normal));
            Output.setText("Normal");
        }

        else if (results >= 25 && results < 30) {
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.obese));
            Output.setText("Overweight");
        }

        else if (results >= 30) {
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.exobese));
            Output.setText("Obesity");
        }

    }
}
