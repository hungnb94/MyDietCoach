package com.hb.mydietcoach.adapter.tip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.TipDetail;

import io.realm.RealmList;

public class TipDetailAdapter extends RecyclerView.Adapter<TipDetailAdapter.ViewHolder> {
    private IClickOnItemListener listener;

    private RealmList<TipDetail> list;
    private int imgResId;
    private LayoutInflater inflater;

    public TipDetailAdapter(Context context, RealmList<TipDetail> list, int imgResId) {
        this.list = list;
        this.imgResId = imgResId;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_tip_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(imgResId);
        holder.textView.setText(list.get(position).getMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.clickOnItem();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivTipType);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public interface IClickOnItemListener {
        void clickOnItem();
    }

    public void setListener(IClickOnItemListener listener) {
        this.listener = listener;
    }
}
