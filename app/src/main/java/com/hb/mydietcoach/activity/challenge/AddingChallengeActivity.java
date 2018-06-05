package com.hb.mydietcoach.activity.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.adapter.AddingChallengeAdapter;
import com.hb.mydietcoach.model.Challenge;
import com.hb.mydietcoach.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddingChallengeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String arr[];
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;

    private RecyclerView recyclerView;
    private AddingChallengeAdapter challengeAdapter;
    private List<Challenge> listExercise, listEatHabit, listSelfControl, listMyChallenge;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_challenge);

        arr = getResources().getStringArray(R.array.spinner_adding_challenge);
        listExercise = generateExerciseChallenges();
        listEatHabit = generateEatHabitChallenges();
        listSelfControl = generateSelfControlChallenges();
        realm = Realm.getDefaultInstance();
        RealmResults<Challenge> realmResults = realm.where(Challenge.class).findAll();
        listMyChallenge = new ArrayList<>();
        listMyChallenge.addAll(realmResults);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_challenge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        spinner = findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        challengeAdapter = new AddingChallengeAdapter(this, listExercise);
        recyclerView.setAdapter(challengeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adding_challenge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.action_new_challenge) {
            Intent intent = new Intent(this, NewChallengeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public List<Challenge> generateExerciseChallenges() {
        List<Challenge> list = new ArrayList<>();
        list.add(new Challenge(R.drawable.challenges_pushups01_sh,
                getString(R.string.push_up_challenge_title),
                Constants.CHALLENGE_TYPE_PUSH_UP));
        list.add(new Challenge(R.drawable.challenges_gym1,
                getString(R.string.gym_challenge_title),
                Constants.CHALLENGE_TYPE_GYM));
        return list;
    }

    public List<Challenge> generateEatHabitChallenges() {
        List<Challenge> list = new ArrayList<>();
        list.add(new Challenge(R.drawable.challenge_water_full,
                getString(R.string.drink_more_water),
                Constants.CHALLENGE_TYPE_DRINK_WATER));
        list.add(new Challenge(R.drawable.challenges_gym1,
                getString(R.string.gym_challenge_title),
                Constants.CHALLENGE_TYPE_GYM));
        return list;
    }

    public List<Challenge> generateSelfControlChallenges() {
        List<Challenge> list = new ArrayList<>();
        list.add(new Challenge(R.drawable.challenges_pushups01_sh,
                getString(R.string.push_up_challenge_title),
                Constants.CHALLENGE_TYPE_PUSH_UP));
        list.add(new Challenge(R.drawable.challenges_gym1,
                getString(R.string.gym_challenge_title),
                Constants.CHALLENGE_TYPE_GYM));
        return list;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0) {
            challengeAdapter = new AddingChallengeAdapter(this, listExercise);
        } else if (position == 1) {
            challengeAdapter = new AddingChallengeAdapter(this, listEatHabit);
        } else if (position == 2) {
            challengeAdapter = new AddingChallengeAdapter(this, listSelfControl);
        } else if (position == 3) {
            challengeAdapter = new AddingChallengeAdapter(this, listMyChallenge);
        }
        recyclerView.setAdapter(challengeAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
