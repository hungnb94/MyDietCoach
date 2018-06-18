package com.hb.mydietcoach.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.BaseAnimationInterface;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.hb.mydietcoach.R;

import java.util.HashMap;

public class DraffActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    SliderLayout mDemoSlider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.some_draff);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.slim_man_avatar_default);
        file_maps.put("Big Bang Theory", R.drawable.fat_man_avatar_default);
        file_maps.put("House of Cards", R.drawable.slim_man_avatar_default);
        file_maps.put("Game of Thrones", R.drawable.fat_man_avatar_default);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        ListView l = (ListView) findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(DraffActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.draff, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class TransformerAdapter extends BaseAdapter {
        private Context mContext;

        public TransformerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return SliderLayout.Transformer.values().length;
        }

        @Override
        public Object getItem(int position) {
            return SliderLayout.Transformer.values()[position].toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView t = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_draff, null);
            t.setText(getItem(position).toString());
            return t;
        }
    }

    public class ChildAnimationExample implements BaseAnimationInterface {

        private final static String TAG = "ChildAnimationExample";

        @Override
        public void onPrepareCurrentItemLeaveScreen(View current) {
            View descriptionLayout = current.findViewById(com.daimajia.slider.library.R.id.description_layout);
            if (descriptionLayout != null) {
                current.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
            }
            Log.e(TAG, "onPrepareCurrentItemLeaveScreen called");
        }

        @Override
        public void onPrepareNextItemShowInScreen(View next) {
            View descriptionLayout = next.findViewById(com.daimajia.slider.library.R.id.description_layout);
            if (descriptionLayout != null) {
                next.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
            }
            Log.e(TAG, "onPrepareNextItemShowInScreen called");
        }

        @Override
        public void onCurrentItemDisappear(View view) {
            Log.e(TAG, "onCurrentItemDisappear called");
        }

        @Override
        public void onNextItemAppear(View view) {
            View descriptionLayout = view.findViewById(com.daimajia.slider.library.R.id.description_layout);
            if (descriptionLayout != null) {
                view.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.VISIBLE);
                ValueAnimator animator = ObjectAnimator.ofFloat(
                        descriptionLayout, "y", -descriptionLayout.getHeight(),
                        0).setDuration(500);
                animator.start();
            }
            Log.e(TAG, "onCurrentItemDisappear called");
        }
    }
}
