package com.ckr.treeselector.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : ckr
 * @date : 2019/10/12 14:21
 * @description :
 */
public abstract class AppFragment extends BaseFragment {
    protected static final String FLAG = "flag";
    protected static final String INDEX = "index";
    protected static final int DEFAULT_VALUE = -1;
    private Unbinder unbinder;
    protected View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView != null) {
            unbinder = ButterKnife.bind(this, mRootView);
        }
        init();
        return mRootView;
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected boolean mFlag = false;

    public boolean getFlag() {
        return mFlag;
    }

    public void setFlag(boolean mFlag) {
        this.mFlag = mFlag;
    }

    public void lazyLoad(boolean isRefresh) {
    }
}
