package com.zalesskyi.android.diploma.view;

public interface BaseView {
    void showError(String err);

    void showProgress();
    void hideProgress();

    interface MainView {

    }

    interface DetailView {

    }
}
