package com.synnlabz.fitme.bmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synnlabz.fitme.MainActivity;
import com.synnlabz.fitme.R;

import java.util.HashMap;
import java.util.Map;

public class ShowBMI extends AppCompatActivity {

    private String Age , Height , Weight, noimage;      //defining variables
    private int Gender , Categ , age;
    private Float height,weight;
    private double BMR , TDEE;

    private Button mContinue;
    private TextView Results , Output;
    private ImageView Icon;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_b_m_i);

        mContinue = (Button)findViewById(R.id.next);        //initializing items
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

        try {       //error handing
            Age = getIntent().getStringExtra("age");        //get variables from previous fragments
            Height = getIntent().getStringExtra("height");
            Weight = getIntent().getStringExtra("weight");
            Gender = getIntent().getIntExtra("gender",0);
        } catch (Exception e) {     //catch clause
            e.printStackTrace();
        }

        weight = Float.parseFloat(Weight);      //converting string into float value
        height = Float.parseFloat(Height);
        age = Integer.parseInt(Age);

        if (Gender==1){
            BMR = 10*weight+6.25*height-5*age+5;        //calculating TDEE value using BMR
            TDEE = BMR*1.375;
        }else if(Gender==2){
            BMR = 10*weight+6.25*height-5*age-161;
            TDEE = BMR*1.375;
        }

        float bmi = (100*100*weight)/(height*height);       //calculating BMI value
        final double result = Math.round(bmi * 100.0) / 100.0;
        Results.setText(String.valueOf(result));

        checkBMI(result);       //calling check BMI function

        mContinue.setOnClickListener(new View.OnClickListener() {       //when user clicks continue btn
            @Override
            public void onClick(View v) {
                String userId = mAuth.getCurrentUser().getUid();        //get current user id
                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);       //making firebase reference
                Map userInfo = new HashMap<>();
                userInfo.put("gender", Gender);     //putting values into hashmap
                userInfo.put("age", Age);
                userInfo.put("height", Height);
                userInfo.put("weight", Weight);
                userInfo.put("category",Categ);
                userInfo.put("tdee",TDEE);
                userInfo.put("bmi", result);
                userInfo.put("profileImageUrl","default");
                currentUserDb.updateChildren(userInfo);     //push all the values into firebase database
                Intent intent = new Intent(ShowBMI.this, MainActivity.class);
                startActivity(intent);      //redirecting to next activity
            }
        });

    }

    public void checkBMI(double results){
        if (results < 18.5 ) {      //decision making phase
            Categ = 1;              //check users bmi value and categorize
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.underweight));
            Output.setText("Underweight");      //print output
        }

        else if (results >= 18.5 && results < 25) {
            Categ = 2;
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.normal));
            Output.setText("Normal");
        }

        else if (results >= 25 && results < 30) {
            Categ = 3;
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.obese));
            Output.setText("Overweight");
        }

        else if (results >= 30) {
            Categ = 4;
            Icon.setImageDrawable(getResources().getDrawable(R.drawable.exobese));
            Output.setText("Obesity");
        }

    }
}
