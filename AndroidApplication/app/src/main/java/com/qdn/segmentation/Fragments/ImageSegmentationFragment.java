package com.qdn.segmentation.Fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdn.segmentation.Utils.Constants;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qdn.segmentation.Activity.MainActivity;
import com.qdn.segmentation.Helpers.SNPEHelper;
import com.qdn.segmentation.Interfaces.IBitmapLoader;
import com.qdn.segmentation.R;
import com.qdn.segmentation.tasks.SegmentImageTask;

public class ImageSegmentationFragment extends Fragment implements IBitmapLoader {

    private ImageView mImageViewSegmented;
    private SNPEHelper mSNPEHelper;
    private Bitmap mBitmap;
    private IBitmapLoader mCallBackBitmapLoader;
    private ProgressDialog mProgressDialog;
    private AppCompatActivity mActivity;
    private Toolbar mToolbar;
    private TextView mTextViewImageType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_segmentation, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        mSNPEHelper = new SNPEHelper(getActivity().getApplication());
        initViews();
        initToolbar();
        String imgPath = getArguments().getString(Constants.BUNDLE_KEY_IMAGE);
        if (imgPath != null) {
            mBitmap = BitmapFactory.decodeFile(imgPath);
            mImageViewSegmented.setImageBitmap(mBitmap);
            mTextViewImageType.setText(getString(R.string.original_image));
            mProgressDialog = ProgressDialog.show(mActivity, getString(R.string.dialog_image_segmentation),
                    getString(R.string.dialog_segmentation_process), true);
            mProgressDialog.show();
            mSNPEHelper.loadSegmentImageTask(getContext(), mSNPEHelper.getNeuralNetwork(), mBitmap, mCallBackBitmapLoader);
        }
    }

    private void initToolbar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar_home);
        setHasOptionsMenu(true);
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setTitle(R.string.image_segmentation);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        mCallBackBitmapLoader = this;
        mImageViewSegmented = (ImageView) mActivity.findViewById(R.id.image_view_segment);
        mTextViewImageType = (TextView) mActivity.findViewById(R.id.textView_imagetype);
        mSNPEHelper = MainActivity.mSNPEHelper;
    }


    @Override
    public void loadResultBitmap(Bitmap bitmap) {
        mProgressDialog.dismiss();
        mImageViewSegmented.setImageBitmap(bitmap);
        mTextViewImageType.setText(getString(R.string.segmented_image));
    }
}