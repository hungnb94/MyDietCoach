package com.hb.mydietcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.dialog.MyAlertDialog;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermConditionActivity extends AppCompatActivity {

    private static final String TAG = TermConditionActivity.class.getSimpleName();

    private EditText edtWeight;
    private Spinner spinnerWeightType, spinnerGender;
    private CheckBox checkBox;

    private String weightTypes[];
    private String genderTypes[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);

        weightTypes = getResources().getStringArray(R.array.weight_types);
        genderTypes = getResources().getStringArray(R.array.gender_types);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        ButterKnife.bind(this);
        edtWeight = findViewById(R.id.edtWeight);
        spinnerWeightType = findViewById(R.id.spinnerWeightType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, weightTypes);
        spinnerWeightType.setAdapter(adapter);

        spinnerGender = findViewById(R.id.spinnerGender);
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, genderTypes);
        spinnerGender.setAdapter(adapter);

        checkBox = findViewById(R.id.checkbox);
    }

    //Click cancel
    @OnClick(R.id.btnCancel)
    void clickCancel(View view) {
        finish();
    }

    //Click agree with terms and conditions
    @OnClick(R.id.btnAgree)
    void clickAgree(View view) {
        if (allParametersAvailable()) {
            int position = spinnerGender.getSelectedItemPosition();
            boolean isFemale = (position == 0);
            String strWeight = edtWeight.getText().toString();
            float weight = Float.valueOf(strWeight);
            if (spinnerWeightType.getSelectedItemPosition()==1){
                weight = changeLbToKg(weight);
            }

            PreferenceManager pre = new PreferenceManager(this);
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_LAUNCH, false);
            pre.putFloat(PreferenceManager.USER_WEIGHT, weight);
            pre.putBoolean(PreferenceManager.IS_GENDER_FEMALE, isFemale);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean allParametersAvailable() {
        try {
            String strWeight = edtWeight.getText().toString();
            if (TextUtils.isEmpty(strWeight)) {
                MyAlertDialog dialog = new MyAlertDialog(this, getString(R.string.enter_your_weight));
                dialog.show();
                return false;
            }
            float weight = Float.valueOf(strWeight);
            if (weight <= 0) {
                MyAlertDialog dialog = new MyAlertDialog(this, getString(R.string.bigger_than_zero));
                dialog.show();
                return false;
            }
            if (!checkBox.isChecked()) {
                MyAlertDialog dialog = new MyAlertDialog(this, getString(R.string.must_accept_terms_conditions));
                dialog.show();
                return false;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private float changeLbToKg(float lbWeight){
        return lbWeight * Constants.LB_TO_KG;
    }
}
