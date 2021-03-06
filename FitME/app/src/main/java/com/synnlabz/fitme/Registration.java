package com.synnlabz.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synnlabz.fitme.bmi.SetValue;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private Button mRegister;       //defining variables
    private EditText mEmail, mPassword, mName;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mRegister = (Button)findViewById(R.id.register);        //initializing components
        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);

        final LoadingDialog loadingDialog = new LoadingDialog(Registration.this);       //initializing loading widget

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(Registration.this, SetValue.class);
                    startActivity(intent);
                    //finish();  window leaked error fixed
                    return;
                }
            }
        };

        mRegister.setOnClickListener(new View.OnClickListener() {       //when user clicks register button
            @Override
            public void onClick(View view) {

                final String email = mEmail.getText().toString();       //getting inpute from fields
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();

                if (email.equals("") || password.equals("") || name.equals("")) {  //Validation
                    Toast.makeText(getApplicationContext(),"Please fill the empty fields",Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Registration.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                            }else{
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("email", email);       //put values into hashmap
                                userInfo.put("name", name);
                                currentUserDb.updateChildren(userInfo);     //update database
                            }
                        }
                    });
                    loadingDialog.startLoadingDialog();     //loading screen
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    },3000);        //showing up 3sec
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);      //check authentication state
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    public void OpensignUp(View view) {     //redirects to Login Page
        startActivity(new Intent(Registration.this,Login.class));
    }
}
