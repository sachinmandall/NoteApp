package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
    Button loginbutton;
    android.widget.ProgressBar ProgressBar;
    TextView CreateAccounttextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText=findViewById(R.id.Email_edit_text);
        passwordEditText=findViewById(R.id.password_edit_text);
        loginbutton=findViewById(R.id.login_account_button);
        ProgressBar=findViewById(R.id.ProgressBar);
        CreateAccounttextview=findViewById(R.id.create_account_button_textview);

        //loginbutton.setOnClickListener((v->loginUser());
        loginbutton.setOnClickListener(v ->loginUser() );
        CreateAccounttextview.setOnClickListener(v->startActivity(new Intent(LoginActivity.this,CreateAcoountActivity.class)));



    }
    void loginUser(){
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();

        boolean isValidated=validateData(email,password);
        if (!isValidated){
            return;
        }
        logincreateAccountInfirebase(email,password);


    }
    void logincreateAccountInfirebase(String email,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInprogress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInprogress(false);
                if (task.isSuccessful()){
                    //login is sucess


                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to main activity
                        finish();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }else{
                       utility.showToast(LoginActivity.this,"Email not verified please verified your Emaill");
                    }

                }else{
                    //login faild
                    utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());

                }

            }
        });

    }
    void changeInprogress(boolean inprogress) {
        ProgressBar progressBar = findViewById(R.id.ProgressBar); // Make sure this ID matches the one in your layout XML
        Button createAccountButton = findViewById(R.id.create_account_button); // Make sure this ID matches the one in your layout XML

        if (inprogress) {
            progressBar.setVisibility(View.VISIBLE);
            loginbutton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginbutton.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email,String password){
        //validate the data input by user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            passwordEditText.setError("password length is invalid");
            return false;

        }

        return true;
    }
}