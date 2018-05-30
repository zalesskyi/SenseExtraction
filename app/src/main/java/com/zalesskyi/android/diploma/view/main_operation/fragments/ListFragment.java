package com.zalesskyi.android.diploma.view.main_operation.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    @BindView(R.id.test_text)
    EditText mEditText;

    @BindView(R.id.send_btn)
    Button mSendBtn;

    public static ListFragment newInstance() {
        return new ListFragment();
    }


    public ListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListFragment(MainListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, parent, false);
        ButterKnife.bind(this, v);
        setupUI();
        return v;
    }

    private void setupUI() {
        mSendBtn.setOnClickListener(v -> {
            mListener.getAbstract("Легендарная группа «Битлз» зародилась в 1959 году в Великобритании, в городе Ливерпуле.");
        });
    }
}
