package com.belous.v.clrc.other.di;

import android.app.Application;


public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();

    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
