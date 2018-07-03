package com.hb.mydietcoach.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hb.mydietcoach.R;
import com.hb.mydietcoach.utils.MyUtils;

import java.io.File;
import java.util.List;

public class MiniPhotoAdapter extends RecyclerView.Adapter<MiniPhotoAdapter.ViewHolder> {
    private final String TAG = MiniPhotoAdapter.class.getSimpleName();

    private Context context;
    private List<File> list;
    private LayoutInflater inflater;

    private OnItemClickListener listener;

    public MiniPhotoAdapter(Context context, List<File> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_small_photo, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final File imgFile = list.get(position);
        try {
            Glide.with(context)
                    .load(imgFile)
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onItemClick(position);
                }
            });
        } catch (OutOfMemoryError error) {
            MyUtils.showToast(context, context.getString(R.string.out_of_memory));
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
        } catch (Exception e) {
            MyUtils.showToast(context, e.getMessage());
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
