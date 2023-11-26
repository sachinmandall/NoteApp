package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateAcoountActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText,confirmpasswordEditText;
    Button createaccountbutton;
    ProgressBar ProgressBar;
    TextView logintextview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acoount);

        emailEditText=findViewById(R.id.Email_edit_text);
        passwordEditText=findViewById(R.id.password_edit_text);
        confirmpasswordEditText=findViewById(R.id.confirm_password_edit_text);
      createaccountbutton=findViewById(R.id.create_account_button);
        ProgressBar=findViewById(R.id.ProgressBar);
      logintextview=findViewById(R.id.login_textview);

      createaccountbutton.setOnClickListener(v->createAccount());
      logintextview.setOnClickListener(v->finish());



    }
    void createAccount(){
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String confirmPassword=confirmpasswordEditText.getText().toString();

        boolean isValidated=validateData(email,password,confirmPassword);
        if (!isValidated){
            return;
        }
        createAccountInfirebase(email,password);



    }

        void createAccountInfirebase(String email,String password){
           changeInprogress(true);
            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAcoountActivity.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            changeInprogress(false);
                            if (task.isSuccessful()){
                                //create acccount is done
                              utility.showToast(CreateAcoountActivity.this, "Check email to verify");

                                firebaseAuth.getCurrentUser().sendEmailVerification();
                                firebaseAuth.signOut();
                                finish();


                            }else {
                                //failure
                                Toast.makeText(CreateAcoountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
            );


        }
        //it will take time proceed above method thats why we will create progess bar
        void changeInprogress(boolean inprogress) {
            ProgressBar progressBar = findViewById(R.id.ProgressBar); // Make sure this ID matches the one in your layout XML
            Button createAccountButton = findViewById(R.id.create_account_button); // Make sure this ID matches the one in your layout XML

            if (inprogress) {
                progressBar.setVisibility(View.VISIBLE);
                createAccountButton.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                createAccountButton.setVisibility(View.VISIBLE);
            }
        }


    boolean validateData(String email,String password,String confirmpassword){
        //validate the data input by user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
           passwordEditText.setError("password length is invalid");
           return false;

        }
        if (!password.equals(confirmpassword)){
            confirmpasswordEditText.setError("Passsword does not match");
            return false;
        }
        return true;
    }
}