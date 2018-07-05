package com.hb.mydietcoach.activity.tip;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.adapter.tip.SpinnerTipCategoryAdapter;
import com.hb.mydietcoach.model.tip.TipCategory;
import com.hb.mydietcoach.model.tip.TipDetail;
import com.hb.mydietcoach.utils.MyUtils;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddingTipActivity extends BaseActivity {
    private RealmResults<TipCategory> categories;

    private Spinner spinner;
    private EditText editText;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_tip);

        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.add_tip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        initParams();
        initView();
    }

    private void initParams() {
        realm = Realm.getDefaultInstance();
        categories = realm.where(TipCategory.class).findAll();
    }

    private void initView() {
        spinner = findViewById(R.id.spinner);
        SpinnerTipCategoryAdapter adapter = new SpinnerTipCategoryAdapter(this, categories);
        spinner.setAdapter(adapter);
        editText = findViewById(R.id.editText);
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

    @OnClick(R.id.btnSave)
    void clickSave() {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            MyUtils.showToast(AddingTipActivity.this, getString(R.string.alert_empty_tip));
            return;
        }

        //Add new tip to realm
        TipCategory tc = categories.get(spinner.getSelectedItemPosition());
        realm.beginTransaction();
        TipDetail td = new TipDetail(text, 1);
        Objects.requireNonNull(tc).getAdvices().add(td);
        realm.insert(td);
        realm.insertOrUpdate(tc);
        realm.commitTransaction();

        finish();
    }
}
