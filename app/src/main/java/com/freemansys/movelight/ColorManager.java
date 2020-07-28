package com.freemansys.movelight;

import android.content.Context;

import androidx.core.graphics.ColorUtils;

public class ColorManager {

    private Context mContext;
    private static int defaultColor;
    private static float[] hsl = new float[3]; // hue, saturation, lightness

    public interface ColorListener {
        void onChange(int color);
    }

    private static ColorListener colorListener;

    public ColorManager(Context context, int hex) {
        mContext = context;
        defaultColor = hex;
        ColorUtils.colorToHSL(hex, hsl);
    }

    public void changeScreenColor(int value) {
        int newColor = 0;

        if (value <= 15) {
            newColor = mContext.getColor(R.color.colorPrimary);
        } else {
            float lightness = (float) value / 100;
            hsl[2] = lightness;
            newColor = ColorUtils.HSLToColor(hsl);
        }

        colorListener.onChange(newColor);
    }

    public void setOnColorChanged(ColorListener listener){
        colorListener = listener;
    }
}
