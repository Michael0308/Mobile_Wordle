package edu.cuhk.csci3310.wordle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Achievement> achievementList;

    public RecyclerAdapter(ArrayList<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private ImageView statusImage;

        public MyViewHolder(final View view) {
            super(view);
            nameText = view.findViewById(R.id.achievement_name);
            statusImage = view.findViewById(R.id.achievement_status);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String achievementName = achievementList.get(position).getName();
        Boolean achievementStatus = achievementList.get(position).getStatus();
        String achievementMessage = achievementList.get(position).getToastMessage();
        holder.nameText.setText(achievementName);
        holder.statusImage.setImageResource(achievementStatus ? R.drawable.check_done: R.drawable.check_not_done);
        holder.nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Unlock by: " + achievementMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }
}
