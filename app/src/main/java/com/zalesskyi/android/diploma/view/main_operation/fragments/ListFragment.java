package com.zalesskyi.android.diploma.view.main_operation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.realm.Abstract;
import com.zalesskyi.android.diploma.view.main_operation.adapters.AbstractsAdapter;
import com.zalesskyi.android.diploma.view.main_operation.listeners.ItemListener;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment implements MainListener.ListCallback {

    private MainListener mListener;
    private AbstractsAdapter mAdapter;

    private ItemListener mItemListener = new ItemListener() {
        @Override
        public void open(Abstract item) {
            mListener.open(item);
        }

        @Override
        public void share(Abstract item) {
            mListener.share(item);
        }

        @Override
        public void star(Abstract item) {
            mListener.star(item);
        }

        @Override
        public void openWith(Abstract item) {
            mListener.openWith(item);
        }

        @Override
        public void remove(Abstract item) {
            mListener.remove(item);
        }

        @Override
        public void openSource(Abstract item) {
            mListener.openSource(item);
        }
    };

    @BindView(R.id.main_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_list_view)
    View mEmptyListView;

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

        mListener.getListOfAbstractsFromRealm(this);
        return v;
    }

    @Override
    public void showEmptyList() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showList(List<Abstract> list) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyListView.setVisibility(View.GONE);
        for (Abstract abs : list) {
            mAdapter.addItem(abs);
        }
    }

    public void setListener(MainListener listener) {
        mListener = listener;
    }

    private void setupUI() {
        mAdapter = new AbstractsAdapter(getActivity(), mItemListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }
}
