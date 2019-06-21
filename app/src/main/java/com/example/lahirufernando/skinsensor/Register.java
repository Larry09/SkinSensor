package com.example.lahirufernando.skinsensor;
/**
 * Created by lahirufernando on 30/11/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Register extends AppCompatActivity {
    //Instances
    EditText usernameText;
    EditText passwordText;
    TextView signinLogin;
    Button registerButton;


    //Firebase Instances
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        signinLogin = (TextView) findViewById(R.id.signinLogin);
        registerButton = (Button) findViewById(R.id.registerButton);


        //Firebase methods assignment
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();// Gets current users logged in
                //If user exists log them in
                if (user != null) {

                } else {
                    startActivity(new Intent(Register.this, LoginPage.class));
                }
            }
        };


        //Click Listeners - Register Page button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName, userPass, userSex;
                userName = usernameText.getText().toString().trim();
                userPass = passwordText.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPass)) {
                    Toast.makeText(Register.this, "Please fill the boxes", Toast.LENGTH_LONG).show();
                }
                //Check if deatils are input or not
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPass)) {
                    mAuth.createUserWithEmailAndPassword(userName, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "User Account Created", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Register.this, LoginPage.class));
                            } else {
                                Toast.makeText(Register.this, "User Account Failed to be created", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        //Already User Login
        signinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, LoginPage.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
