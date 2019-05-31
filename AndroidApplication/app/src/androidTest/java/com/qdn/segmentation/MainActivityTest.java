package com.qdn.segmentation;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.qdn.segmentation.Helpers.SNPEHelper;
import com.qdn.segmentation.Interfaces.INetworkLoader;
import com.qdn.segmentation.Utils.Logger;
import com.qualcomm.qti.snpe.NeuralNetwork;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest  implements INetworkLoader {

    private static final String TAG = MainActivityTest.class.getSimpleName();
    private Context mContext;
    private SNPEHelper mSNPEHelper;
    private INetworkLoader mCallbackINetworkLoader;
    private boolean isNetworkBuilt;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        mCallbackINetworkLoader = this;
        mSNPEHelper = new SNPEHelper((Application)mContext.getApplicationContext());
    }

    @Test
    public void Android_UT_test_buildNetwork_positive() throws InterruptedException {
        mSNPEHelper.loadNetwork((Application)mContext.getApplicationContext(), mCallbackINetworkLoader);
        Thread.sleep(10000);
        Logger.d(TAG,"isNetworkBuilt "+isNetworkBuilt);
        assertTrue(isNetworkBuilt);
    }

    @Test
    public void Android_UT_test_buildNetwork_negative() {
        mSNPEHelper.loadNetwork((Application)mContext.getApplicationContext(), mCallbackINetworkLoader);
        Logger.d(TAG,"isNetworkBuilt "+isNetworkBuilt);
        assertFalse(isNetworkBuilt);
    }
    @Override
    public void onNetworkBuilt(NeuralNetwork neuralNetwork) {
        if(neuralNetwork != null)
        isNetworkBuilt = true;
        else isNetworkBuilt = false;
    }
}
