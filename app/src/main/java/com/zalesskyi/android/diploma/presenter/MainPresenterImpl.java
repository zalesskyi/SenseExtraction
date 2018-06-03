package com.zalesskyi.android.diploma.presenter;

import android.app.Application;

import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.utils.NetworkCheck;

public class MainPresenterImpl extends BasePresenter
        implements PresenterContract.MainPresenter {
    private static final String TAG = "MainPresenter";

    public MainPresenterImpl(Application application, NetworkCheck networkCheck
            , Interactor interactor, RealmService realmService) {
        super.mApplication = application;
        super.mNetworkCheck = networkCheck;
        super.mInteractor = interactor;
        super.mRealmService = realmService;
    }

    @Override
    public void shareAbstract(long id) {

    }

    @Override
    public void starAbstract(long id) {

    }

    @Override
    public void openWithAbstract(long id) {

    }

    @Override
    public void removeAbstract(long id) {

    }
}
