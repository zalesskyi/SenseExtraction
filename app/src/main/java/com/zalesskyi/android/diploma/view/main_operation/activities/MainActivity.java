package com.zalesskyi.android.diploma.view.main_operation.activities;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.app.App;
import com.zalesskyi.android.diploma.presenter.MainPresenterImpl;
import com.zalesskyi.android.diploma.presenter.PresenterContract;
import com.zalesskyi.android.diploma.realm.Abstract;
import com.zalesskyi.android.diploma.view.BaseActivity;
import com.zalesskyi.android.diploma.view.BaseView;
import com.zalesskyi.android.diploma.view.detail_operation.activities.DetailActivity;
import com.zalesskyi.android.diploma.view.detail_operation.listeners.DetailListener;
import com.zalesskyi.android.diploma.view.main_operation.adapters.AbstractsAdapter;
import com.zalesskyi.android.diploma.view.main_operation.fragments.BottomSheetFragment;
import com.zalesskyi.android.diploma.view.main_operation.fragments.LinkDialogFragment;
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
            return enterLink();
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
        public void openTxtFile(String path, boolean isForUploading) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, Abstract.TXT_TYPE, path, isForUploading));
        }

        @Override
        public void openPdfFile(String path, boolean isForUploading) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, Abstract.PDF_TYPE, path, isForUploading));
        }

        @Override
        public void openDocFile(String path, boolean isForUploading) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, Abstract.DOC_TYPE, path, isForUploading));
        }

        @Override
        public void openWebPage(String url, boolean isForUploading) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, Abstract.WEB_TYPE, url, isForUploading));
        }

        @Override
        public void openClipboardText(boolean isForUploading) {
            startActivity(DetailActivity.newIntent(MainActivity.this,
                    Abstract.CLIPBOARD_TYPE, null, isForUploading));
        }

        @Override
        public void getListOfAbstractsFromRealm(ListCallback callback) {
            mPresenter.doGetListFromRealm(callback);
        }

        @Override
        public void open(Abstract item) {
            startActivity(DetailActivity.newIntent(MainActivity.this,
                    item.getType(), item.getPathToAbstract(), false));
        }

        @Override
        public void share(Abstract item) {
            // todo
        }

        @Override
        public void star(Abstract item) {
            new Abstract.Builder(item)
                    .setIsFavorite(true).build();
        }

        @Override
        public void openWith(Abstract item) {

        }

        @Override
        public void remove(Abstract item) {
            mPresenter.doRemoveItemFromList(item);
        }

        @Override
        public void openSource(Abstract item) {
            startActivity(DetailActivity.newIntent(
                    MainActivity.this, item.getType(), item.getPathToSource(), false));
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

        addFragment(R.id.main_container, ListFragment.newInstance(mListener));
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
                mBottomSheet = BottomSheetFragment.newInstance(mListener);
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
                    mFragmentManager.beginTransaction().remove(mBottomSheet).commit();
                    replaceFragment(R.id.main_container, ListFragment.newInstance(mListener));
                    subscriber.onNext(path);
                    subscriber.onCompleted();
                    mFab.setVisibility(View.VISIBLE);
                }).setFileFormats(formats).setFileIcon(getFileIcon(formats[0])).build();
                replaceWithAnimFragment(R.id.main_container, chooserFragment);
                mFragmentManager.beginTransaction().remove(mBottomSheet).commit();
                mFab.setVisibility(View.INVISIBLE);
            } catch (ExternalStorageNotAvailableException exc) {
                subscriber.onError(exc);
            }
        });
    }

    private int getFileIcon(String format) {
        if (format.equals(".pdf")) {
            return R.drawable.ic_pdf_file;
        } else if (format.equals(".txt")) {
            return R.drawable.ic_txt_file;
        } else {
            return R.drawable.ic_doc_x_file;
        }
    }

    private Observable<String> enterLink() {
        return Observable.create(subscriber ->  {
            mFragmentManager.beginTransaction().remove(mBottomSheet).commit();
            LinkDialogFragment.newInstance(subscriber).show(mFragmentManager, "LinkDialogFragment");
        });
    }
}
