package com.hb.mydietcoach.activity.diary;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.hb.mydietcoach.R;
import com.hb.mydietcoach.adapter.diary.DiaryActivityAdapter;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.model.diary.IItemDiary;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;

public class EdittingMealHistoryActivity extends AppCompatActivity {
    private static final String TAG = EdittingMealHistoryActivity.class.getSimpleName();

    private SwipeMenuListView listView;
    private DiaryActivityAdapter adapter;
    private List<IItemDiary> listItems;
    private MyDatabase database;

    //Function show guideline
    PreferenceManager pre;
    RelativeLayout relativeLayout;
    ToolTipsManager toolTipsManager;
    boolean isFirstTimeDeleteMeal;

    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editting_meal_history);

        database = new MyDatabase(this);
        pre = new PreferenceManager(this);
        isFirstTimeDeleteMeal = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_DELETE_MEAL, true);
        sdf = new SimpleDateFormat(Constants.FULL_DATE_FORMAT);

        toolTipsManager = new ToolTipsManager();

        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (isFirstTimeDeleteMeal && listItems.size() > 0) {
            Log.e(TAG, "Show tool tips");
            //Show guideline
            View item = listView.getChildAt(0);
            ToolTip.Builder builder = new ToolTip.Builder(this, item, relativeLayout,
                    getString(R.string.swipe_to_delete), ToolTip.POSITION_BELOW);
            builder.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGuideline));
            toolTipsManager.show(builder.build());
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_DELETE_MEAL, false);
            isFirstTimeDeleteMeal = false;
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.edit_meals_history));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        //Function guideline
        relativeLayout = findViewById(R.id.rootLayout);

        listView = findViewById(R.id.listView);
        listItems = database.getAllFood();
        adapter = new DiaryActivityAdapter(this, listItems);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(EdittingMealHistoryActivity.this);
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
                Toast.makeText(EdittingMealHistoryActivity.this,
                        getString(R.string.item_was_deleted),
                        Toast.LENGTH_SHORT)
                        .show();
                database.deleteItem(listItems.get(position).getId());
                listItems.remove(position);
                adapter.notifyDataSetChanged();
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
