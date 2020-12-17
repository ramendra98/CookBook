package com.example.cookbookuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteRecipeAdpter extends RecyclerView.Adapter<FavoriteRecipeAdpter.holder> {
    private Context context;
    private List<FavoriteLitModel> list;

    public FavoriteRecipeAdpter(Context context, List<FavoriteLitModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.favouiret_recipe_item,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.recipeName.setText(list.get(position).getRecipeName());
        holder.decription.setText(list.get(position).getDecription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView recipeName,decription;
        public holder(@NonNull View itemView) {
            super(itemView);
            recipeName=itemView.findViewById(R.id.recipe);
            decription=itemView.findViewById(R.id.description);
        }
    }
}
