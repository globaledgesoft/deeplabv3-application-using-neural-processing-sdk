package com.qdn.segmentation;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.qdn.segmentation.Activity.CameraActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CameraFragmentTest {

    private static final String TAG = CameraFragmentTest.class.getSimpleName();
    private Context mContext;

    @Rule
    public ActivityTestRule<CameraActivity> mCameraActivityRule = new ActivityTestRule<>(CameraActivity.class);

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void Android_UT_test_cameraPreview() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.texture)).check(matches(withId(R.id.texture)));
    }

    @Test
    public void Android_UT_test_photoFilePath() throws InterruptedException {
        Thread.sleep(2000);
        File mFile = new File(mCameraActivityRule.getActivity().getExternalFilesDir(null), "pic.jpg");
        assertTrue(mFile.exists());
    }
}
