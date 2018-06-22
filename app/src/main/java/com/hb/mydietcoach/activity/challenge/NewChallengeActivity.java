package com.hb.mydietcoach.activity.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.model.NormalChallenge;
import com.hb.mydietcoach.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewChallengeActivity extends AppCompatActivity {

    private MyDatabase database;
    private EditText editText;
    //Funcion alert
    private LinearLayout llAlert;
    private TextView tvAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_challenge);

        database = new MyDatabase(this);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.create_challenge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = findViewById(R.id.editText);
        //Alert function
        llAlert = findViewById(R.id.llAlert);
        tvAlert = findViewById(R.id.tvAlert);
        tvAlert.setText(R.string.you_did_not_enter_challenge_text);

        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnSave)
    void clickSave(View view) {
        String strContent = editText.getText().toString();
        if (TextUtils.isEmpty(strContent)){
            llAlert.setVisibility(View.VISIBLE);
            return;
        }
        NormalChallenge challenge = new NormalChallenge();
        challenge.setTotalCount(Constants.DEFAULT_MY_CHALLENGE)
                .setUnit(getString(R.string.times))
                .setImageId(R.drawable.challenges_general_after)
                .setTitle(strContent)
                .setStars(Constants.STARS_FOR_MY_CHALLENGE)
                .setType(Constants.CHALLENGE_TYPE_OF_MY);
        long id =database.insertChallenge(challenge);
        challenge.setId(id);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(AddingChallengeActivity.NEW_MY_CHALLENGE_TITLE, strContent);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
