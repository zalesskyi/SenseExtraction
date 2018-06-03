package com.zalesskyi.android.diploma.app.di;

import android.app.Application;

import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.realm.RealmServiceImpl;
import com.zalesskyi.android.diploma.utils.NetworkCheck;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    public NetworkCheck provideNetworkCheck() {
        return new NetworkCheck(mApplication);
    }

    @Provides
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    public RealmService provideRealmService(Realm realm) {
        return new RealmServiceImpl(realm);
    }
}
