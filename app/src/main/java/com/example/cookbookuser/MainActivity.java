package com.example.cookbookuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
private RecyclerView recyclerView;
private List<FoodMdel> myFoodList;
private List<FavoriteLitModel>favolist;
private EditText searchFood;
FoodMdel foodMdel;
    private DatabaseReference databaseReference;
    private ValueEventListener listener;
    private CookBookAdpter adpter;
    private FavoriteRecipeAdpter  favoriteRecipeAdpter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String currentUserId;
    Dialog favDialog;
    String uNameS;
    ProgressDialog progressDialog;
    private TextView recipeName,recip_Descri;
    //private DatabaseReference RootRf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler);
        searchFood=findViewById(R.id.searchRecipe);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        myFoodList= new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Recipe");
        listener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFoodList.clear();
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren())
                {
                    FoodMdel foodMdel1=itemSnapshot.getValue(FoodMdel.class);
                    myFoodList.add(foodMdel1);
                    progressDialog.dismiss();
                }
                adpter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
 adpter=new CookBookAdpter(MainActivity.this,myFoodList);
recyclerView.setAdapter(adpter);

searchFood.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    filetr(s.toString());
    }
});
    }

    private void filetr(String text) {
        ArrayList<FoodMdel>filterList=new ArrayList<>();
        for(FoodMdel item:myFoodList)
        {
         if(item.getItemName().toLowerCase().contains(text.toLowerCase()))
         {
             filterList.add(item);
         }
        }
        adpter.filteredList(filterList);
    }
// Edit

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.setting,menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.logout)
        {
            if(currentUser==null)
            {
                Toast.makeText(this, "User Already logout", Toast.LENGTH_SHORT).show();
            }
            else  {


                mAuth.signOut();
                Toast.makeText(this, "Logout Success..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }

        if(item.getItemId()==R.id.favourite)
        {
            if(currentUser==null)
            {
                Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else {
                FavouriteRecipeList();
            }
        }
        return  true;
    }

    private void FavouriteRecipeList() {
        favDialog=new Dialog(this);
        favDialog.setContentView(R.layout.mark_favorite);
        //  favDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_bordars));
        favDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        favDialog.setCancelable(true);
       RecyclerView recyclerVi=(RecyclerView)favDialog.findViewById(R.id.markerecipe);
        favDialog.show();
// Show Favorite Recipe ......
        GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,1);
        recyclerVi.setLayoutManager(gridLayoutManager);
      favolist= new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("FavRecipe")
        .child(currentUserId);
        listener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favolist.clear();
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren())
                {
                    FavoriteLitModel favoriteLitModel=itemSnapshot.getValue(FavoriteLitModel.class);
                    favolist.add(favoriteLitModel);
                }
                favoriteRecipeAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        favoriteRecipeAdpter=new FavoriteRecipeAdpter(MainActivity.this,favolist);
        recyclerVi.setAdapter(favoriteRecipeAdpter);


    }


    private void LoinActivity() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser!=null)
        {
            currentUserId=currentUser.getUid();
            //  Toast.makeText(this,"he"+ currentUserId, Toast.LENGTH_SHORT).show();
        }
      /*  if (currentUser==null)
        {

        }*/
    }
}