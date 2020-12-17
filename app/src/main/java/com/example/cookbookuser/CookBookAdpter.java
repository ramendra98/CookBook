package com.example.cookbookuser;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CookBookAdpter extends RecyclerView.Adapter<CookBookAdpter.MyCookViewHolder> {
    private Context context;
    private List<FoodMdel>myFoodlist;
    private int lastPosition=-1;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public CookBookAdpter(Context context, List<FoodMdel> myFoodlist) {
        this.context = context;
        this.myFoodlist = myFoodlist;
    }

    @NonNull
    @Override
    public MyCookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout,parent,false);

        return new MyCookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCookViewHolder holder, int position) {

        Glide.with(context)

                .load(myFoodlist.get(position).getItemImage())
                .centerCrop()
                .into(holder.imageView);
//holder.imageView.setImageResource(myFoodlist.get(position).getItemImage());
holder.itemName.setText(myFoodlist.get(position).getItemName());
holder.description.setText(myFoodlist.get(position).getItemDescription());
holder.prices.setText("Rs."+myFoodlist.get(position).getItemPrices());
holder.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

   if(currentUser==null)
   {
       Intent intent=new Intent(context,Login.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
       context.startActivity(intent);

   }
   else {
       Intent intent = new Intent(context, DetailActivity.class);
       intent.putExtra("Image", myFoodlist.get(holder.getAdapterPosition()).getItemImage());
       intent.putExtra("Description", myFoodlist.get(holder.getAdapterPosition()).getItemDescription());
       intent.putExtra("Name", myFoodlist.get(holder.getAdapterPosition()).getItemName());
       intent.putExtra("Prices", myFoodlist.get(holder.getAdapterPosition()).getItemPrices());
       context.startActivity(intent);
   }


    }
});
setAnimation(holder.itemView,position);

    }



    public  void setAnimation(View viewToAnimate,int position)
    {
if(position>lastPosition)
{
    ScaleAnimation animation=new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
            Animation.RELATIVE_TO_SELF,0.5f,
            Animation.RELATIVE_TO_SELF,0.5f
            );
    animation.setDuration(1500);
    viewToAnimate.startAnimation(animation);
    lastPosition=position;
}
    }

    @Override
    public int getItemCount() {
        return myFoodlist.size();
    }

    public void filteredList(ArrayList<FoodMdel> filterList) {
        myFoodlist=filterList;
        notifyDataSetChanged();
    }

    class MyCookViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView prices,description,itemName;
        CardView cardView;
        public MyCookViewHolder( View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            prices=itemView.findViewById(R.id.prices);
            imageView=itemView.findViewById(R.id.img);
            cardView=itemView.findViewById(R.id.myCard);

        }
    }

}
