package com.hb.mydietcoach.activity.tip;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.adapter.TipDetailAdapter;
import com.hb.mydietcoach.model.TipCategory;
import com.hb.mydietcoach.model.TipDetail;

import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TipDetailActivity extends AppCompatActivity implements TipDetailAdapter.IClickOnItemListener {
    private final String TAG = TipDetailActivity.class.getSimpleName();

    public static final String TIP_CATEGORY_ID = "id";
    public static final String TIP_TITLE = "title";
    public static final String TIP_IMAGE_RESOURCE = "image";

    private FrameLayout flArrow;

    private RealmList<TipDetail> list;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detail);

        Bundle bundle = getIntent().getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = bundle.getString(TIP_TITLE);
        getSupportActionBar().setTitle(title);


        //Get data from realm
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TipCategory> results = realm.where(TipCategory.class)
//                .equalTo("id", bundle.getInt(TIP_CATEGORY_ID))
                .findAll();

        list = new RealmList<>();

        Log.e(TAG, "All data from list");
        for (TipCategory category : results) {
            Log.e(TAG, category.getTitle());
            list.addAll(category.getAdvices());
        }
        for (TipDetail detail : list) {
            Log.e(TAG, detail.getMessage());
        }

        //Setting recycler view
        recyclerView = findViewById(R.id.recyclerView);

        //Setting adapter
        TipDetailAdapter adapter = new TipDetailAdapter(this, list, bundle.getInt(TIP_IMAGE_RESOURCE));
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Set up view pager
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Using for hide/show next/back icon
    private Handler handler;
    private Runnable arrowRunnable = new Runnable() {
        @Override
        public void run() {
            flArrow.setVisibility(View.GONE);
        }
    };

    @Override
    public void clickOnItem() {
        flArrow.setVisibility(View.VISIBLE);
        if (handler == null) handler = new Handler();
        handler.removeCallbacks(arrowRunnable);
        new Handler().postDelayed(arrowRunnable, 5000);
    }

    @OnClick(R.id.ivBack)
    void clickBack() {
    }
}
