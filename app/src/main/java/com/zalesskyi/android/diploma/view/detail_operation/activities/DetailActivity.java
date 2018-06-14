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
import com.zalesskyi.android.diploma.realm.Abstract;
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
    public void onDestroy() {
        super.onDestroy();

        mPresenter.dismiss();
    }

    @Override
    public void displayAbstractFile(String pathToAbstract) {
        Intent i = newIntent(this, Abstract.TXT_TYPE, pathToAbstract, false);
        startActivity(i);
        finish();
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
        if (mDetailType == Abstract.WEB_TYPE) {
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
        } else if (mDetailType == Abstract.DOC_TYPE) {
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
        } else if (mDetailType == Abstract.PDF_TYPE) {
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
        } else if (mDetailType == Abstract.TXT_TYPE) {
            mPdfView.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            mDocPager.setVisibility(View.GONE);

            mTxtView.setMovementMethod(new ScrollingMovementMethod());
            displayTxtFile();
        } else if (mDetailType == Abstract.CLIPBOARD_TYPE) {
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
                        if (mDetailType == Abstract.CLIPBOARD_TYPE) {
                            mPresenter.doGetAbstractFromString(mPresenter.getTextFromClipboard().toString());
                        } else if (mDetailType == Abstract.TXT_TYPE) {
                            mPresenter.doGetAbstractFromTxt(mPath);
                        } else if (mDetailType == Abstract.PDF_TYPE) {
                            mPresenter.doGetAbstractFromPdf(mPath);
                        } else if (mDetailType == Abstract.DOC_TYPE) {
                            mPresenter.doGetAbstractFromDoc(mPath);
                        } else if (mDetailType == Abstract.WEB_TYPE) {
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
