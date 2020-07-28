package com.freemansys.movelight;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout screenBackground;
    private ImageButton powerButton;
    private SeekBar seekBar;
    private FlashManager mFlashManager;
    private AlertManager mAlertManager;
    private GestureManager mGestureManager;
    private ColorManager mColorManager;
    private boolean flashStatus = false;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlashManager = new FlashManager(this);
        mAlertManager = new AlertManager(this);
        mGestureManager = new GestureManager(this);
        mColorManager = new ColorManager(this, getColor(R.color.colorAccent));

        screenBackground = (RelativeLayout) findViewById(R.id.screen_background);

        powerButton = (ImageButton) findViewById(R.id.power_button);
        powerButton.setOnClickListener(clickListener);

        seekBar = (SeekBar) findViewById(R.id.lightness_seekBar);
        seekBar.setOnSeekBarChangeListener(seekbarListener);

        AlertManager.NotificationReceiver receiver = new AlertManager.NotificationReceiver();
        receiver.setOnNotificationClosed(notificationListener);

        mGestureManager.setOnChopDetected(chopListener);

        mColorManager.setOnColorChanged(colorListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.power_button:
                    if(flashStatus == false) {
                        mFlashManager.setFlashON();
                        mAlertManager.showNotification();
                    } else {
                        mFlashManager.setFlashOff();
                        mAlertManager.closeNotification();
                    }
                    flashStatus = !flashStatus;
                    changePowerButtonColor(flashStatus);
                    break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekbarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mColorManager.changeScreenColor(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private AlertManager.NotificationReceiver.NotificationListener notificationListener
            = new AlertManager.NotificationReceiver.NotificationListener() {
        @Override
        public void onClosed() {
            mFlashManager.setFlashOff();
            flashStatus = false;
            changePowerButtonColor(flashStatus);
        }
    };

    private GestureManager.ChopListener chopListener = new GestureManager.ChopListener() {
        @Override
        public void onChop() {
            if (flashStatus == false) {
                mFlashManager.setFlashON();
                mAlertManager.showNotification();
                flashStatus = true;
            } else {
                mFlashManager.setFlashOff();
                mAlertManager.closeNotification();
                flashStatus = false;
            }
            changePowerButtonColor(flashStatus);
        }
    };

    private ColorManager.ColorListener colorListener = new ColorManager.ColorListener() {
        @Override
        public void onChange(int color) {
            screenBackground.setBackgroundColor(color);
        }
    };

    private void changePowerButtonColor(boolean isEnabled) {
        int newColor = (isEnabled) ? Color.GREEN : Color.WHITE;
        powerButton.setColorFilter(newColor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGestureManager.closeSensor();
    }
}