package com.hb.mydietcoach.activity.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.activity.ContactFAQActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.SettingsActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.adapter.diary.DiaryActivityAdapter;
import com.hb.mydietcoach.adapter.diary.SearchingFoodAdapter;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.model.diary.Exercise;
import com.hb.mydietcoach.model.diary.Food;
import com.hb.mydietcoach.model.diary.FoodAssets;
import com.hb.mydietcoach.model.diary.IItemDiary;
import com.hb.mydietcoach.notification.NotificationManager;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;
import com.hb.mydietcoach.utils.FileUtils;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

import static com.hb.mydietcoach.utils.Constants.RC_ADD_EXERCISE;
import static com.hb.mydietcoach.utils.Constants.RC_EDIT_MEAL_HISTORY;

public class DiaryActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = DiaryActivity.class.getSimpleName();

    private DrawerLayout drawer;

    //List view
    private SwipeMenuListView listView;
    private DiaryActivityAdapter adapter;
    private List<IItemDiary> listItems = new ArrayList<>();
    private MyDatabase database;
    private SimpleDateFormat format;

    //Info at bottom
//    private ImageView ivMoreDiary;
//    private LinearLayout llContentScrollView;

    //Function add meal
    private FloatingActionButton fab;
    private LinearLayout llAddMeal, llDetailInput;
    private ImageView ivBarcode;
    private AutoCompleteTextView autoFoodName;
    private Animation rotationAnimation;
    private ArrayList<FoodAssets> foodAssets;
    private List<FoodAssets> listSearchingFood;
    private SearchingFoodAdapter searchingAdapter;
    private EditText edtAmount, edtCalories;

    //Function change date
    private Calendar currentDate, todayDate;
    private TextView tvCurrentDate;

    //Function guideline for new user
    ToolTipsManager toolTipsManager;
    RelativeLayout rootLayout;
    private PreferenceManager pre;
    boolean isFirstTimeAddMeal;

    private float caloriesLeft, caloriesConsumed, caloriesBurned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //Check first time open diary
        pre = new PreferenceManager(this);
        boolean isFirstOpenDiary = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_OPEN_DIARY, true);
        if (isFirstOpenDiary) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(PreferenceManager.IS_FIRST_TIME_OPEN_DIARY, true);
            startActivity(intent);
            finish();
        }

        initParams();
        initView();
    }

    /**
     * Init all params needed
     */
    @SuppressLint("SimpleDateFormat")
    private void initParams() {
        currentDate = Calendar.getInstance();
        todayDate = Calendar.getInstance();
        database = new MyDatabase(this);
        rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        format = new SimpleDateFormat(Constants.DATE_FORMAT);
        toolTipsManager = new ToolTipsManager();
        isFirstTimeAddMeal = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_ADD_MEAL, true);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.diet_diary);
        ButterKnife.bind(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_diary);

        fab = findViewById(R.id.fab);
        //TODO: EVENT FOR IV SHOW MORE DIARY
