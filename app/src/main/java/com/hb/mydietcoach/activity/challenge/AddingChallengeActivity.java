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
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.model.AnimationChallenge;
import com.hb.mydietcoach.model.Challenge;
import com.hb.mydietcoach.model.NormalChallenge;
import com.hb.mydietcoach.model.RunChallenge;
import com.hb.mydietcoach.model.SelfControlChallenge;
import com.hb.mydietcoach.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.hb.mydietcoach.utils.Constants.RC_NEW_MY_CHALLENGE;

public class AddingChallengeActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {
    public static final String NEW_MY_CHALLENGE_TITLE = "title";

    private String arr[];
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;

    private RecyclerView recyclerView;
    private AddingChallengeAdapter challengeAdapter;
    private List<Challenge> listExercise, listEatHabit, listSelfControl, listMyChallenge;
    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_challenge);

        arr = getResources().getStringArray(R.array.spinner_adding_challenge);
        listExercise = generateExerciseChallenges();
        listEatHabit = generateEatHabitChallenges();
        listSelfControl = generateSelfControlChallenges();
        database = new MyDatabase(this);
        listMyChallenge = database.getAllMyChallenge();

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
                String strTitle = data.getExtras().getString(NEW_MY_CHALLENGE_TITLE);
                Challenge challenge = new NormalChallenge(R.drawable.challenges_general_after,
                        strTitle,
                        Constants.STARS_FOR_MY_CHALLENGE,
                        8,
                        0,
                        getString(R.string.times),
                        Constants.CHALLENGE_TYPE_OF_MY);
                bundle.putSerializable(Constants.DATA_SERIALIZABLE, challenge);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    /**
     * Generate default exercise challenge list
     *
     * @return list exercise challenge
     */
    private List<Challenge> generateExerciseChallenges() {
        List<Challenge> list = new ArrayList<>();
        list.add(new NormalChallenge(R.drawable.challenges_gym1,
                getString(R.string.go_to_the_gym),
                Constants.STARS_FOR_GYM_EXERCISE,
                2,
                0,
                getString(R.string.times),
                Constants.CHALLENGE_TYPE_GYM));
        list.add(new NormalChallenge(R.drawable.challenges_pushups01_sh,
                getString(R.string.push_up_challenge_title),
                Constants.STARS_FOR_PUSH_UP,
                2,
                0,
                getString(R.string.sets),
                Constants.CHALLENGE_TYPE_PUSH_UP));
        list.add(new RunChallenge(R.drawable.challenges_walk1_sh,
                getString(R.string.walk_2_miles),
                Constants.STARS_FOR_WALK_A_MILE,
                2,
                0,
                getString(R.string.miles),
                0.01,
                Constants.CHALLENGE_TYPE_WALK_A_MILE));
        return list;
    }

    /**
     * Generate default eat habit challenge list
     *
     * @return list eat habit challenge
     */
    private List<Challenge> generateEatHabitChallenges() {
        List<Challenge> list = new ArrayList<>();
        list.add(new NormalChallenge(R.drawable.challenge_water_full,
                getString(R.string.drink_more_water),
                Constants.STARS_FOR_DRINK_WATER,
                8,
                0,
                getString(R.string.glasses),
                Constants.CHALLENGE_TYPE_DRINK_WATER));

        list.add(new AnimationChallenge(R.drawable.challenges_table_plate,
                getString(R.string.fill_my_plate),
                Constants.STARS_FOR_FILL_MY_PLATE,
                5,
                0,
                getString(R.string.meals),
                Constants.CHALLENGE_TYPE_FILL_MY_PLATE));
        return list;
    }

    /**
     * Generate default self control challenge list
     *
     * @return list self control challenge
     */
    private List<Challenge> generateSelfControlChallenges() {
        List<Challenge> list = new ArrayList<>();
        list.add(new SelfControlChallenge(R.drawable.junk_food_avoid,
                getString(R.string.avoid_junk_food),
                Constants.STARS_FOR_AVOID_JUNK_FOOD,
                Constants.CHALLENGE_TYPE_AVOID_JUNK_FOOD));
        list.add(new SelfControlChallenge(R.drawable.sugray_drink,
                getString(R.string.avoid_sugary_drinks),
                Constants.STARS_FOR_AVOID_SURGARY_DRINKS,
                Constants.CHALLENGE_TYPE_AVOID_SUGARY_DRINK));
        list.add(new SelfControlChallenge(R.drawable.snack,
                getString(R.string.avoid_snacking),
                Constants.STARS_FOR_AVOID_SNACKING,
                Constants.CHALLENGE_TYPE_AVOID_SNACKING));
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
