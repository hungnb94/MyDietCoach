package com.hb.mydietcoach.activity.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.Challenge;
import com.hb.mydietcoach.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EdittingChallengeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = EdittingChallengeActivity.class.getSimpleName();

    private static final int RC_ADD_CHALLENGE = 1;
    private ImageView ivType;
    private TextView tvTitle;

    private LinearLayout llAmount;
    private EditText edtAmount;
    private TextView tvAmount;

    private String arr[];
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private TextView tvUnit;
    private TextView tvAmountStars;

    //Challenge information
    private int itemType, numAmount;
    private String strChallengeTitle;
    private Challenge challenge;

    //Flicker effect after change challenge
    private Animation animAlpha;
    private LinearLayout llChallengeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editting_challenge);

        initParam();
        initView();
    }

    private void initParam() {
        arr = getResources().getStringArray(R.array.spinner_editting_challenge);

        Bundle bundle = getIntent().getExtras();
        itemType = bundle.getInt(ChallengesActivity.ITEM_TYPE, Constants.CHALLENGE_TYPE_DRINK_WATER);
        numAmount = bundle.getInt(ChallengesActivity.ITEM_AMOUNT, 8);
        strChallengeTitle = bundle.getString(ChallengesActivity.ITEM_TITLE, getString(R.string.drink_more_water));

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animAlpha.setDuration(400);
        animAlpha.setStartOffset(20);
        animAlpha.setRepeatMode(Animation.REVERSE);
        animAlpha.setRepeatCount(3);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.edit_challenges);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        ivType = findViewById(R.id.ivChallengeType);
        tvTitle = findViewById(R.id.tvTitleChallenge);
        llAmount = findViewById(R.id.llAmount);
        edtAmount = findViewById(R.id.edtAmountAction);
        tvAmount = findViewById(R.id.tvAmountChallenges);

        tvTitle.setText(strChallengeTitle);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(numAmount - 1);
        tvAmountStars = findViewById(R.id.tvAmountStars);

        tvAmountStars.setText(String.valueOf(numAmount * Constants.STARS_FOR_DRINK_WATER));

        tvUnit = findViewById(R.id.tvUnitChallenges);

        if (itemType == Constants.CHALLENGE_TYPE_DRINK_WATER) {
            ivType.setImageResource(R.drawable.challenge_water_full);
            tvUnit.setText(R.string.glasses);
            int numStars = numAmount * 3;
            tvAmountStars.setText(String.valueOf(numStars));
            llAmount.setVisibility(View.GONE);
        }

        llChallengeInfo = findViewById(R.id.llChallengeInfo);
    }

    @OnClick(R.id.btnSave)
    void clickSave(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(ChallengesActivity.ITEM_TYPE, challenge.getType());
        bundle.putInt(ChallengesActivity.ITEM_AMOUNT, spinner.getSelectedItemPosition() + 1);
        bundle.putString(ChallengesActivity.ITEM_TITLE, tvTitle.getText().toString());
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editting_challenge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_add_challenge) {
            Intent intent = new Intent(this, AddingChallengeActivity.class);
            startActivityForResult(intent, RC_ADD_CHALLENGE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_ADD_CHALLENGE && data != null) {
                Bundle bundle = data.getExtras();
                challenge = (Challenge) bundle.getSerializable(Constants.DATA_SERIALIZABLE);
                ivType.setImageResource(challenge.getImageId());
                tvTitle.setText(challenge.getTitle());
            }
            llChallengeInfo.startAnimation(animAlpha);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (itemType == Constants.CHALLENGE_TYPE_DRINK_WATER) {
            int numStars = (position + 1) * Constants.STARS_FOR_DRINK_WATER;
            tvAmountStars.setText(String.valueOf(numStars));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
