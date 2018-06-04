package com.zalesskyi.android.diploma.view.detail_operation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.aspose.words.Document;
import com.github.barteksc.pdfviewer.PDFView;
import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.app.App;
import com.zalesskyi.android.diploma.presenter.DetailPresenterImpl;
import com.zalesskyi.android.diploma.view.BaseActivity;
import com.zalesskyi.android.diploma.view.BaseView;
import com.zalesskyi.android.diploma.view.detail_operation.fragments.PageFragment;
import com.zalesskyi.android.diploma.view.detail_operation.listeners.DetailListener;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements BaseView.DetailView {

    private static final String TAG = "DetailActivity";

    private static final String DETAIL_TYPE_EXTRA = "detail_type";
    private static final String PATH_EXTRA = "path";

    public static final int DETAIL_TYPE_WEB_PAGE = 0;
    public static final int DETAIL_TYPE_TXT_FILE = 1;
    public static final int DETAIL_TYPE_DOC_FILE = 2;
    public static final int DETAIL_TYPE_PDF_FILE = 3;

    private int mDetailType;
    private String mPath;
    private int mCurrentPage;
    private int mDocPageCount;

    @Inject
    DetailPresenterImpl mPresenter;

    @BindView(R.id.detail_pager)
    ViewPager mDocPager;

    @BindView(R.id.detail_web)
    WebView mWebView;

    @BindView(R.id.detail_pdf_view)
    PDFView mPdfView;

    private DetailListener mListener = imageView -> {
        mPresenter.doGetDocPageImage(mPath, mCurrentPage)
                .doOnRequest(l -> showProgress())
                .subscribe(imageView::setImageBitmap,
                        err -> showError(err.getMessage()),
                        this::hideProgress);
    };

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
        mPresenter.init(this);

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
        if (mDetailType == DETAIL_TYPE_WEB_PAGE) {
            mPdfView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            mDocPager.setVisibility(View.GONE);

        } else if (mDetailType == DETAIL_TYPE_DOC_FILE) {
            mPdfView.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            mDocPager.setVisibility(View.VISIBLE);

            mDocPageCount = mPresenter.doGetWordDocumentPageCount(mPath);

            if (mDocPageCount != 0) {
                mDocPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        mCurrentPage = position;
                        return new PageFragment(mListener, position);
                    }

                    @Override
                    public int getCount() {
                        return mDocPageCount;
                    }
                });
            }
        } else if (mDetailType == DETAIL_TYPE_PDF_FILE) {
            mPdfView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            mDocPager.setVisibility(View.GONE);
            mPdfView.fromFile(new File(mPath))
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .spacing(3)
                    .onError( err -> {
                        Log.e(TAG, err.getMessage());
                    })
                    .load();
        } else if (mDetailType == DETAIL_TYPE_TXT_FILE) {

        }
    }
}
