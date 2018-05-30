package com.zalesskyi.android.diploma.view.main_operation.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.app.App;
import com.zalesskyi.android.diploma.presenter.PresenterContract;
import com.zalesskyi.android.diploma.view.BaseView;
import com.zalesskyi.android.diploma.view.main_operation.fragments.ListFragment;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements BaseView.MainView {

    private FragmentManager mFragmentManager;

    private MainListener mListener = new MainListener() {
        @Override
        public void getAbstract(String text) {
            mPresenter.doGetAbstractFromString(text);
        }
    };

    @Inject
    PresenterContract.MainPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.get(this).getAppComponent().inject(this);

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.main_container);

        if (fragment == null) {
            fragment = new ListFragment(mListener);
            mFragmentManager.beginTransaction()
                    .add(R.id.main_container, fragment)
                    .commit();
        }
    }
}
