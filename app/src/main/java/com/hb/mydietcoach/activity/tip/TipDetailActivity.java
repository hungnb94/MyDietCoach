package com.hb.mydietcoach.activity.tip;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.adapter.TipDetailAdapter;
import com.hb.mydietcoach.model.TipCategory;
import com.hb.mydietcoach.model.TipDetail;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;

public class TipDetailActivity extends AppCompatActivity implements TipDetailAdapter.IClickOnItemListener {
    private final String TAG = TipDetailActivity.class.getSimpleName();

    public static final String TIP_CATEGORY_ID = "id";
    public static final String TIP_TITLE = "title";
    public static final String TIP_IMAGE_RESOURCE = "image";

    private Bundle bundle;

    private FrameLayout flArrow;

    private RealmList<TipDetail> list;

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detail);

        ButterKnife.bind(this);

        bundle = getIntent().getExtras();

        //Get data from realm
        Realm realm = Realm.getDefaultInstance();
        TipCategory results = realm.where(TipCategory.class)
                .equalTo(TipCategory.ID, bundle.getInt(TIP_CATEGORY_ID))
                .findFirst();

        list = new RealmList<>();
        list.addAll(Objects.requireNonNull(results).getAdvices());
        initView();
    }

    private void initView() {
        //set up tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String title = Objects.requireNonNull(bundle).getString(TIP_TITLE);
        getSupportActionBar().setTitle(title);

        flArrow = findViewById(R.id.flArrow);
        //Setting recycler view
        recyclerView = findViewById(R.id.recyclerView);

        //Setting adapter
        TipDetailAdapter adapter = new TipDetailAdapter(this, list, bundle.getInt(TIP_IMAGE_RESOURCE));
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapter.setListener(this);
        recyclerView.setLayoutManager(manager);

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
    private Handler handler = new Handler();
    private Runnable arrowRunnable = new Runnable() {
        @Override
        public void run() {
            flArrow.setVisibility(View.GONE);
        }
    };

    @Override
    public void clickOnItem() {
        flArrow.setVisibility(View.VISIBLE);
        handler.removeCallbacks(arrowRunnable);
        new Handler().postDelayed(arrowRunnable, 5000);
    }

    @OnClick(R.id.ivBack)
    void clickBack() {
        int position = manager.findFirstVisibleItemPosition();
        if (position > 0) manager.smoothScrollToPosition(recyclerView, null, position - 1);
        else manager.smoothScrollToPosition(recyclerView, null, list.size() - 1);
    }

    @OnClick(R.id.ivNext)
    void clickNext() {
        int position = manager.findFirstVisibleItemPosition();
        if (position < list.size() - 1)
            manager.smoothScrollToPosition(recyclerView, null, position + 1);
        else manager.smoothScrollToPosition(recyclerView, null, 0);
    }
}
