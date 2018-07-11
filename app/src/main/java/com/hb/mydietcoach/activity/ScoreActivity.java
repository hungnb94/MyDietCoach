package com.hb.mydietcoach.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.dialog.TargetArchiveDialog;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

public class ScoreActivity extends BaseActivity {

    private int nPoints, nLevel, nTarget, nTotalPoints;
    private String sLastPointFor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPoint();
    }

    private void initPoint() {
        PreferenceManager pre = new PreferenceManager(this);

        sLastPointFor = pre.getString(PreferenceManager.LAST_POINTS_FOR, getString(R.string.starting_with));

        nPoints = pre.getInt(PreferenceManager.POINTS, 40);
        nLevel = pre.getInt(PreferenceManager.LEVEL, 1);
        nTotalPoints = pre.getInt(PreferenceManager.TOTAL_POINTS, nPoints);
        nTarget = 100 + (nLevel - 1) * Constants.TARGET_DISTANT;
    }

    public void addPoints(int points) {
        nPoints += points;
        nTotalPoints += points;
    }

    public void checkLevel() {
        if (nPoints >= nTarget) {
            //Show dialog
            Dialog dialog = new TargetArchiveDialog(this, nTarget, nLevel, nTarget + Constants.TARGET_DISTANT);
            dialog.show();

            nPoints = 0;
            ++nLevel;
            nTarget += Constants.TARGET_DISTANT;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager pre = new PreferenceManager(this);
        pre.putString(PreferenceManager.LAST_POINTS_FOR, sLastPointFor);
        pre.putInt(PreferenceManager.POINTS, nPoints);
        pre.putInt(PreferenceManager.LEVEL, nLevel);
        pre.putInt(PreferenceManager.TOTAL_POINTS, nTotalPoints);
    }

    public int getPoints() {
        return nPoints;
    }

    public int getLevel() {
        return nLevel;
    }

    public int getTarget() {
        return nTarget;
    }

    public int getTotalPoints() {
        return nTotalPoints;
    }

    public String getLastPointFor() {
        return sLastPointFor;
    }

    public void setLastPointFor(String lastPointFor) {
        this.sLastPointFor = lastPointFor;
    }
}
