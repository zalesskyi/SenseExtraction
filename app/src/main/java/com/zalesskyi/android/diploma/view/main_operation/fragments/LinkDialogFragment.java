package com.zalesskyi.android.diploma.view.main_operation.fragments;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.zalesskyi.android.diploma.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class LinkDialogFragment extends DialogFragment {

    private Subscriber<? super String> mSubscriber;

    @BindView(R.id.link_edit_text)
    EditText mLinkEditText;

    public static LinkDialogFragment newInstance(Subscriber<? super String> subscriber) {
        LinkDialogFragment fragment = new LinkDialogFragment();
        fragment.mSubscriber = subscriber;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedIsntanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_dialog_link, null);
        ButterKnife.bind(this, v);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_link_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, (di, i) -> {
                    mSubscriber.onNext(mLinkEditText.getText().toString());
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorAccent));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorAccent));
        });
        return dialog;
    }

    public int getColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getActivity().getResources()
                    .getColor(color, getActivity().getTheme());
        }
        return getActivity().getResources().getColor(color);
    }
}
