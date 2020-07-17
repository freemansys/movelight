package com.freemansys.movelight;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton powerButton;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean flashStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerButton = (ImageButton) findViewById(R.id.power_button);
        powerButton.setOnClickListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSharedPreferences.edit();

        flashStatus = mSharedPreferences.getBoolean("is_flashlight_enabled", false);

        changePowerButtonColor(flashStatus);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.power_button:
                changeFlashlightStatus();
                break;
        }
    }

    private void changePowerButtonColor(boolean isEnabled) {
        int newColor = (isEnabled) ? Color.GREEN : Color.WHITE;
        powerButton.setColorFilter(newColor);
    }

    private void changeFlashlightStatus(){
        if(flashStatus == true){
            editor.putBoolean("is_flashlight_enabled", false);
        }else{
            editor.putBoolean("is_flashlight_enabled", true);
        }

        editor.commit();
        flashStatus = !flashStatus;
        changePowerButtonColor(flashStatus);
    }
}