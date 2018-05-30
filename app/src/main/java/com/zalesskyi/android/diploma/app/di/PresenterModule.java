package com.zalesskyi.android.diploma.app.di;

import android.app.Application;

import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.presenter.MainPresenterImpl;
import com.zalesskyi.android.diploma.presenter.PresenterContract;
import com.zalesskyi.android.diploma.utils.NetworkCheck;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    PresenterContract.MainPresenter provideMainPresenter(Application application, NetworkCheck networkCheck,
                                                         Interactor interactor) {
        return new MainPresenterImpl(application, networkCheck, interactor);
    }
}
