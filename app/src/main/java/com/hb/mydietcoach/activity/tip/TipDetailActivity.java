package com.hb.mydietcoach.activity.tip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.adapter.tip.TipDetailAdapter;
import com.hb.mydietcoach.model.TipCategory;
import com.hb.mydietcoach.model.TipDetail;
import com.hb.mydietcoach.utils.MyUtils;

import java.util.Collections;
import java.util.Comparator;
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
    private Realm realm;

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
        realm = Realm.getDefaultInstance();
        TipCategory results = realm.where(TipCategory.class)
                .equalTo(TipCategory.ID, bundle.getInt(TIP_CATEGORY_ID))
                .findFirst();

        list = new RealmList<>();
        list.addAll(Objects.requireNonNull(results).getAdvices());
        sort(list);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tip_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                share();
                break;
            case R.id.action_like:
                like();
                break;
            case R.id.action_dislike:
                dislike();
                break;
//            case R.id.action_add_reminder:
//                setReminder();
//                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        TipDetail tip = getCurrentTip();
        String message = tip.getMessage() + getString(R.string.via_my_diet_coach);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("plain/text");
        startActivity(intent);
    }

    private void like() {
        TipDetail detail = getCurrentTip();
        realm.beginTransaction();
        detail.setPiority(Objects.requireNonNull(detail).getPiority() + 1);
        realm.insertOrUpdate(detail);
        realm.commitTransaction();
        MyUtils.showToast(this, getString(R.string.like_toast_msg));
    }

    private void dislike() {
        MyUtils.showToast(this, getString(R.string.dislike_toast_msg));
        final TipDetail detail = getCurrentTip();
        realm.beginTransaction();
        Objects.requireNonNull(detail).deleteFromRealm();
        realm.commitTransaction();
    }

    private void setReminder() {
        //TODO: set reminder
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

    private TipDetail getCurrentTip() {
        int position = manager.findFirstVisibleItemPosition();
        return list.get(position);
    }

    /**
     * Sort list by piority
     *
     * @param list: list of tip detail
     */
    private void sort(RealmList<TipDetail> list) {
        Collections.sort(list, new Comparator<TipDetail>() {
            @Override
            public int compare(TipDetail tipDetail, TipDetail t1) {
                if (tipDetail.getPiority() > t1.getPiority()) return -1;
                else if (tipDetail.getPiority() == t1.getPiority()) return 0;
                return 1;
            }
        });
    }
}
