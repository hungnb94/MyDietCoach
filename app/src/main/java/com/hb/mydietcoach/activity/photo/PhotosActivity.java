package com.hb.mydietcoach.activity.photo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.ContactFAQActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.SettingsActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.reminder.EdittingReminderActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.adapter.MiniPhotoAdapter;
import com.hb.mydietcoach.model.Reminder;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hb.mydietcoach.activity.reminder.EdittingReminderActivity.HOUR_MINISECOND;
import static com.hb.mydietcoach.utils.Constants.RC_CAMERA_PERMISSION;
import static com.hb.mydietcoach.utils.Constants.RC_EXTERNAL_STORAGE;
import static com.hb.mydietcoach.utils.Constants.RC_PICK_IMAGE;
import static com.hb.mydietcoach.utils.Constants.RC_TAKE_PHOTO;

public class PhotosActivity extends AppCompatActivity implements MiniPhotoAdapter.OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String TAG = PhotosActivity.class.getSimpleName();

    private DrawerLayout drawer;

    //    private FloatingActionButton fabSelectGallery, fabTakeSnapshot;
    private SliderLayout sliderShow;

    private RecyclerView recyclerView;
    MiniPhotoAdapter adapter;
    private List<File> imageList;

    private FrameLayout flAddbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        FileTask fileTask = new FileTask();
        fileTask.execute();

        initView();
        verifyStoragePermissions();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.my_motivational_photo);
        ButterKnife.bind(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_photos);

        //Add button is gone or visible
        flAddbutton = findViewById(R.id.flAddButton);

        sliderShow = findViewById(R.id.slider);
        sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        sliderShow.stopAutoCycle();
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager pre = new PreferenceManager(this);
        boolean hasPhoto = pre.getBoolean(PreferenceManager.IS_HAS_MOTIVATIONAL_PHOTO, false);
        if (hasPhoto) flAddbutton.setVisibility(View.GONE);
    }

    @OnClick(R.id.fabSelectFrGallery)
    void clickSelectFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent pickImageIntent = Intent.createChooser(intent, getString(R.string.select_photo));
        startActivityForResult(pickImageIntent, RC_PICK_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.fabTakeSnapshot)
    void clickTakeSnapshot() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    RC_CAMERA_PERMISSION);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, RC_TAKE_PHOTO);
        }
    }

    @OnClick(R.id.btnAddReminder)
    void clickAddReminder() {
        Intent intent = new Intent(this, EdittingReminderActivity.class);
        Bundle bundle = new Bundle();
        Reminder reminder = new Reminder(0,
                "View my motivational photos",
                Calendar.getInstance(),
                24 * HOUR_MINISECOND);
        bundle.putSerializable(Constants.DATA_SERIALIZABLE, reminder);
        bundle.putBoolean(EdittingReminderActivity.IS_MOTIVATIONAL_REMINDER, true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    RC_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    saveAndUpdateUI(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == RC_TAKE_PHOTO) {
            if (resultCode == RESULT_OK && data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                saveAndUpdateUI(bitmap);
            }
        }
    }

    /**
     * Save bitmap to app directory and update UI
     *
     * @param bitmap: image
     */
    @SuppressLint("StaticFieldLeak")
    void saveAndUpdateUI(final Bitmap bitmap) {

        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {
                File newImg = saveBitmap(bitmap);
                return newImg;
            }

            @Override
            protected void onPostExecute(File newImg) {
                super.onPostExecute(newImg);
                if (newImg != null) {
                    imageList.add(newImg);
                    adapter.notifyDataSetChanged();

                    DefaultSliderView sliderView = new DefaultSliderView(PhotosActivity.this);
                    sliderView.image(newImg);
                    sliderShow.addSlider(sliderView);
                    sliderShow.setCurrentPosition(imageList.size() - 1);
                }
            }
        }.execute();
    }

    File saveBitmap(Bitmap bitmap) {
        File file = null;
        FileOutputStream out = null;
        try {
            File directory = new File(Environment.getExternalStorageDirectory(), Constants.MY_FOLDER);
            if (!directory.exists()) directory.mkdirs();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            file = new File(directory, sdf.format(Calendar.getInstance().getTime()) + ".png");
            file.createNewFile();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            return file;
        } catch (Exception e) {
            Log.e(TAG, "Save image file failed");
            Log.e(TAG, "File name " + file.getAbsolutePath());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.camera_permission_granted, Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RC_TAKE_PHOTO);
            } else {
                Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RC_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (imageList == null || imageList.size() == 0) {
                    FileTask fileTask = new FileTask();
                    fileTask.execute();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_photos) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
            //Blank
        } else if (id == R.id.nav_tips) {
            Intent intent = new Intent(this, TipsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_challenges) {
            Intent intent = new Intent(this, ChallengesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_rewards) {

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
    public void onItemClick(int position) {
        sliderShow.setCurrentPosition(position);
    }

    @SuppressLint("StaticFieldLeak")
    public class FileTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<File> list = getAllImage();
            imageList.clear();
            imageList.addAll(list);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            addImageToPhotoSlider();

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(PhotosActivity.this,
                            LinearLayoutManager.HORIZONTAL,
                            false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            if (imageList == null) imageList = new ArrayList<>();
            adapter = new MiniPhotoAdapter(PhotosActivity.this, imageList);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(PhotosActivity.this);

        }

        /**
         * Get all image from app directory
         *
         * @return all image from app folder
         */
        private List<File> getAllImage() {
            List<File> list = new ArrayList<>();
            File directory = new File(Environment.getExternalStorageDirectory(), Constants.MY_FOLDER);
            if (directory.listFiles() == null) return list;
            for (File file : directory.listFiles()) {
                if (isImageFile(file.getName())) list.add(file);
            }
            return list;
        }

        private final String[] ImageExtensions = new String[]{"jpg", "png", "jpeg"};

        /**
         * Compare file is image or not
         *
         * @param fileName: name of file
         * @return true if is image, false if otherwise
         */
        private boolean isImageFile(String fileName) {
            for (String extention : ImageExtensions) {
                if (fileName.toLowerCase().endsWith(extention)) return true;
            }
            return false;
        }
    }

    private void addImageToPhotoSlider() {
        for (File file : imageList) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView.image(file);
            sliderShow.addSlider(sliderView);
        }
    }
}
