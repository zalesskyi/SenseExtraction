package com.zalesskyi.android.diploma.view.main_operation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.realm.Abstract;
import com.zalesskyi.android.diploma.view.main_operation.listeners.ItemListener;

import java.util.ArrayList;
import java.util.List;

public class AbstractsAdapter extends RecyclerView.Adapter<AbstractHolder> {

    private List<Abstract> mAbstracts;
    private Context mContext;
    private ItemListener mItemListener;

    public AbstractsAdapter(Context ctx, ItemListener listener) {
        mAbstracts = new ArrayList<>();
        mContext = ctx;
        mItemListener = listener;
    }

    @NonNull
    @Override
    public AbstractHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_abstract, parent, false);

        return new AbstractHolder(v, mItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractHolder holder, int position) {
        holder.bindAbstract(mAbstracts.get(position));
    }

    @Override
    public int getItemCount() {
        return mAbstracts.size();
    }

    public void addItem(Abstract abs) {
        mAbstracts.add(abs);
        notifyDataSetChanged();
    }

    public void deleteItem(Abstract abs) {
        mAbstracts.remove(abs);
        notifyDataSetChanged();
    }
}
