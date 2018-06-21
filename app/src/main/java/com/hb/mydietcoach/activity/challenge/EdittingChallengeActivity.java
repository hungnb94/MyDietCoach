package com.hb.mydietcoach.activity.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.hb.mydietcoach.model.NormalChallenge;
import com.hb.mydietcoach.model.RunChallenge;
import com.hb.mydietcoach.model.SelfControlChallenge;
import com.hb.mydietcoach.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.hb.mydietcoach.utils.Constants.RC_ADD_CHALLENGE;

public class EdittingChallengeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = EdittingChallengeActivity.class.getSimpleName();

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
    private TextView tvAllDay;

    //Challenge information
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
        challenge = (Challenge) bundle.getSerializable(Constants.DATA_SERIALIZABLE);

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

        tvAmountStars = findViewById(R.id.tvAmountStars);

        spinner = findViewById(R.id.spinner);
        tvAllDay = findViewById(R.id.tvAllDay);

        tvUnit = findViewById(R.id.tvUnitChallenges);

        llChallengeInfo = findViewById(R.id.llChallengeInfo);

        updateUI();
    }

    private void updateUI() {
        ivType.setImageResource(challenge.getImageId());
        tvTitle.setText(challenge.getTitle());

        if (challenge instanceof NormalChallenge) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
            spinner.setAdapter(adapter);
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(this);
            int total = ((NormalChallenge) challenge).getTotalCount();
            spinner.setSelection(total - 1);
            tvAmountStars.setText(String.valueOf(total * challenge.getStars()));
            tvUnit.setVisibility(View.VISIBLE);
            tvUnit.setText(((NormalChallenge) challenge).getUnit());

            llAmount.setVisibility(View.GONE);
            tvAllDay.setVisibility(View.GONE);
        } else if (challenge instanceof RunChallenge) {
            tvAmount.setText(((RunChallenge) challenge).getUnit());
            spinner.setVisibility(View.GONE);
            tvAllDay.setVisibility(View.VISIBLE);
            llAmount.setVisibility(View.VISIBLE);
            double leng = ((RunChallenge) challenge).getTotalLength();
            edtAmount.setText(String.valueOf(leng));
            int stars = (int) (leng * challenge.getStars());
            tvAmountStars.setText(String.valueOf(stars));
            tvUnit.setVisibility(View.INVISIBLE);
        } else if (challenge instanceof SelfControlChallenge) {
            llAmount.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            tvAllDay.setVisibility(View.VISIBLE);
            tvUnit.setVisibility(View.INVISIBLE);
            tvAmountStars.setText(String.valueOf(challenge.getStars()));
        }
    }

    @OnTextChanged(R.id.edtAmountAction)
    void textChange(CharSequence text) {
        try {
            double leng = Double.valueOf(text.toString());
            if (leng > 0) {
                int stars = (int) (leng * challenge.getStars());
                tvAmountStars.setText(String.valueOf(stars));

                if (challenge instanceof RunChallenge) {
                    ((RunChallenge) challenge).setTotalLength(leng);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Input leng wrong: " + text);
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnSave)
    void clickSave() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA_SERIALIZABLE, challenge);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
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
                updateUI();
            }
            llChallengeInfo.startAnimation(animAlpha);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (challenge instanceof NormalChallenge) {
            ((NormalChallenge) challenge).setTotalCount(position + 1);
            int numStars = (position + 1) * challenge.getStars();
            tvAmountStars.setText(String.valueOf(numStars));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
