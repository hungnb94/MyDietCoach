package com.hb.mydietcoach.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hb.mydietcoach.custom_view.DrawingView;
import com.hb.mydietcoach.R;

public class DraffActivity extends AppCompatActivity {
    DrawingView drawingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.some_draff);

        drawingView = findViewById(R.id.drawingView);
        drawingView.setDrawingListener(listener);
    }

    DrawingView.OnDrawingListener listener = new DrawingView.OnDrawingListener() {
        @Override
        public void onDrawing() {
            Toast.makeText(DraffActivity.this, "Drawing on drawing view", Toast.LENGTH_SHORT).show();
        }
    };
}
