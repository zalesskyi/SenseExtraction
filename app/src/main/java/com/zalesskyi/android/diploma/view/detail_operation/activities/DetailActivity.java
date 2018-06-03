package com.zalesskyi.android.diploma.view.detail_operation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.webkit.WebView;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.app.App;
import com.zalesskyi.android.diploma.presenter.PresenterContract;
import com.zalesskyi.android.diploma.view.BaseActivity;
import com.zalesskyi.android.diploma.view.BaseView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements BaseView.DetailView {

    private static final String DETAIL_TYPE_EXTRA = "detail_type";
    private static final String PATH_EXTRA = "path";

    public static final int DETAIL_TYPE_WEB_PAGE = 0;
    public static final int DETAIL_TYPE_TXT_FILE = 1;
    public static final int DETAIL_TYPE_DOC_FILE = 2;
    public static final int DETAIL_TYPE_PDF_FILE = 3;
    // todo                 DETAIL_TYPE_GOOGLE_DRIVE

    private int mDetailType;
    private String mPath;

    @Inject
    PresenterContract.DetailPresenter mPresenter;

    @BindView(R.id.detail_pager)
    ViewPager mPager;

    @BindView(R.id.detail_web)
    WebView mWebView;

    public static Intent newIntent(Context context, int detailType, String path) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DETAIL_TYPE_EXTRA, detailType);
        intent.putExtra(PATH_EXTRA, path);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        App.get(this).getAppComponent().inject(this);
        ButterKnife.bind(this);

        mDetailType = getIntent().getIntExtra(DETAIL_TYPE_EXTRA, 1);
        mPath = getIntent().getStringExtra(PATH_EXTRA);

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

    }
}
