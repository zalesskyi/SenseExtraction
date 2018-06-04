package com.zalesskyi.android.diploma.view.main_operation.activities;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.app.App;
import com.zalesskyi.android.diploma.presenter.MainPresenterImpl;
import com.zalesskyi.android.diploma.presenter.PresenterContract;
import com.zalesskyi.android.diploma.view.BaseActivity;
import com.zalesskyi.android.diploma.view.BaseView;
import com.zalesskyi.android.diploma.view.detail_operation.activities.DetailActivity;
import com.zalesskyi.android.diploma.view.main_operation.fragments.BottomSheetFragment;
import com.zalesskyi.android.diploma.view.main_operation.fragments.ListFragment;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooser;
import rx.Observable;


public class MainActivity extends BaseActivity
        implements BaseView.MainView {

    private BottomSheetFragment mBottomSheet;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private MainListener mListener = new MainListener() {

        @Override
        public Observable<String> getDocFile() {
            return chooseFile(".doc", ".docx");
        }

        @Override
        public Observable<String> getLink() {
            return null;
        }

        @Override
        public Observable<String> getPdfFile() {
            return chooseFile(".pdf");
        }

        @Override
        public Observable<String> getTxtFile() {
            return chooseFile(".txt");
        }

        @Override
        public Observable<String> getFileFromDrive() {
            return null;
        }

        @Override
        public void openTxtFile(String path) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, DetailActivity.DETAIL_TYPE_TXT_FILE, path));
        }

        @Override
        public void openPdfFile(String path) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, DetailActivity.DETAIL_TYPE_PDF_FILE, path));
        }

        @Override
        public void openDocFile(String path) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, DetailActivity.DETAIL_TYPE_DOC_FILE, path));
        }

        @Override
        public void openWebPage(String url) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, DetailActivity.DETAIL_TYPE_WEB_PAGE, url));
        }
    };

    @Inject
    MainPresenterImpl mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.get(this).getAppComponent().inject(this);
        ButterKnife.bind(this);
        mPresenter.init(this);

        addFragment(R.id.main_container, new ListFragment(mListener));
        setupUI();
    }

    @Override
    public void showError(String err) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    private void setupUI() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.DKGRAY);

        mFab.setOnClickListener(v -> {
            requestPermissions(() -> {
                mBottomSheet = new BottomSheetFragment(mListener);
                mBottomSheet.show(mFragmentManager, mBottomSheet.getTag());
            }, () -> showError("This permission is needed"), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        });
    }

    /**
     * Выбор файла из файловой системы устройства.
     *
     * @param formats расширение(я) файла(ов)
     * @return observable which emits path
     */
    private Observable<String> chooseFile(String... formats) {
        return Observable.create(subscriber -> {
            try {
                FileChooser chooserFragment = new FileChooser.Builder(FileChooser.ChooserType.FILE_CHOOSER, path -> {
                    replaceFragment(R.id.main_container, new ListFragment(mListener));
                    subscriber.onNext(path);
                    subscriber.onCompleted();
                    mFab.setVisibility(View.VISIBLE);
                }).setFileFormats(formats).build();
                replaceWithAnimFragment(R.id.main_container, chooserFragment);
                mFragmentManager.beginTransaction().remove(mBottomSheet).commit();
                mFab.setVisibility(View.INVISIBLE);
            } catch (ExternalStorageNotAvailableException exc) {
                subscriber.onError(exc);
            }
        });
    }
}
