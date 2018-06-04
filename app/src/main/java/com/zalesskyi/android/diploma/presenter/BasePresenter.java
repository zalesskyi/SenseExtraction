package com.zalesskyi.android.diploma.presenter;

import android.app.Application;
import android.os.Environment;

import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.utils.NetworkCheck;
import com.zalesskyi.android.diploma.view.BaseView;

public abstract class BasePresenter<V extends BaseView> {

    protected Application mApplication;
    protected NetworkCheck mNetworkCheck;
    protected Interactor mInteractor;
    protected RealmService mRealmService;
    protected V mView;

    /**
     * @return путь к директории с картинками страниц doc-файлов.
     *         Каждый файл имеет свою поддиректорию (название - имя doc-файла),
     *         в которой лежат файлы изображений его страниц (название = номер страницы).
     */
    protected String getPathToDocPageImages() {
        return mApplication.getFilesDir().getAbsolutePath() + "/docPages/";
    }

    /**
     * @return путь к директории с картинками миниатюр рефератов.
     *         Миниатюра - это изображение первой страницы файла.
     *         Выводится в главном списке ListFragment.
     */
    protected String getPathToThumbnails() {
        return mApplication.getFilesDir().getAbsolutePath() + "/thumbnails";
    }

    /**
     * @return путь к директории с рефератами.
     */
    protected String getPathToAbstracts() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/SenseExtraction/";
    }

    public void init(V v) {
        mView = v;
    }

    public void dismiss() {
        mView = null;
    }
}
