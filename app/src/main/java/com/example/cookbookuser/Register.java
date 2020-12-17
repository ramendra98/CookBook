 package com.example.cookbookuser;

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
 import com.google.android.gms.tasks.OnFailureListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.AuthResult;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;

 import java.util.HashMap;

 public class Register extends AppCompatActivity implements View.OnClickListener {
private EditText userName,email,password;
private TextView loginAccount;
private Button register;
private FirebaseDatabase database;
private DatabaseReference myRef;
     private FirebaseUser currentUser;
     private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Declared();
    }

    private void Declared() {
        userName=findViewById(R.id.userName);
        email=findViewById(R.id.emailId);
        password=findViewById(R.id.password);
        loginAccount=findViewById(R.id.login);
       register=findViewById(R.id.register);

       register.setOnClickListener(this);
     loginAccount.setOnClickListener(this);
        mAuth= FirebaseAuth.getInstance();
      database=FirebaseDatabase.getInstance();
      myRef=database.getReference();

    }

     @Override
     public void onClick(View v) {
         switch (v.getId())
         {
             case R.id.register:
                 userRegister();
                 break;
             case R.id.login:
                 Intent intent=new Intent(Register.this,Login.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intent);
                 finish();
                 break;
         }
     }

     private void userRegister() {
        String uName=userName.getText().toString().trim();
        String uEmail=email.getText().toString().trim();
        String uPass=password.getText().toString().trim();

        if(uName.isEmpty())
        {
            userName.setError("Please Enter Your Name ..");
            userName.requestFocus();
            return;
        }
         if(uEmail.isEmpty())
         {
            email.setError("Please Enter Your Email  ..");
             email.requestFocus();
             return;
         }
         if(uPass.isEmpty())
         {
             password.setError("Please Enter Your Password ..");
             password.requestFocus();
             return;
         }
         else {
             final HashMap<String,Object> map=new HashMap<>();
             map.put("name",uName);
             map.put("email",uEmail);
             map.put("password",uPass);
             mAuth.createUserWithEmailAndPassword(uEmail,uPass)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful())
                             {
                                 String userID=mAuth.getCurrentUser().getUid();
                                 myRef.child("User1").child(userID).setValue(map);
                                 Intent intent=new Intent(Register.this,MainActivity.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                 startActivity(intent);
                                 finish();
                                 Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                             }

                         }
                     }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(Register.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                 }
             });
         }

     }
 }