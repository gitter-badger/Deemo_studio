package com.sevenre.trackre.vehicle;


import com.sevenre.trackre.vehicle.utils.FontsOverride;

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/roboto.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/roboto.ttf");
    }
}