
package com.qdn.segmentation.tasks;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import com.qdn.segmentation.Utils.Logger;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qualcomm.qti.snpe.SNPE;
import com.qdn.segmentation.Activity.MainActivity;
import com.qdn.segmentation.Interfaces.INetworkLoader;
import com.qdn.segmentation.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
public class LoadNetworkTask extends AsyncTask<File, Void, NeuralNetwork> {

    private static final String TAG = LoadNetworkTask.class.getSimpleName();
    private final Application mApplication;
    private final NeuralNetwork.Runtime mTargetRuntime;
    private final MainActivity.SupportedTensorFormat mTensorFormat;
    private INetworkLoader mCallbackINetworkLoader;
    private InputStream mInputstream;

    public LoadNetworkTask(final Application application,
                           final NeuralNetwork.Runtime targetRuntime,
                           final MainActivity.SupportedTensorFormat tensorFormat,
                           final INetworkLoader iNetworkLoader) {
        mApplication = application;
        mTargetRuntime = targetRuntime;
        mTensorFormat = tensorFormat;
        this.mCallbackINetworkLoader = iNetworkLoader;
        mInputstream = getFileByResourceId(R.raw.model);

    }

    @Override
    protected NeuralNetwork doInBackground(File... params) {
        NeuralNetwork network = null;
        try {

            final SNPE.NeuralNetworkBuilder builder = new SNPE.NeuralNetworkBuilder(mApplication)
                    .setDebugEnabled(false)
                    .setRuntimeOrder(mTargetRuntime)
                    .setModel(mInputstream, mInputstream.available())
                    .setCpuFallbackEnabled(true)
                    .setUseUserSuppliedBuffers(mTensorFormat != MainActivity.SupportedTensorFormat.FLOAT);
            network = builder.build();
        } catch (IllegalStateException | IOException e) {
            Logger.e(TAG, e.getMessage(), e);
        }
        return network;
    }

    @Override
    protected void onPostExecute(NeuralNetwork neuralNetwork) {
        super.onPostExecute(neuralNetwork);

        if (neuralNetwork != null) {
            mCallbackINetworkLoader.onNetworkBuilt(neuralNetwork);
        } else
            Toast.makeText(mApplication, "Failed Building network !!!", Toast.LENGTH_SHORT).show();

    }


    private InputStream getFileByResourceId(int id) {
        InputStream ins = mApplication.getResources().openRawResource(id);
        return ins;
    }
}
