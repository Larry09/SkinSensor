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

public class LoginPage extends AppCompatActivity {


    //Assign ID
    EditText usernameText;
    EditText passwordText;
    Button loginButton;
    TextView createAccountText;
    //Firebase Instances
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountText = (TextView) findViewById(R.id.createAccountText);

        //Firebase methods assignment
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();// Gets current users logged in
                //If user exists log them in
                if(user != null){

                }else {
                    startActivity(new Intent(LoginPage.this,Register.class));
                }
            }
        };
        //Click Listeners

      loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login using Username & Password
                String username,password;
                username = usernameText.getText().toString().trim();
                password = passwordText.getText().toString().trim();
                //Toast.makeText(LoginPage.this,"Welcome to SkinSensor",Toast.LENGTH_LONG).show();
                //startActivity(new Intent(LoginPage.this,HomePage.class));
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginPage.this, "Please fill the boxes", Toast.LENGTH_LONG).show();
                }
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                    mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginPage.this,"Welcome to SkinSensor",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginPage.this,HomePage.class));
                            }
                            else{
                                Toast.makeText(LoginPage.this, "Incorrect login details, please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
      });
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this,Register.class);
                startActivity(intent);
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
