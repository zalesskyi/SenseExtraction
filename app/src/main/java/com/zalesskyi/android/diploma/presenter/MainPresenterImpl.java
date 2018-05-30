package com.zalesskyi.android.diploma.presenter;

import android.app.Application;

import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.utils.NetworkCheck;

import java.io.File;

public class MainPresenterImpl extends BasePresenter
        implements PresenterContract.MainPresenter {

    public MainPresenterImpl(Application application, NetworkCheck networkCheck, Interactor interactor) {
        super.mApplication = application;
        super.mNetworkCheck = networkCheck;
        super.mInteractor = interactor;
    }

    @Override
    public void doGetAbstractFromFile(File sourceText) {

    }

    @Override
    public void doGetAbstractFromString(String sourceText) {
        mInteractor.toDoGetAbstract(sourceText, 0);
    }

    @Override
    public void doGetAbstractFromUrl(String sourceTextUrl) {

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
