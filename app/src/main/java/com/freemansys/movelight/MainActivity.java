package com.freemansys.movelight;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton powerButton;
    private FlashManager mFlashManager;
    private AlertManager mAlertManager;
    private boolean flashStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlashManager = new FlashManager(this);
        mAlertManager = new AlertManager(this);

        powerButton = (ImageButton) findViewById(R.id.power_button);
        powerButton.setOnClickListener(this);
    }

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

    private void changePowerButtonColor(boolean isEnabled) {
        int newColor = (isEnabled) ? Color.GREEN : Color.WHITE;
        powerButton.setColorFilter(newColor);
    }

}