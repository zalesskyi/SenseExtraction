package com.zalesskyi.android.diploma.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.view.main_operation.fragments.ListFragment;

import io.reactivex.functions.Action;

public class BaseActivity extends AppCompatActivity {

    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mFragmentManager = getSupportFragmentManager();
    }

    protected void replaceWithAnimFragment(int id, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.move_right_in_activity, R.anim.move_left_out_activity, R.anim.move_left_in_activity, R.anim.move_right_out_activity).replace(id, fragment).addToBackStack(tag);
        fragmentTransaction.commit();
    }

    protected void replaceWithAnimFragment(int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.move_right_in_activity, R.anim.move_left_out_activity, R.anim.move_left_in_activity, R.anim.move_right_out_activity).replace(id, fragment);
        fragmentTransaction.commit();
    }

    protected void addFragment(int id, Fragment f) {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.main_container);

        if (fragment == null) {
            fragment = f;
            mFragmentManager.beginTransaction()
                    .add(R.id.main_container, fragment)
                    .commit();
        }
    }

    protected void replaceFragment(int id, Fragment f) {
        mFragmentManager.beginTransaction().replace(id, f).commit();
    }

    /**
     * Запрос разрешений.
     * @param successCallback callback, выполняющийся в случае успеха
     * @param failureCallback callback, выполняющийся в случае неудачи
     * @param permissions разрешения
     */
    protected void requestPermissions(Action successCallback, Action failureCallback, String...permissions) {
        new RxPermissions(this).requestEach(permissions)
                .subscribe(permission -> {
                    if (permission.granted) {
                        successCallback.run();
                    } else {
                        failureCallback.run();
                    }
                });
    }
}