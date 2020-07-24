package com.freemansys.movelight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class GestureManager implements SensorEventListener {

    private static final int DELAY_BETWEEN_CHOPS_MS = 250;
    private static final int CHOP_RESET_TIME_MS = 1000;
    private static final float THRESHOLD_TRIGGER_EVENT = 25f;
    private static final int CHOP_LIMIT = 2;

    private SensorManager msSensorManager;
    private Sensor mSensor;

    private long lastTimeChopDetected;
    private int chopCount = 0;

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
        long timeNow = 0;

        if (Math.abs(x) > THRESHOLD_TRIGGER_EVENT) {
            timeNow = System.currentTimeMillis();
            if (lastTimeChopDetected + DELAY_BETWEEN_CHOPS_MS > timeNow) {
                return;
            }

            if (lastTimeChopDetected != 0 && (timeNow - lastTimeChopDetected) > CHOP_RESET_TIME_MS) {
                Log.d("Movelight", "chop timeout");
                lastTimeChopDetected = 0;
                chopCount = 0;
                return;
            }

            lastTimeChopDetected = timeNow;
            chopCount++;
            Log.d("Movelight", "chopCount: " + chopCount);
        }

        if (chopCount >= CHOP_LIMIT) {
            chopListener.onChop();
            chopCount = 0;
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
