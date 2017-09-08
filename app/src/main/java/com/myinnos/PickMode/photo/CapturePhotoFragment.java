package com.myinnos.PickMode.photo;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.myinnos.PickMode.R;
import com.myinnos.PickMode.photo.cameraview.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myinnos on 07/09/17.
 */

public class CapturePhotoFragment extends Fragment {

    private static final String TAG = "CapturePhotoFragment";

    private static final String DIR_YUMMYPETS = "/myinnos";

    @BindView(R.id.mCameraPhotoView)
    CameraView mCameraPhotoView;
    @BindView(R.id.mBtnTakePhoto)
    ImageView mBtnTakePhoto;
    @BindView(R.id.mShutter)
    View mShutter;

    private Handler mBackgroundHandler;

    @OnClick(R.id.mBtnTakePhoto)
    void onTakePhotoClick() {
        mCameraPhotoView.takePicture();
    }

    public static CapturePhotoFragment newInstance() {
        return new CapturePhotoFragment();
    }

    private void initViews() {
        if (mCameraPhotoView != null) {
            mCameraPhotoView.addCallback(mCallback);
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File dirDest =
                            new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                    DIR_YUMMYPETS);
                    File file;
                    String fileName = "myinnos_" +
                            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + ".jpg";
                    if (dirDest.exists()) {
                        Log.d(TAG, "exists " + dirDest.getAbsolutePath());
                        file = new File(dirDest, fileName);
                    } else {
                        if (dirDest.mkdir()) {
                            file = new File(dirDest, fileName);
                        } else {
                            file = null;
                        }
                    }
                    OutputStream os = null;
                    if (file != null) {
                        try {
                            os = new FileOutputStream(file);
                            os.write(data);
                            os.close();
                        } catch (IOException e) {
                            Log.w(TAG, "Cannot write to " + file, e);
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    // Ignore
                                }
                            }
                        }
                    }
                }
            });
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraPhotoView.start();
    }

    @Override
    public void onPause() {
        mCameraPhotoView.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_photo_view, container, false);
        ButterKnife.bind(this, v);
        initViews();
        return v;
    }

}