//        ivMoreDiary = findViewById(R.id.ivMoreDiary);
//        llContentScrollView = findViewById(R.id.llContentScrollView);
        llDetailInput = findViewById(R.id.llDetailInputDiary);

        //Function add meal
        llAddMeal = findViewById(R.id.llAddMeal);
        ivBarcode = findViewById(R.id.ivBarcode);
        autoFoodName = findViewById(R.id.autoFoodName);
        edtAmount = findViewById(R.id.edtAmount);
        edtCalories = findViewById(R.id.edtCalories);

        //Function change current date
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        setCurrentDate();

        //Function guideline
        rootLayout = findViewById(R.id.rootLayout);

        initFromDatabase();
        initAutoCompleteTextView();
    }

    @SuppressLint("StaticFieldLeak")
    private void initFromDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String strCurrentDate = format.format(currentDate.getTime());
                listItems.clear();
                listItems.addAll(database.findDiaryItemByDate(strCurrentDate));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                initSwipeListView();

                initDetailCalories();
            }
        }.execute();
    }

    /**
     * Init SwipeListView
     */
    private void initSwipeListView() {
        listView = findViewById(R.id.listView);
        adapter = new DiaryActivityAdapter(this, listItems);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(DiaryActivity.this);
                // set item background
                deleteItem.setBackground(R.color.colorAccent);
                // set item width
                deleteItem.setWidth((int) getResources().getDimension(R.dimen.menu_item_delete_width));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_l_style);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Object object = listItems.get(position);
                if (object instanceof Exercise) {
                    Exercise exercise = (Exercise) object;
                    if (exercise.isReminder()) {
                        NotificationManager manager = new NotificationManager(DiaryActivity.this);
                        manager.cancelAlarm(exercise.getId());
                    }
                }

                database.deleteItem(listItems.get(position).getId());
                listItems.remove(position);
                adapter.notifyDataSetChanged();
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DiaryActivity.this, AddExerciseActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Init view in bottom
     * with full information about calories: left, burned, consumed
     */
    private void initDetailCalories() {
        caloriesLeft = pre.getInt(PreferenceManager.DAILY_CALORIES_GOAL, 1500);

        caloriesConsumed = 0;
        caloriesBurned = 0;
        for (int i = 0; i < listItems.size(); i++) {
            Object object = listItems.get(i);
            if (object instanceof Food) {
                Food food = (Food) object;
                float calo = parseFloats(food.getCalories());
                caloriesConsumed += calo;

                caloriesLeft -= calo;
            } else if (object instanceof Exercise) {
                Exercise exercise = (Exercise) object;
                float calo = parseFloats(exercise.getCalories());
                caloriesBurned += calo;

                caloriesLeft += calo;
            }
        }

        ((TextView) findViewById(R.id.tvCaloriesLeft)).setText(String.valueOf(Math.round(caloriesLeft)));
        ((TextView) findViewById(R.id.tvCaloresBurned)).setText(String.valueOf(Math.round(caloriesBurned)));
        ((TextView) findViewById(R.id.tvCaloriesConsumed)).setText(String.valueOf(Math.round(caloriesConsumed)));
    }

    /**
     * Init AutoCompleteTextView
     */
    private void initAutoCompleteTextView() {
        getFoodFromAssets();
        listSearchingFood = new ArrayList<>();
        searchingAdapter = new SearchingFoodAdapter(this, listSearchingFood);
        autoFoodName.setAdapter(searchingAdapter);
        autoFoodName.setThreshold(1);
    }

    /**
     * Get all food from assets file
     */
    @SuppressLint("StaticFieldLeak")
    private void getFoodFromAssets() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodAssets = new ArrayList<>();
                String content = FileUtils.readFileFromAssets(DiaryActivity.this, "new_food_db_1.txt");
                StringTokenizer tokenizer = new StringTokenizer(content, "\n");
                while (tokenizer.hasMoreTokens()) {
                    try {
                        String mLine = tokenizer.nextToken();
                        mLine = mLine.substring(8, mLine.length() - 1);

                        //Convert text to object
                        Gson gson = new Gson();
                        FoodAssets food = gson.fromJson(mLine, FoodAssets.class);

                        //Add to list
                        foodAssets.add(food);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception");
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    //State focus on auto text view in first time
    boolean isFocusAutoTextView = false;

    //Event focus on AutoCompleteTextView
    @OnFocusChange(R.id.autoFoodName)
    public void focusAutoTextView(View view, boolean focus) {
        if (!isFocusAutoTextView && focus) {
            isFocusAutoTextView = true;
            llDetailInput.setVisibility(View.VISIBLE);
            ivBarcode.setImageResource(R.drawable.widget_arrow);
        }
    }

    //Event change text on AutoCompleteTextView
    @OnTextChanged(R.id.autoFoodName)
    public void changeTextAutoTextView(CharSequence charSequence) {
        if (llDetailInput.getVisibility() != View.VISIBLE)
            llDetailInput.setVisibility(View.VISIBLE);

        listSearchingFood.clear();
        Log.d(TAG, "Find food for auto text view:");
        for (FoodAssets food : foodAssets) {
            if (food.getFn().contains(charSequence)) {
                listSearchingFood.add(food);
            }
        }
        searchingAdapter.notifyDataSetChanged();
    }

    /**
     * Click add event to show AddingExerciseActivity
     */
    @OnClick(R.id.llExercise)
    public void addExercise() {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        startActivityForResult(intent, RC_ADD_EXERCISE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_EXERCISE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            assert bundle != null;
            Exercise exercise = (Exercise) bundle.getSerializable(Constants.DATA_SERIALIZABLE);
            listItems.add(exercise);
            adapter.notifyDataSetChanged();

            updateDetailInformation(exercise);
        } else if (requestCode == RC_EDIT_MEAL_HISTORY) {
            initFromDatabase();
        }
    }

    /**
     * Click add food button
     * To show/hidden adding food layout
     */
    @OnClick(R.id.fab)
    public void addFood() {
        if (Objects.requireNonNull(getSupportActionBar()).isShowing()) {
            getSupportActionBar().hide();
            llAddMeal.setVisibility(View.VISIBLE);
            llDetailInput.setVisibility(View.GONE);
        } else {
            resetAddingFoodLayout();
        }
        //start animation for beautiful UI
        fab.startAnimation(rotationAnimation);
        new Handler().postDelayed(fabRunable, Constants.ANIMATION_LENGTH);
    }

    /**
     * Click add new meal
     * Save to database and update UI
     */
    @OnClick(R.id.ivAddMeal)
    public void addMeal() {
        String foodName = autoFoodName.getText().toString();
        String amount = edtAmount.getText().toString();
        String calories = edtCalories.getText().toString();
        if (TextUtils.isEmpty(foodName) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(calories)) {
            Toast.makeText(this, getString(R.string.input_full_info), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(Constants.FULL_DATE_FORMAT);
        String time = sdf.format(Calendar.getInstance().getTime());
        Food food = new Food(0, foodName, calories, amount, time);
        long id = database.insertFood(food);
        food.setId(id);
        listItems.add(food);
        adapter.notifyDataSetChanged();
        resetAddingFoodLayout();
        fab.startAnimation(rotationAnimation);
        new Handler().postDelayed(fabRunable, Constants.ANIMATION_LENGTH);
        if (isFirstTimeAddMeal) {
            //Show guideline
            View item = listView.getChildAt(0);
            ToolTip.Builder builder = new ToolTip.Builder(this, item, rootLayout,
                    getString(R.string.swipe_to_delete), ToolTip.POSITION_BELOW);
            builder.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGuideline));
            toolTipsManager.show(builder.build());
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_ADD_MEAL, false);
            isFirstTimeAddMeal = false;
        }

        updateDetailInformation(food);
    }

    private void updateDetailInformation(IItemDiary itemDiary) {
        if (itemDiary instanceof Food) {
            float calo = parseFloats(((Food) itemDiary).getCalories());
            caloriesLeft -= calo;
            caloriesConsumed += calo;
            ((TextView) findViewById(R.id.tvCaloriesConsumed)).setText(String.valueOf(Math.round(caloriesConsumed)));
        } else if (itemDiary instanceof Exercise) {
            float calo = parseFloats(((Exercise) itemDiary).getCalories());
            caloriesLeft += calo;
            caloriesBurned += calo;
            ((TextView) findViewById(R.id.tvCaloresBurned)).setText(String.valueOf(Math.round(caloriesBurned)));
        }
        ((TextView) findViewById(R.id.tvCaloriesLeft)).setText(String.valueOf(Math.round(caloriesLeft)));
    }

    /**
     * Reset adding food layout
     */
    private void resetAddingFoodLayout() {
        Objects.requireNonNull(getSupportActionBar()).show();
        llAddMeal.setVisibility(View.GONE);
        ivBarcode.setImageResource(R.drawable.barcode_icon);
        isFocusAutoTextView = false;
        autoFoodName.setText("");
        edtAmount.setText("");
        edtCalories.setText("");
    }

    //Click previous date
    @OnClick(R.id.ivPrevious)
    public void previousDate(View view) {
        currentDate.add(Calendar.DATE, -1);
        setCurrentDate();

        initFromDatabase();
    }

    //Click next date
    @OnClick(R.id.ivNext)
    public void nextDate(View view) {
        currentDate.add(Calendar.DATE, 1);
        setCurrentDate();

        initFromDatabase();
    }

    /**
     * Click show more information about daily target
     * Show/hidden full information
     */
//    @OnClick(R.id.ivMoreDiary)
//    public void showMore() {
//        //Block when add meal layout is visible
//        if (llAddMeal.getVisibility() == View.VISIBLE) return;
//
//        if (llContentScrollView.getVisibility() == View.VISIBLE) {
//            llContentScrollView.setVisibility(View.GONE);
//            ivMoreDiary.setRotation(180);
//        } else {
//            llContentScrollView.setVisibility(View.VISIBLE);
//            ivMoreDiary.setRotation(0);
//        }
//    }

    //Runnable handle click add food button (for beautiful UI) - not nessesary
    Runnable fabRunable = new Runnable() {
        @Override
        public void run() {
            if (Objects.requireNonNull(getSupportActionBar()).isShowing()) {
                fab.setImageResource(R.drawable.quick_meal_icon);
            } else {
                fab.setImageResource(R.drawable.ic_close_f_btn);
            }
            fab.startAnimation(rotationAnimation);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_meal_history) {
            editMealHistory();
        } else if (id == R.id.action_profile) {
            openProfileActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Open ProfileActivity
     */
    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Open EdittingMealHistoryActivity
     */
    private void editMealHistory() {
        Intent intent = new Intent(this, EdittingMealHistoryActivity.class);
        startActivityForResult(intent, RC_EDIT_MEAL_HISTORY);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_diary) {
            //Blank
        } else if (id == R.id.nav_log_weight) {
            Intent intent = new Intent(this, WeightLoggingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_reminder) {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_photos) {
            Intent intent = new Intent(this, PhotosActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_tips) {
            Intent intent = new Intent(this, TipsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_challenges) {
            Intent intent = new Intent(this, ChallengesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_rewards) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactFAQActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Set current date for tvCurrentDate
     */
    @SuppressLint("SimpleDateFormat")
    private void setCurrentDate() {
        String strCurrentDate;
        SimpleDateFormat sdf;
        if (todayDate.equals(currentDate)) {
            sdf = new SimpleDateFormat(Constants.FORMAT_TODAY_DATE);
            strCurrentDate = getString(R.string.today) + ", " + sdf.format(currentDate.getTime());
        } else {
            sdf = new SimpleDateFormat(Constants.FORMAT_NORMAL_DATE);
            strCurrentDate = sdf.format(currentDate.getTime());
        }
        tvCurrentDate.setText(strCurrentDate);
    }

    private float parseFloats(String number) {
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            Log.e(TAG, "Parse float exception: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
