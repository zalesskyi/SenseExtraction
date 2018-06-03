package com.zalesskyi.android.diploma.app;

import android.app.Application;
import android.content.Context;

import com.zalesskyi.android.diploma.app.di.ApiModule;
import com.zalesskyi.android.diploma.app.di.AppComponent;
import com.zalesskyi.android.diploma.app.di.AppModule;
import com.zalesskyi.android.diploma.app.di.DaggerAppComponent;
import com.zalesskyi.android.diploma.app.di.PresenterModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private AppComponent mAppComponent;

    public static App get(Context ctx) {
        return (App) ctx.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
        initRealmConfiguration();
    }

    private void initAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .presenterModule(new PresenterModule())
                .build();
    }

    private void initRealmConfiguration() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("sense.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
