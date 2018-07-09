package com.hb.mydietcoach.activity.contact_faq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.fragment.FAQ1_Fragment;
import com.hb.mydietcoach.fragment.FAQ2_Fragment;
import com.hb.mydietcoach.fragment.FAQ3_Fragment;

import java.util.Objects;

public class FrequentAskQuestionActivity extends BaseActivity {

    public static final String FREQUENT_ASK_QUESTION = "FREQUENT_ASK_QUESTION";
    public static final int FQ_HOW_SUBCRIPTION_BE_CHARGED = 1;
    public static final int FQ_HOW_CANCEL_SUBCRIPTION_RENEWAL = 2;
    public static final int FQ_HOW_DO_DELETE_OR_EDIT_ITEM_FROM_DIARY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequent_ask_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.contact_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int type = getIntent().getIntExtra(FREQUENT_ASK_QUESTION, 1);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fr = getFragmentFromType(type);
        ft.replace(R.id.llContent, fr);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private Fragment getFragmentFromType(int fragmentType) {
        switch (fragmentType) {
            case FQ_HOW_SUBCRIPTION_BE_CHARGED:
                return new FAQ1_Fragment();
            case FQ_HOW_CANCEL_SUBCRIPTION_RENEWAL:
                return new FAQ2_Fragment();
            case FQ_HOW_DO_DELETE_OR_EDIT_ITEM_FROM_DIARY:
                return new FAQ3_Fragment();
            default:
                return new FAQ1_Fragment();
        }
    }
}
