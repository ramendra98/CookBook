package com.example.cookbookuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {
private EditText userName,password;
private Button login;
TextView newRegistration,skip;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    ProgressDialog progressDialog;
    public Login() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Declared();
        newRegistration.setOnClickListener(this);
        login.setOnClickListener(this);
        skip.setOnClickListener(this);

        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Login..");
        progressDialog.setMessage("Please wait...");

    }

    private void Declared() {
        userName=findViewById(R.id.emailId);
        password=findViewById(R.id.password);
       newRegistration=findViewById(R.id.register_Activity);
       login=findViewById(R.id.login_activity);
       skip=findViewById(R.id.skip);

        mAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_activity:
                loginAccount();
                break;
            case R.id.register_Activity:
                Intent intent=new Intent(Login.this,Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.skip:
                foodBook();
                break;


        }
    }

    private void foodBook() {
        Intent intent=new Intent(Login.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void loginAccount() {


        final String uEmail=userName.getText().toString().trim();
        final String uPass=password.getText().toString().trim();


        if(uEmail.isEmpty())
        {
           userName.setError("Please Enter Your Email  ..");
           userName.requestFocus();
            return;
        }
        if(uPass.isEmpty())
        {
            password.setError("Please Enter Your Password ..");
            password.requestFocus();
            return;
        }
        else{
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(uEmail, uPass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendUserToMainAcivity();
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Logged in Successful", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    });
        }
    }

    private void SendUserToMainAcivity() {
        Intent intent=new Intent(Login.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}