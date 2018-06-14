package com.zalesskyi.android.diploma.view.main_operation.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zalesskyi.android.diploma.R;
import com.zalesskyi.android.diploma.realm.Abstract;
import com.zalesskyi.android.diploma.view.main_operation.listeners.ItemListener;

import java.io.File;

import butterknife.BindView;

public class AbstractHolder extends RecyclerView.ViewHolder {

    private ItemListener mListener;

    private View mView;

    @BindView(R.id.item_abstract_thumb)
    ImageView mThumbView;

    @BindView(R.id.item_abstract_name)
    TextView mNameView;

    @BindView(R.id.item_abstract_file_type)
    ImageView mTypeView;

    @BindView(R.id.item_abstract_menu)
    TextView mMenu;

    public AbstractHolder(View itemView, ItemListener listener) {
        super(itemView);
        mView = itemView;

        mListener = listener;
    }

    public void bindAbstract(Abstract abs) {
        setupThumb(abs.getPathToSourceThumb(), abs.getType());
        mNameView.setText(abs.getPathToSource());
    }

    private void setupThumb(String pathToThumb, int fileType) {
        Bitmap bm = getBitmapFromFile(pathToThumb);

        if (bm != null) {
            mThumbView.setImageBitmap(bm);
        } else if (fileType == Abstract.CLIPBOARD_TYPE) {
            mThumbView.setImageResource(R.drawable.ic_clipboard);
        } else if (fileType == Abstract.DOC_TYPE) {
            mThumbView.setImageResource(R.drawable.ic_doc_x_file);
        } else if (fileType == Abstract.PDF_TYPE) {
            mThumbView.setImageResource(R.drawable.ic_pdf_file);
        } else if (fileType == Abstract.TXT_TYPE) {
            mThumbView.setImageResource(R.drawable.ic_txt_file);
        } else if (fileType == Abstract.WEB_TYPE) {
            mThumbView.setImageResource(R.drawable.ic_link);
        }
    }

    @Nullable
    private Bitmap getBitmapFromFile(String path) {
        File image = new File(path);
        if (!image.exists()) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(
                image.getAbsolutePath(), new BitmapFactory.Options());

        return Bitmap.createScaledBitmap(bitmap,
                mThumbView.getWidth(), mThumbView.getHeight(), true);
    }
}
