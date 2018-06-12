package com.zalesskyi.android.diploma.view.detail_operation.activities;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.app.App;
import com.zalesskyi.android.diploma.presenter.DetailPresenterImpl;
import com.zalesskyi.android.diploma.view.BaseActivity;
import com.zalesskyi.android.diploma.view.BaseView;
import com.zalesskyi.android.diploma.view.detail_operation.fragments.PageFragment;
import com.zalesskyi.android.diploma.view.detail_operation.listeners.DetailListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailActivity extends BaseActivity implements BaseView.DetailView {

    private static final String TAG = "DetailActivity";

    private static final String DETAIL_TYPE_EXTRA = "detail_type";
    private static final String PATH_EXTRA = "path";
    private static final String IS_UPLOADING_EXTRA = "is_uploading";

    public static final int DETAIL_TYPE_WEB_PAGE = 0;
    public static final int DETAIL_TYPE_TXT_FILE = 1;
    public static final int DETAIL_TYPE_DOC_FILE = 2;
    public static final int DETAIL_TYPE_PDF_FILE = 3;
    public static final int DETAIL_TYPE_CLIPBOARD_TEXT = 4;

    private int mDetailType;
    private String mPath;
    private int mCurrentPage;
    private int mDocPageCount;
    private boolean mIsUploading;

    @Inject
    DetailPresenterImpl mPresenter;

    @BindView(R.id.detail_pager)
    ViewPager mDocPager;

    @BindView(R.id.detail_web)
    WebView mWebView;

    @BindView(R.id.detail_pdf_view)
    PDFView mPdfView;

    @BindView(R.id.detail_txt_view)
    TextView mTxtView;

    @BindView(R.id.detail_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.detail_coordinator)
    View mCoordinator;

    private DetailListener mListener = imageView -> {
        mPresenter.doGetDocPageImage(mPath, mCurrentPage)
                .doOnRequest(l -> showProgress())
                .subscribe(imageView::setImageBitmap,
                        err -> showError(err.getMessage()),
                        this::hideProgress);
    };

    public static Intent newIntent(Context context, int detailType, String path, boolean isUploading) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DETAIL_TYPE_EXTRA, detailType);
        intent.putExtra(PATH_EXTRA, path);
        intent.putExtra(IS_UPLOADING_EXTRA, isUploading);
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
        mIsUploading = getIntent().getBooleanExtra(IS_UPLOADING_EXTRA, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        setupUI();
    }
    @Override
    public void showError(String err) {

    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupUI() {
        if (mDetailType == DETAIL_TYPE_WEB_PAGE) {
            mPdfView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            mDocPager.setVisibility(View.GONE);
            mTxtView.setVisibility(View.GONE);

            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    return false;
                }
            });
            mWebView.loadUrl(mPath);
        } else if (mDetailType == DETAIL_TYPE_DOC_FILE) {
            mPdfView.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            mDocPager.setVisibility(View.VISIBLE);
            mTxtView.setVisibility(View.GONE);

            mPresenter.doGetWordDocumentPageCount(mPath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnRequest(l -> showProgress())
                    .subscribe(pageCount -> {
                        mDocPageCount = pageCount;
                                if (mDocPageCount != 0) {
                                    mDocPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                                        @Override
                                        public Fragment getItem(int position) {
                                            mCurrentPage = position;
                                            return PageFragment.newInstance(mListener, position);
                                        }

                                        @Override
                                        public int getCount() {
                                            return mDocPageCount;
                                        }
                                    });
                                }
                    },
                            err -> showError(err.getMessage()), () -> hideProgress());
        } else if (mDetailType == DETAIL_TYPE_PDF_FILE) {
            mPdfView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            mDocPager.setVisibility(View.GONE);
            mTxtView.setVisibility(View.GONE);
            mPdfView.fromFile(new File(mPath))
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .spacing(3)
                    .onError( err -> {
                        Log.e(TAG, err.getMessage());
                    })
                    .load();
        } else if (mDetailType == DETAIL_TYPE_TXT_FILE) {
            mTxtView.setVisibility(View.VISIBLE);
            mPdfView.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            mDocPager.setVisibility(View.GONE);

            mTxtView.setMovementMethod(new ScrollingMovementMethod());
            displayTxtFile();
        } else if (mDetailType == DETAIL_TYPE_CLIPBOARD_TEXT) {
            mTxtView.setVisibility(View.VISIBLE);
            mPdfView.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            mDocPager.setVisibility(View.GONE);

            mTxtView.setMovementMethod(new ScrollingMovementMethod());
            mTxtView.setText(mPresenter.getTextFromClipboard());
        }

        if (mIsUploading) {
            Snackbar.make(mCoordinator, R.string.detail_upload_snackbar, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.yes, view -> {
                        if (mDetailType == DETAIL_TYPE_CLIPBOARD_TEXT) {
                            mPresenter.doGetAbstractFromString(mPresenter.getTextFromClipboard().toString());
                        } else if (mDetailType == DETAIL_TYPE_TXT_FILE) {
                            mPresenter.doGetAbstractFromTxt(mPath);
                        } else if (mDetailType == DETAIL_TYPE_PDF_FILE) {
                            mPresenter.doGetAbstractFromPdf(mPath);
                        } else if (mDetailType == DETAIL_TYPE_DOC_FILE) {
                            mPresenter.doGetAbstractFromDoc(mPath);
                        } else if (mDetailType == DETAIL_TYPE_WEB_PAGE) {
                            mPresenter.doGetAbstractFromUrl(mPath);
                        }
                    }).show();
        }
    }

    private void displayTxtFile() {
        StringBuilder source = new StringBuilder("");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(mPath), StandardCharsets.UTF_8))) {
            String buf;
            while ((buf = reader.readLine()) != null) {
                source.append(buf).append("\n");
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            showError(exc.getMessage());
        }
        mTxtView.setText(source);
    }
}
