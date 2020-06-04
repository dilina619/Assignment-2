package com.synnlabz.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button mLogin;      //defining variables
    private EditText mEmail, mPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = (Button) findViewById(R.id.login);

        mEmail = (EditText) findViewById(R.id.email);       //initializing components
        mPassword = (EditText) findViewById(R.id.password);

        final LoadingDialog loadingDialog = new LoadingDialog(Login.this);

        mEmail.setHintTextColor(Color.GRAY);
        mPassword.setHintTextColor(Color.GRAY);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    //finish(); window leaked error fixed
                    return;
                }
            }
        };

        mLogin.setOnClickListener(new View.OnClickListener() {      //when user clicks login btn
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();       //getting user inputs
                final String password = mPassword.getText().toString();

                if (email.equals("") || password.equals("")) {  //Validation
                    Toast.makeText(getApplicationContext(),"Please fill the empty fields",Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "sign in error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    loadingDialog.startLoadingDialog();     //loading screen apprear
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    },3000);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    public void OpenRegister(View view) {       //redirecting to next page
        startActivity(new Intent(Login.this,Registration.class));
    }

    public void ForgetPass(View view) {
        startActivity(new Intent(Login.this,ResetPassword.class));
    }
}
