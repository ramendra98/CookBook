package com.example.cookbookuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity{
private ImageView image,addFavorite;
private TextView descrip,prices,itemName,comment;
Dialog recipeDialog;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ValueEventListener listener;
    private RecyclerView recyclerView;
    private CommentAdpter adpter;
    private List<CommentModel> commentModel;
  private   String itemNameRecipe;
   private String recipeDescri;
    Dialog favDialog;
    CheckBox check_box_Recipe,check_box_Cui;
    Button okBtn;
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        image=findViewById(R.id.img);
       descrip=findViewById(R.id.description);
      prices=findViewById(R.id.prices);
       itemName=findViewById(R.id.recipeName);
        comment=findViewById(R.id.comment);
        recyclerView=findViewById(R.id.recycer);
        addFavorite=findViewById(R.id.addfavorite);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //get Image and id

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            descrip.setText(bundle.getString("Description"));
            prices.setText("Rs."+bundle.getString("Prices"));
            itemName.setText(bundle.getString("Name"));

            // image.setImageResource(bundle.getInt("Image"));
            Glide.with(this).load(bundle.getString("Image"))
                    .into(image);
        }
        // Favorite
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteDialog();
            }
        });

        //Define Image Name
        itemNameRecipe=itemName.getText().toString();
        recipeDescri= descrip.getText().toString();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(DetailActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        commentModel= new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Comment").child(itemNameRecipe);
        listener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentModel.clear();
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren())
                {
                    CommentModel commentModel1=itemSnapshot.getValue(CommentModel.class);
                    commentModel.add(commentModel1);
                }
                adpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adpter=new CommentAdpter(DetailActivity.this,commentModel);
        recyclerView.setAdapter(adpter);



//Comment box
       comment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CommentMessage();
           }


       });
    }

    private void favoriteDialog() {
        favDialog=new Dialog(this);
        favDialog.setContentView(R.layout.favorite);
        //  favDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_bordars));
        favDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        favDialog.setCancelable(true);
        okBtn=favDialog.findViewById(R.id.okBtn);
        check_box_Recipe=favDialog.findViewById(R.id.checkBox1);
        check_box_Cui=favDialog.findViewById(R.id.checkBox2);
        favDialog.show();
        // Save Recipe




        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_box_Recipe.isChecked())
                {
                    HashMap<String,Object>recipeMap=new HashMap<>();
                    recipeMap.put("RecipeName",itemNameRecipe);
                    recipeMap.put("Decription",recipeDescri);
                    String rId=UUID.randomUUID().toString();
                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    DatabaseReference myRecipe=database.getReference().child("FavRecipe");

                    myRecipe.child(currentUserId).child(rId).setValue(recipeMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    favDialog.dismiss();
                                    Toast.makeText(DetailActivity.this, "Favorites Recipe Add Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            favDialog.dismiss();
                            Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



                }
                if(check_box_Cui.isChecked())
                {

                    Toast.makeText(DetailActivity.this, "Not Work", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void CommentMessage() {
final EditText uName,message;
Button postme;
recipeDialog=new Dialog(this);
        recipeDialog.setContentView(R.layout.comment_dialog);
        recipeDialog.setTitle("Comment..");
        recipeDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        recipeDialog.setCancelable(true);
        recipeDialog.show();
        uName= recipeDialog.findViewById(R.id.userName);
        message= recipeDialog.findViewById(R.id.textmessage);

        postme=recipeDialog.findViewById(R.id.post);
        postme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           String userName=uName.getText().toString().trim();
                String usermessage=message.getText().toString().trim();
                if(userName.isEmpty())
                {
                    uName.setError("Enter Your Name!");
                   uName.requestFocus();
                    return;
                     }

                if(usermessage.isEmpty())
                {
                    message.setError("Enter Your Name!");
                    message.requestFocus();
                    return;
                }
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference();
                HashMap<Object,String> map=new HashMap<>();
                map.put("name",uName.getText().toString());
                map.put("comment",message.getText().toString());
                String id= UUID.randomUUID().toString();
                myRef.child("Comment").child(itemNameRecipe).child(id).setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        recipeDialog.dismiss();
                        Toast.makeText(DetailActivity.this, "Comment Posted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        recipeDialog.dismiss();
                        Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser!=null)
        {
          currentUserId=currentUser.getUid();
          //  Toast.makeText(this,"he"+ currentUserId, Toast.LENGTH_SHORT).show();
        }

    }
}