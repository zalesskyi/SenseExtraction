package com.zalesskyi.android.diploma.view.detail_operation.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.view.detail_operation.listeners.DetailListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends Fragment {

    private static final String PAGE_ARG_KEY = "page_arg";

    private DetailListener mListener;
    private int mCurrentPage;

    @BindView(R.id.page_image_view)
    ImageView mPageImage;

    public static PageFragment newInstance(DetailListener listener, int page) {
        PageFragment fragment = new PageFragment();
        fragment.setListener(listener);
        Bundle args = new Bundle();
        args.putInt(PAGE_ARG_KEY, page);
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment() {
        // required
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPage = getArguments().getInt(PAGE_ARG_KEY, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page, parent, false);
        ButterKnife.bind(this, v);

        mListener.displayDocPage(mPageImage);
        return v;
    }

    public void setListener(DetailListener listener) {
        mListener = listener;
    }
}
