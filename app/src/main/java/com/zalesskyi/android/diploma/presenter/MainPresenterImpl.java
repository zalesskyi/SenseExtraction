package com.zalesskyi.android.diploma.presenter;

import android.app.Application;

import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.realm.Abstract;
import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.utils.NetworkCheck;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

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
    public void doGetListFromRealm(MainListener.ListCallback callback) {
        mRealmService.getObjects(Abstract.class)
                .doOnRequest(l -> mView.showProgress())
                .subscribe(abstracts -> {
                    callback.showList(abstracts);
                }, err -> {
                    mView.showError(err.getMessage());
                    mView.hideProgress();
                }, () -> {
                    mView.hideProgress();
                });
    }

    @Override
    public void doRemoveItemFromList(Abstract item) {
        mRealmService.deleteObject(item.getId(), Abstract.class);
    }
}
