package com.freemansys.movelight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GestureManager implements SensorEventListener {

    private static final int DELAY_BETWEEN_CHOPS_MS = 750;
    private static final float THRESHOLD_TRIGGER_EVENT = 30f;

    private SensorManager msSensorManager;
    private Sensor mSensor;

    private long lastTimeChopDetected;
    private boolean isGestureInProgress = false;

    public interface ChopListener {
        void onChop();
    }

    private static ChopListener chopListener;

    public GestureManager(Context context) {
        initSensor(context);
    }

    private void initSensor(Context context){
        msSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = msSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        long timeDelta = 0;

        if (Math.abs(x) > THRESHOLD_TRIGGER_EVENT) {
            lastTimeChopDetected = System.currentTimeMillis();
            isGestureInProgress = true;
        } else {
            timeDelta = System.currentTimeMillis() - lastTimeChopDetected;
            if (timeDelta > DELAY_BETWEEN_CHOPS_MS && isGestureInProgress) {
                isGestureInProgress = false;
                chopListener.onChop();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void closeSensor() {
        msSensorManager.unregisterListener(this);
    }

    public void setOnChopDetected(ChopListener listener){
        chopListener = listener;
    }
}
