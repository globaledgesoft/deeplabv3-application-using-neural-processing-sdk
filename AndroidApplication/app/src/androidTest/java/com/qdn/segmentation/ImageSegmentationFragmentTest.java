package com.qdn.segmentation;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.qdn.segmentation.Helpers.SNPEHelper;
import com.qdn.segmentation.Interfaces.IBitmapLoader;
import com.qdn.segmentation.Interfaces.INetworkLoader;
import com.qdn.segmentation.Utils.Logger;
import com.qualcomm.qti.snpe.NeuralNetwork;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ImageSegmentationFragmentTest implements INetworkLoader, IBitmapLoader {

    private static final String TAG = ImageSegmentationFragmentTest.class.getSimpleName();
    private Context mContext;
    private SNPEHelper mSNPEHelper;
    private INetworkLoader mCallbackINetworkLoader;
    private IBitmapLoader mCallbackBitmapLoader;
    private boolean isNetworkBuilt;
    private boolean isBitmapLoaded;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        mCallbackINetworkLoader = this;
        mCallbackBitmapLoader = this;
        mSNPEHelper = new SNPEHelper((Application) mContext.getApplicationContext());

    }

    public void buildNetwork() {
        mSNPEHelper.loadNetwork((Application) mContext.getApplicationContext(), mCallbackINetworkLoader);
    }

    @Test
    public void Android_UT_test_imageSegmentation() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.person3);
            mSNPEHelper.loadSegmentImageTask(mContext,mSNPEHelper.getNeuralNetwork(),mBitmap,mCallbackBitmapLoader);
            Thread.sleep(20000);
            assertTrue(isBitmapLoaded);
        }
    }

    @Override
    public void onNetworkBuilt(NeuralNetwork neuralNetwork) {
        Logger.d(TAG, "onNetworkBuilt");
        if (neuralNetwork != null) {
            mSNPEHelper.setNeuralNetwork(neuralNetwork);
            isNetworkBuilt = true;
        }
        else isNetworkBuilt = false;
    }

    @Override
    public void loadResultBitmap(Bitmap bitmap) {
        Logger.d(TAG, "loadResultBitmap");
        if(bitmap != null)
            isBitmapLoaded = true;
        else isBitmapLoaded = false;
    }
}
