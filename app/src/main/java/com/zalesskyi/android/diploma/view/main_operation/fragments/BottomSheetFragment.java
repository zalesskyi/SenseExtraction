package com.zalesskyi.android.diploma.view.main_operation.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetFragment";

    private MainListener mListener;

    @BindView(R.id.bs_link)
    View mLinkView;

    @BindView(R.id.bs_txt)
    View mTxtView;

    @BindView(R.id.bs_pdf)
    View mPdfView;

    @BindView(R.id.bs_doc)
    View mDocView;

    @BindView(R.id.bs_drive)
    View mDriveView;

    private View.OnClickListener mClickListener = v -> {
        if (v.equals(mLinkView)) {
            // todo link
        } else if (v.equals(mTxtView)) {
            mListener.getTxtFile()
                    .subscribe(path -> {
                       Log.i(TAG, path);
                    });
        } else if (v.equals(mPdfView)) {
            mListener.getPdfFile()
                    .subscribe(path -> {
                        Log.i(TAG, path);
                    });
        } else if (v.equals(mDocView)) {
            mListener.getDocFile()
                    .subscribe(path -> {
                        Log.i(TAG, path);
                    });
        } else if (v.equals(mDriveView)) {
            // todo drive
        }
    };


    public BottomSheetFragment() {
        // required
    }

    @SuppressLint("ValidFragment")
    public BottomSheetFragment(MainListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_abstract_bottom_sheet, parent, false);
        ButterKnife.bind(this, v);
        setupUI();

        return v;
    }

    private void setupUI() {
        mLinkView.setOnClickListener(mClickListener);
        mTxtView.setOnClickListener(mClickListener);
        mPdfView.setOnClickListener(mClickListener);
        mDocView.setOnClickListener(mClickListener);
        mDriveView.setOnClickListener(mClickListener);
    }
}
