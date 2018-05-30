package com.zalesskyi.android.diploma.app.di;

import com.zalesskyi.android.diploma.view.main_operation.activities.MainActivity;

import javax.inject.Inject;

import dagger.Component;

@Component(modules = {ApiModule.class, AppModule.class, PresenterModule.class})
public interface AppComponent {

    void inject(MainActivity activity);
}
