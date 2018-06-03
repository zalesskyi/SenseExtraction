package com.zalesskyi.android.diploma.app.di;

import android.app.Application;

import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.presenter.DetailPresenterImpl;
import com.zalesskyi.android.diploma.presenter.MainPresenterImpl;
import com.zalesskyi.android.diploma.presenter.PresenterContract;
import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.utils.NetworkCheck;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    PresenterContract.MainPresenter provideMainPresenter(Application application, NetworkCheck networkCheck,
                                                         Interactor interactor, RealmService realmService) {
        return new MainPresenterImpl(application, networkCheck, interactor, realmService);
    }

    @Provides
    PresenterContract.DetailPresenter provideDetailPresenter(Application application, NetworkCheck networkCheck,
                                                             Interactor interactor, RealmService realmService) {
        return new DetailPresenterImpl(application, networkCheck, interactor, realmService);
    }
}
