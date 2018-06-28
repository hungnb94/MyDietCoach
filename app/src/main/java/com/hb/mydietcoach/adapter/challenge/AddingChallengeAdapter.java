package com.hb.mydietcoach.adapter.challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.challenge.Challenge;
import com.hb.mydietcoach.utils.Constants;

import java.util.List;

public class AddingChallengeAdapter extends RecyclerView.Adapter<AddingChallengeAdapter.MyViewHolder> {
    private Activity activity;
    private List<Challenge> challenges;
    private LayoutInflater layoutInflater;

    public AddingChallengeAdapter(Activity activity, List<Challenge> challenges) {
        this.activity = activity;
        this.challenges = challenges;
        layoutInflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_adding_challenge, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Challenge challenge = challenges.get(position);
        holder.imageView.setImageResource(challenge.getImageId());
        holder.tvTitle.setText(challenge.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.DATA_SERIALIZABLE, challenge);
                intent.putExtras(bundle);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTitleChallenge);
        }
    }
}
