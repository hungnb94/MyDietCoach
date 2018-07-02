package com.hb.mydietcoach.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.WeightChangeHistory;
import com.hb.mydietcoach.utils.Constants;
import com.michael.easydialog.EasyDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class WeightHistoryAdapter extends RecyclerView.Adapter<WeightHistoryAdapter.ViewHolder> implements View.OnClickListener {
    private final String TAG = WeightHistoryAdapter.class.getSimpleName();

    private static final int PIN = 0;
    private static final int BIG_WEIGHT_GREEN = 1;
    private static final int BIG_WEIGHT_WHITE = 2;
    private static final int SMALL_WEIGHT_GREEN = 3;
    private static final int SMALL_WEIGHT_WHITE = 4;
    private static final int HALF_WEIGHT_GREEN = 5;

    private int MARGIN_DEFAULT;
    private int PIN_WIDTH;
    private int PIN_HEIGHT;
    private int BIG_WEIGHT_WIDTH;
    private int BIG_WEIGHT_HEIGHT;
    private int SMALL_WEIGHT_WIDTH;
    private int SMALL_WEIGHT_HEIGHT;
    private int HALF_WEIGHT_WIDTH;
    private int HALF_WEIGHT_HEIGHT;

    private float maxWidth; //Max width of RecyclerView

    private int itemCount;

    private List<WeightChangeHistory> histories;
    private List<List<ImageItemType>> lists;

    private Activity activity;


    public WeightHistoryAdapter(Activity activity, List<WeightChangeHistory> histories, float maxWidth) {
        this.activity = activity;
        this.histories = histories;
        this.maxWidth = maxWidth;

        //Calculate init width
        calculateDefaultWidth(activity);

        parseToList();

        itemCount = lists.size();
    }

    private void parseToList() {
        //Create temp list to save all
        List<ImageItemType> tempList = new ArrayList<>();
        int pos = 0;
        for (WeightChangeHistory wch : histories) {
            float weight = wch.getWeightChange();
            tempList.addAll(getItemsFromWeight(weight, pos));
            ++pos;
        }

        lists = new ArrayList<>();
        //Parse temp list to result
        List<ImageItemType> current = new ArrayList<>();
        float width = maxWidth;
        for (ImageItemType item : tempList) {
            if (width >= item.getWidth() + 2 * MARGIN_DEFAULT) {
                current.add(item);
                width = width - item.getWidth() - (2 * MARGIN_DEFAULT);
            } else {
                lists.add(0, current);
                width = maxWidth;
                current = new ArrayList<>();
            }
        }
        lists.add(0, current);
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weight_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.layout.removeAllViews();
        List<ImageItemType> itemTypes = lists.get(position);
        for (ImageItemType type : itemTypes) {
            ImageView imageView = type.getImageFromType(holder.itemView.getContext());

            //Set onclick for pin
            if (type.getType() == PIN) {
                imageView.setTag(histories.get(type.getHistoryPosition()));
                imageView.setOnClickListener(this);
            }

            holder.layout.addView(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View imageView) {
        WeightChangeHistory history = (WeightChangeHistory) imageView.getTag();

        @SuppressLint("InflateParams") View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_pin, null);

        TextView tvCurrentWeight = dialogView.findViewById(R.id.tvCurrentWeight);
        tvCurrentWeight.setText(activity.getString(R.string.weight)
                + " " + history.getCurentWeight()
                + " " + activity.getString(R.string.kgs));

        TextView tvDate = dialogView.findViewById(R.id.tvDate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(history.getTime());
        tvDate.setText(activity.getString(R.string.date)
                + " " + sdf.format(calendar.getTime()));

        TextView tvAmountWeightChange = dialogView.findViewById(R.id.tvAmountWeightChange);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        if (history.getWeightChange() >= 1) {
            tvAmountWeightChange.setText(activity.getString(R.string.gain)
                    + " " + numberFormat.format(history.getWeightChange())
                    + " " + activity.getString(R.string.kgs));
        } else if (history.getWeightChange() >= 0) {
            tvAmountWeightChange.setText(activity.getString(R.string.gain)
                    + " " + Math.round(history.getWeightChange() * 1000)
                    + " " + activity.getString(R.string.gr));
        } else if (history.getWeightChange() <= -1) {
            tvAmountWeightChange.setText(activity.getString(R.string.lost)
                    + " " + numberFormat.format(-history.getWeightChange())
                    + " " + activity.getString(R.string.kgs));
        } else {
            tvAmountWeightChange.setText(activity.getString(R.string.lost)
                    + " " + Math.round(-history.getWeightChange() * 1000)
                    + " " + activity.getString(R.string.gr));
        }

        final EasyDialog dialog = new EasyDialog(activity).setLayout(dialogView)
                .setGravity(EasyDialog.GRAVITY_TOP)
                .setLocationByAttachedView(imageView)
                .setBackgroundColor(activity.getResources().getColor(R.color.colorGuideline))
                .setCancelable(true)
                .setTouchOutsideDismiss(true);
        dialog.show();

        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.itemLinearLayout);
        }
    }

    private void calculateDefaultWidth(Context context) {
        MARGIN_DEFAULT = context.getResources().getDimensionPixelSize(R.dimen._3sdp);

        BIG_WEIGHT_WIDTH = context.getResources().getDimensionPixelOffset(R.dimen._30sdp);
        BIG_WEIGHT_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen._25sdp);

        SMALL_WEIGHT_WIDTH = context.getResources().getDimensionPixelSize(R.dimen._20sdp);
        SMALL_WEIGHT_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen._15sdp);

        HALF_WEIGHT_WIDTH = context.getResources().getDimensionPixelSize(R.dimen._20sdp);
        HALF_WEIGHT_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen._15sdp);

        PIN_WIDTH = context.getResources().getDimensionPixelSize(R.dimen._17sdp);
        PIN_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen._30sdp);
    }

    private List<ImageItemType> getItemsFromWeight(float weight, int historyPosition) {
        List<ImageItemType> list = new ArrayList<>();

        if (weight >= 0) {
            while (weight > 1) {
                list.add(new ImageItemType(BIG_WEIGHT_GREEN, historyPosition));
                weight = weight - 1;
            }
            while (weight > 0.1) {
                list.add(new ImageItemType(SMALL_WEIGHT_GREEN, historyPosition));
                weight = weight - .1f;
            }
            while (weight > 0.05) {
                list.add(new ImageItemType(HALF_WEIGHT_GREEN, historyPosition));
                weight = weight - .05f;
            }
        } else {
            weight = -weight;
            while (weight > 1) {
                list.add(new ImageItemType(BIG_WEIGHT_WHITE, historyPosition));
                weight = weight - 1;
            }
            while (weight > 0.1) {
                list.add(new ImageItemType(SMALL_WEIGHT_WHITE, historyPosition));
                weight = weight - .1f;
            }
        }
        list.add(new ImageItemType(PIN, historyPosition));

        return list;
    }

    class ImageItemType {

        int type;
        int historyPosition;

        ImageItemType(int type, int historyPosition) {
            setType(type);
            setHistoryPosition(historyPosition);
        }

        int getType() {
            if (type > 5 || type < 0) type = 0;
            return type;
        }

        void setType(int type) {
            if (type > 5 || type < 0) type = 0;
            this.type = type;
        }

        int getHistoryPosition() {
            return historyPosition;
        }

        void setHistoryPosition(int historyPosition) {
            this.historyPosition = historyPosition;
        }

        float getWidth() {
            switch (getType()) {
                case BIG_WEIGHT_GREEN:
                    return BIG_WEIGHT_WIDTH;
                case BIG_WEIGHT_WHITE:
                    return BIG_WEIGHT_WIDTH;
                case SMALL_WEIGHT_GREEN:
                    return SMALL_WEIGHT_WIDTH;
                case SMALL_WEIGHT_WHITE:
                    return SMALL_WEIGHT_WIDTH;
                case HALF_WEIGHT_GREEN:
                    return HALF_WEIGHT_WIDTH;
                case PIN:
                    return PIN_WIDTH;
                default:
                    return BIG_WEIGHT_WIDTH;
            }
        }

        float getHeight() {
            switch (getType()) {
                case BIG_WEIGHT_GREEN:
                    return BIG_WEIGHT_HEIGHT;
                case BIG_WEIGHT_WHITE:
                    return BIG_WEIGHT_HEIGHT;
                case SMALL_WEIGHT_GREEN:
                    return SMALL_WEIGHT_HEIGHT;
                case SMALL_WEIGHT_WHITE:
                    return SMALL_WEIGHT_HEIGHT;
                case HALF_WEIGHT_GREEN:
                    return HALF_WEIGHT_HEIGHT;
                default:
                    return BIG_WEIGHT_HEIGHT;
            }
        }

        ImageView getImageFromType(Context context) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params;
            switch (getType()) {
                case BIG_WEIGHT_GREEN:
                    imageView.setImageResource(R.drawable.weight_green);
                    params =
                            new LinearLayout.LayoutParams(BIG_WEIGHT_WIDTH, BIG_WEIGHT_HEIGHT);
                    params.setMargins(MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT);
                    imageView.setLayoutParams(params);
//                    return imageView;
                    break;
                case BIG_WEIGHT_WHITE:
                    imageView.setImageResource(R.drawable.weight_green_empty);
                    params = new LinearLayout.LayoutParams(BIG_WEIGHT_WIDTH, BIG_WEIGHT_HEIGHT);
                    params.setMargins(MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT);
                    imageView.setLayoutParams(params);
//                    return imageView;
                    break;
                case SMALL_WEIGHT_GREEN:
                    imageView.setImageResource(R.drawable.weight_green);
                    params = new LinearLayout.LayoutParams(SMALL_WEIGHT_WIDTH, SMALL_WEIGHT_HEIGHT);
                    params.setMargins(MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT);
                    imageView.setLayoutParams(params);
//                    return imageView;
                    break;
                case SMALL_WEIGHT_WHITE:
                    imageView.setImageResource(R.drawable.weight_green_empty);
                    params = new LinearLayout.LayoutParams(SMALL_WEIGHT_WIDTH, SMALL_WEIGHT_HEIGHT);
                    params.setMargins(MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT);
                    imageView.setLayoutParams(params);
                    return imageView;
                case HALF_WEIGHT_GREEN:
                    imageView.setImageResource(R.drawable.weight_green_half);
                    params = new LinearLayout.LayoutParams(HALF_WEIGHT_WIDTH, HALF_WEIGHT_HEIGHT);
                    params.setMargins(MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT);
                    imageView.setLayoutParams(params);
//                    return imageView;
                    break;
                default:
                    imageView.setImageResource(R.drawable.pin);
                    params = new LinearLayout.LayoutParams(PIN_WIDTH, PIN_HEIGHT);
                    params.setMargins(MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT);
                    imageView.setLayoutParams(params);
//                    return imageView;
                    break;
            }
            return imageView;
        }
    }
}
