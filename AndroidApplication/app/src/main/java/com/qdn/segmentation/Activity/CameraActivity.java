package com.qdn.segmentation.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qdn.segmentation.Fragments.CameraPreviewFragment;
import com.qdn.segmentation.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {

            //Go to camera Preview Fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraPreviewFragment.newInstance())
                    .commit();
        }
    }

}


