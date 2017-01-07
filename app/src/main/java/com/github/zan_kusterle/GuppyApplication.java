package com.github.zan_kusterle;

import android.app.Application;

import com.github.zan_kusterle.component.ApplicationComponent;
import com.github.zan_kusterle.component.DaggerApplicationComponent;

/**
 * Created by bencz on 2016. 12. 18..
 */

public class GuppyApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.create();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
