package com.qdn.segmentation.Helpers;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.qdn.segmentation.Interfaces.IBitmapLoader;
import com.qdn.segmentation.tasks.SegmentImageTask;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qdn.segmentation.Activity.MainActivity;
import com.qdn.segmentation.Interfaces.INetworkLoader;
import com.qdn.segmentation.tasks.LoadNetworkTask;

import java.lang.*;

public class SNPEHelper {
    private static final String TAG = SNPEHelper.class.getSimpleName();
    public NeuralNetwork mNeuralnetwork;
    BitmapToFloatArrayHelper mBitmapToFloatHelper;

    public SNPEHelper(Application application) {
        mBitmapToFloatHelper = new BitmapToFloatArrayHelper();
    }


    public static void loadNetwork(Application mApplication, INetworkLoader mCallbackINetworkLoader) {
        LoadNetworkTask mLoadTask = new LoadNetworkTask(mApplication, NeuralNetwork.Runtime.GPU_FLOAT16, MainActivity.SupportedTensorFormat.FLOAT, mCallbackINetworkLoader);
        mLoadTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.mNeuralnetwork = neuralNetwork;
    }

    public NeuralNetwork getNeuralNetwork() {
        return mNeuralnetwork;
    }
    public static void loadSegmentImageTask(Context context, NeuralNetwork neuralNetwork, Bitmap bitmap, IBitmapLoader iBitmapLoader) {
        SegmentImageTask task = new SegmentImageTask(context, neuralNetwork, bitmap, iBitmapLoader);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
