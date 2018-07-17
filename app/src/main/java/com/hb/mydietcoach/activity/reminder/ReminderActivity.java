package com.hb.mydietcoach.activity.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.RewardActivity;
import com.hb.mydietcoach.activity.setting.SettingsActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.contact_faq.ContactFAQActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.adapter.MyReminderAdapter;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.model.Reminder;
import com.hb.mydietcoach.notification.NotificationManager;
import com.hb.mydietcoach.utils.Constants;

import java.util.List;

import static com.hb.mydietcoach.utils.Constants.RC_ADD_REMINDER;
import static com.hb.mydietcoach.utils.Constants.RC_EDIT_REMINDER;

public class ReminderActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    //For my reminder
    SwipeMenuListView listMyReminders;
    MyReminderAdapter myReminderAdapter;
    List<Reminder> myReminders;

//    SwipeMenuListView listDefaultReminders;
//    MyReminderAdapter myDefaultReminders;
//    List<Reminder> defaultReminders;

    MyDatabase database;

    int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //Init variable
        database = new MyDatabase(this);

        //Init view
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reminder);

        initMyReminderListView();
    }

    //Init list view with my reminders data
    private void initMyReminderListView() {
        listMyReminders = findViewById(R.id.listMyReminder);

        myReminders = database.getAllReminder();
        myReminderAdapter = new MyReminderAdapter(this, myReminders);
        listMyReminders.setAdapter(myReminderAdapter);

        listMyReminders.setOnItemClickListener(myRemindersListener);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(ReminderActivity.this);
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

        listMyReminders.setMenuCreator(creator);
        // Right
        listMyReminders.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        listMyReminders.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                NotificationManager manager = new NotificationManager(ReminderActivity.this);
                manager.cancelAlarm((int) myReminders.get(position).getId());

                database.deleteReminder(myReminders.get(position).getId());
                myReminders.remove(position);
                myReminderAdapter.notifyDataSetChanged();
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    //Listener for event click on item from list view
    AdapterView.OnItemClickListener myRemindersListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            clickPosition = i;
            Intent intent = new Intent(ReminderActivity.this, EdittingReminderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(EdittingReminderActivity.IS_EDIT, true);
            bundle.putSerializable(Constants.DATA_SERIALIZABLE, myReminders.get(i));
            intent.putExtras(bundle);
            startActivityForResult(intent, RC_EDIT_REMINDER);
        }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_add_reminder) {
            Intent intent = new Intent(this, EdittingReminderActivity.class);
            startActivityForResult(intent, RC_ADD_REMINDER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_diary) {
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_log_weight) {
            Intent intent = new Intent(this, WeightLoggingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_reminder) {
            //Blank
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
            Intent intent = new Intent(this, RewardActivity.class);
            startActivity(intent);
            finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ADD_REMINDER) {
            if (resultCode == RESULT_OK && data != null) {
                Reminder reminder = (Reminder) data.getExtras().getSerializable(Constants.DATA_SERIALIZABLE);
                myReminders.add(reminder);
                myReminderAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == RC_EDIT_REMINDER) {
            if (resultCode == Constants.RESULT_DELETE_REMINDER) {
                myReminders.remove(clickPosition);
                myReminderAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_OK && data != null) {
                Reminder reminder = (Reminder) data.getExtras().getSerializable(Constants.DATA_SERIALIZABLE);
                myReminders.set(clickPosition, reminder);
                myReminderAdapter.notifyDataSetChanged();
            }
        }
    }
}
