package com.zalesskyi.android.diploma.view.main_operation;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.zalesskyi.android.diploma.R;


public class FabBehavior extends FloatingActionButton.Behavior {
    private CoordinatorLayout.LayoutParams mParams;
    private Context mContext;
    private FloatingActionButton mFab;

    public FabBehavior(Context ctx, AttributeSet attrSet) {
        super(ctx, attrSet);
        mContext = ctx;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child, View target,
                               int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target,
                dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        mFab = child;

        if (dyConsumed > 0) {
            mParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = mParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fabBottomMargin)
                    .setInterpolator(new AccelerateInterpolator()).setDuration(
                    mContext.getResources().getInteger(R.integer.float_button_duration))
                    .start();
        } else if (dyConsumed < 0) {
            child.animate().translationY(0)
                    .setInterpolator(new DecelerateInterpolator()).setDuration(
                    mContext.getResources().getInteger(R.integer.float_button_duration))
                    .start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxess) {
        return nestedScrollAxess == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}