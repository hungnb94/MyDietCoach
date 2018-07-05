package com.hb.mydietcoach.activity.challenge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.adapter.challenge.AddingChallengeAdapter;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.model.challenge.Challenge;
import com.hb.mydietcoach.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.hb.mydietcoach.utils.Constants.RC_NEW_MY_CHALLENGE;

public class AddingChallengeActivity extends BaseActivity
        implements AdapterView.OnItemSelectedListener {
    private final String TAG = AddingChallengeActivity.class.getSimpleName();

    private String arr[];
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;

    private RecyclerView recyclerView;
    private AddingChallengeAdapter challengeAdapter;
    private List<Challenge> listExercise, listEatHabit, listSelfControl, listMyChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_challenge);

        arr = getResources().getStringArray(R.array.spinner_adding_challenge);

        listExercise = new ArrayList<>();
        listEatHabit = new ArrayList<>();
        listSelfControl = new ArrayList<>();
        listMyChallenge = new ArrayList<>();

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

        DatabaseTask task = new DatabaseTask();
        task.execute();
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
            startActivityForResult(intent, RC_NEW_MY_CHALLENGE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_NEW_MY_CHALLENGE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                Challenge challenge = (Challenge) data.getExtras().getSerializable(Constants.DATA_SERIALIZABLE);

                //Update adapter
//                listMyChallenge.add(challenge);
//                challengeAdapter.notifyDataSetChanged();

                bundle.putSerializable(Constants.DATA_SERIALIZABLE, challenge);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
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

    @SuppressLint("StaticFieldLeak")
    private class DatabaseTask extends AsyncTask<Void, Void, Void> {
        MyDatabase database;

        List<Challenge> exercises, eattingHabbits, selfControls, myChallenges;


        @Override
        protected Void doInBackground(Void... voids) {
            database = new MyDatabase(AddingChallengeActivity.this);

            exercises = database.getExerciseChallenges();
            eattingHabbits = database.getEatingHabitChallenges();
            selfControls = database.getSelfControlChallenges();
            myChallenges = database.getMyChallenges();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listExercise.clear();
            listExercise.addAll(exercises);

            listEatHabit.clear();
            listEatHabit.addAll(eattingHabbits);

            listSelfControl.clear();
            listSelfControl.addAll(selfControls);

            listMyChallenge.clear();
            listMyChallenge.addAll(myChallenges);

            challengeAdapter.notifyDataSetChanged();
        }
    }
}
