package com.freemansys.movelight;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Vibrator;
import android.util.Log;

public class FlashManager {

    private CameraManager mCameraManager;
    private CameraCharacteristics mCharacteristics;
    private Context mContext;
    private static String cameraId;

    // vibration
    private Vibrator mVibrator;
    private long[] patternON;
    private long[] patternOFF;

    public FlashManager(Context context){
        mContext = context;
        initCamera();
        initVibration();
    }

    private void initCamera() {
        Log.d("Movelight", "initializing camera2"); //camera2 was introduced in Android 6 Marshmallow
        try{
            boolean hasFlash = mContext.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            Log.d("Movelight", "device has flash: " + hasFlash);
            if (hasFlash) {
                mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
                String[] cameras = mCameraManager.getCameraIdList();
                if(cameras != null && cameras.length > 0) {
                    for(String id : cameras) {
                        mCharacteristics = mCameraManager.getCameraCharacteristics(id);
                        if(mCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                            cameraId = id;
                            Log.d("Movelight", "getting flash info");
                            return;
                        } else {
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVibration(){
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        patternON = new long[]{0, 10};
        patternOFF = new long[]{0, 10, 150, 10};
    }

    public void setFlashON() {
        try {
            mCameraManager.setTorchMode(cameraId, true);
            mVibrator.vibrate(patternON, -1);
            Log.d("Movelight", "enabling flashlight");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void setFlashOff() {
        try {
            mCameraManager.setTorchMode(cameraId, false);
            mVibrator.vibrate(patternOFF, -1);
            Log.d("Movelight", "disabling flashlight");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
