package com.ckr.treeselector.common;


import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * Created on 2019/1/4
 *
 * @author ckr
 * @description 包含懒加载的基类fragment
 */
public abstract class BaseFragment extends Fragment {
    private boolean mIsFirstVisible = true;
    private boolean mUserVisible = false;
    private boolean mViewCreated = false;

    protected abstract @LayoutRes
    int getLayoutId();

    protected abstract void init();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mViewCreated) {
            if (isVisibleToUser && !mUserVisible) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && mUserVisible) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final int layoutId = getLayoutId();
        View view = null;
        if (layoutId > 0) {
            view = inflater.inflate(layoutId, container, false);
        }
        mViewCreated = true;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isHidden() && getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstVisible) {
            if (!isHidden() && !mUserVisible && getUserVisibleHint()) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mUserVisible && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsFirstVisible = true;
        mViewCreated = false;
    }

    private void dispatchUserVisibleHint(boolean visible) {
        if (visible && !isParentVisible()) {
            return;
        }
        if (mUserVisible == visible) {
            return;
        }
        mUserVisible = visible;
        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onVisible(true);
            } else {
                onVisible(false);
            }
            dispatchChildVisibleState(true);
        } else {
            dispatchChildVisibleState(false);
            onInvisible();
        }
    }

    private boolean isParentVisible() {
        Fragment fragment = getParentFragment();
        if (fragment == null) {
            return true;
        }
        if (fragment instanceof BaseFragment) {
            BaseFragment lazyFragment = (BaseFragment) fragment;
            return lazyFragment.isSupportUserVisible();
        }
        return fragment.isVisible();
    }

    private boolean isSupportUserVisible() {
        return mUserVisible;
    }

    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment child : fragments) {
                if (child instanceof BaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    ((BaseFragment) child).dispatchUserVisibleHint(visible);
                }
            }
        }
    }

    protected void onVisible(boolean isFirstVisible) {
    }

    protected void onInvisible() {
    }

}
