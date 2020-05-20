package com.synnlabz.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        passwordEmail = (EditText)findViewById(R.id.email);
        resetPassword = (Button)findViewById(R.id.reset);
        firebaseAuth = FirebaseAuth.getInstance();

        passwordEmail.setHintTextColor(Color.GRAY);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = passwordEmail.getText().toString().trim();

                if (user_email.equals("")){
                    Toast.makeText(ResetPassword.this, "Please enter your email address here",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(user_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPassword.this,"Password Reset email sent!!",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ResetPassword.this, Login.class));
                            }else {
                                Toast.makeText(ResetPassword.this,"Error Sending Password Reset",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void OpenLogin(View view) {
        startActivity(new Intent(ResetPassword.this,Login.class));
    }
}
