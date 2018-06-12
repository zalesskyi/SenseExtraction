package com.zalesskyi.android.diploma.view.main_operation.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.presenter.MainPresenterImpl;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {

    private MainListener mListener;

    @BindView(R.id.main_list)
    RecyclerView mRecyclerView;

    public static ListFragment newInstance(MainListener listener) {
        ListFragment fragment = new ListFragment();
        fragment.setListener(listener);
        return fragment;
    }


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, parent, false);
        ButterKnife.bind(this, v);
        setupUI();
        return v;
    }

    public void setListener(MainListener listener) {
        mListener = listener;
    }

    private void setupUI() {

    }
}
