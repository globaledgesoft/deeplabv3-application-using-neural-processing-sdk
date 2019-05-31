package com.qdn.segmentation.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.qdn.segmentation.Utils.Logger;
import com.qualcomm.qti.snpe.FloatTensor;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qdn.segmentation.Helpers.BitmapToFloatArrayHelper;
import com.qdn.segmentation.Utils.Constants;
import com.qdn.segmentation.Interfaces.IBitmapLoader;

import java.util.HashMap;
import java.util.Map;

import static com.qdn.segmentation.Utils.Constants.MNETSSD_INPUT_LAYER;
import static com.qdn.segmentation.Utils.Constants.MNETSSD_OUTPUT_LAYER;

public class SegmentImageTask extends AsyncTask<Void, Void, Bitmap> {

    private static String TAG = SegmentImageTask.class.getSimpleName();
    private static int MNETSSD_NUM_BOXES = Constants.BITMAP_WIDTH * Constants.BITMAP_WIDTH;
    private final float[] floatOutput = new float[MNETSSD_NUM_BOXES];
    private Map<String, FloatTensor> mOutputs;
    private BitmapToFloatArrayHelper mBitmapToFloatHelper;


    public int originalBitmapW, originalBitmapH;
    private int[] mInputTensorShapeHWC;
    private FloatTensor mInputTensorReused;
    private Map<String, FloatTensor> mInputTensorsMap;
    private NeuralNetwork mNeuralnetwork;
    private Bitmap mScaledBitmap, mOutputBitmap;
    private IBitmapLoader mCallbackBitmapLoader;

    public int getInputTensorWidth() {
        return mInputTensorShapeHWC == null ? 0 : mInputTensorShapeHWC[1];
    }

    public int getInputTensorHeight() {
        return mInputTensorShapeHWC == null ? 0 : mInputTensorShapeHWC[2];
    }


    public SegmentImageTask(final Context context,
                            final NeuralNetwork neuralNetwork,
                            final Bitmap bitmap,
                            final IBitmapLoader iBitmapLoader) {
        this.mNeuralnetwork = neuralNetwork;
        this.mCallbackBitmapLoader = iBitmapLoader;
        mBitmapToFloatHelper = new BitmapToFloatArrayHelper();
        originalBitmapH = bitmap.getHeight();
        originalBitmapW = bitmap.getWidth();
        mScaledBitmap = Bitmap.createScaledBitmap(bitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return deeplabV3Inference();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Logger.d(TAG, "onPostExecute " + bitmap);
        mCallbackBitmapLoader.loadResultBitmap(bitmap);

    }

    public Bitmap deeplabV3Inference() {
        try {

            mInputTensorShapeHWC = mNeuralnetwork.getInputTensorsShapes().get(MNETSSD_INPUT_LAYER);
            // allocate the single input tensor
            mInputTensorReused = mNeuralnetwork.createFloatTensor(mInputTensorShapeHWC);
            // add it to the map of inputs, even if it's a single input
            mInputTensorsMap = new HashMap<>();
            mInputTensorsMap.put(MNETSSD_INPUT_LAYER, mInputTensorReused);
            // execute the inference, and get 3 tensors as outputs
            mOutputs = inferenceOnBitmap(mScaledBitmap);
            if (mOutputs != null) {
                MNETSSD_NUM_BOXES = mOutputs.get(MNETSSD_OUTPUT_LAYER).getSize();
                // convert tensors to boxes - Note: Optimized to read-all upfront
                mOutputs.get(MNETSSD_OUTPUT_LAYER).read(floatOutput, 0, MNETSSD_NUM_BOXES);
                //for black/white image
                int w = mScaledBitmap.getWidth();
                int h = mScaledBitmap.getHeight();
                int b = 0xFF;
                int out = 0xFF;

                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        b = b & mScaledBitmap.getPixel(x, y);
                        for (int i = 1; i <= 3 && floatOutput[y * w + x] != 15; i++) {
                            out = out << (8) | b;
                        }
                        mScaledBitmap.setPixel(x, y, floatOutput[y * w + x] != 15 ? out : mScaledBitmap.getPixel(x, y));
                        out = 0xFF;
                        b = 0xFF;
                    }
                }

                mOutputBitmap = Bitmap.createScaledBitmap(mScaledBitmap, originalBitmapW,
                        originalBitmapH, true);
                Logger.d(TAG, mOutputBitmap.getWidth() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mOutputBitmap;
    }



    /* Generic functions, for typical image models */

    private Map<String, FloatTensor> inferenceOnBitmap(Bitmap scaledBitmap) {
        final Map<String, FloatTensor> outputs;

        try {
            // safety check
            if (mNeuralnetwork == null || mInputTensorReused == null || scaledBitmap.getWidth() != getInputTensorWidth() || scaledBitmap.getHeight() != getInputTensorHeight()) {
                Logger.d("SNPEHelper", "No NN loaded, or image size different than tensor size");
                return null;
            }

            // [0.3ms] Bitmap to RGBA byte array (size: 300*300*3 (RGBA..))
            mBitmapToFloatHelper.bitmapToBuffer(scaledBitmap);

            // [2ms] Pre-processing: Bitmap (300,300,4 ints) -> Float Input Tensor (300,300,3 floats)

            final float[] inputFloatsHW3 = mBitmapToFloatHelper.bufferToNormalFloatsBGR();
            if (mBitmapToFloatHelper.isFloatBufferBlack())
                return null;
            mInputTensorReused.write(inputFloatsHW3, 0, inputFloatsHW3.length, 0, 0);
            // [31ms on GPU16, 50ms on GPU] execute the inference

            outputs = mNeuralnetwork.execute(mInputTensorsMap);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("SNPEHelper", e.getCause() + "");
            return null;
        }

        return outputs;
    }
}
