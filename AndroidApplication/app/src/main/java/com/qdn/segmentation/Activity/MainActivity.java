package com.qdn.segmentation.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qdn.segmentation.Utils.Logger;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qdn.segmentation.Interfaces.INetworkLoader;
import com.qdn.segmentation.R;
import com.qdn.segmentation.Helpers.SNPEHelper;

public class MainActivity extends AppCompatActivity implements INetworkLoader {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static SNPEHelper mSNPEHelper;
    private INetworkLoader mCallbackINetworkLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    private void initViews() {
        mCallbackINetworkLoader = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(TAG, "onstart");
        mSNPEHelper = new SNPEHelper(getApplication());
        mSNPEHelper.loadNetwork(getApplication(), mCallbackINetworkLoader);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop()");
    }


    @Override
    public void onNetworkBuilt(NeuralNetwork neuralNetwork) {
        mSNPEHelper.setNeuralNetwork(neuralNetwork);
        Logger.d(TAG, "Network built successfully");
        Intent intent = new Intent(this, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

    }

    public enum SupportedTensorFormat {
        FLOAT,
        UB_TF8
    }
}