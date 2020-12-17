package com.example.cookbookuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdpter extends RecyclerView.Adapter<CommentAdpter.ComentHolder> {
    private Context context;
    private List<CommentModel> list;

    public CommentAdpter(Context context, List<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ComentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_post,parent,false);
        return new ComentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentHolder holder, int position) {
holder.name.setText(list.get(position).getName());
holder.message.setText(list.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ComentHolder extends RecyclerView.ViewHolder {
        private TextView name,message;
        public ComentHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            message=itemView.findViewById(R.id.message);
        }
    }
}
