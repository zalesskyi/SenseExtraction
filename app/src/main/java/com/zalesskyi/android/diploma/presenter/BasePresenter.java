package com.zalesskyi.android.diploma.presenter;

import android.app.Application;

import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.utils.NetworkCheck;
import com.zalesskyi.android.diploma.view.BaseView;

public abstract class BasePresenter<V extends BaseView> {

    protected Application mApplication;
    protected NetworkCheck mNetworkCheck;
    protected Interactor mInteractor;
    protected V mView;

    public void init(V v) {
        mView = v;
    }

    public void dismiss() {
        mView = null;
    }
}
