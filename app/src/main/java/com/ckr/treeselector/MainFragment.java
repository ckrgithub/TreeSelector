package com.ckr.treeselector;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PageRecyclerView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ckr.pageview.adapter.BasePageAdapter;
import com.ckr.pageview.view.PageView;
import com.ckr.treeselector.common.AppFragment;
import com.ckr.treeselector.common.BaseAdapter;
import com.ckr.treeselector.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author : ckr
 * @date : 2020/4/24 11:28
 * @description :
 */
public class MainFragment extends AppFragment {
    private static final String TAG = "MainFragment";
    @BindView(R.id.pageView)
    PageView mPageView;
    @BindView(R.id.navigationView)
    BottomNavigationView mBottomNavigationView;
    private MainAdapter mMainAdapter;
    private int mPosition;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init() {
        Log.d(TAG, "init: ");
        mPosition = 0;
        mPageView.addOnPageChangeListener(new PageRecyclerView.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: position=" + position + ",positionOffset=" + positionOffset + ",positionOffsetPixels=" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: position=" + position + ",mPosition=" + mPosition);
                if (mPosition != position) {
                    Menu menu = mBottomNavigationView.getMenu();
                    MenuItem item = menu.getItem(position);
                    item.setChecked(true);
                    mPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: state=" + state);

            }
        });

        mBottomNavigationView.inflateMenu(R.menu.menu_main);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                Log.d(TAG, "onNavigationItemSelected: itemId=" + itemId + ",mPosition=" + mPosition);
                switch (itemId) {
                    case R.id.main_multi_check:
                        menuItem.setChecked(true);
                        if (mPosition != 0) {
                            if (mPageView != null) {
                                mPageView.setCurrentItem(mPosition = 0, true);
                            }
                        }
                        break;
                    case R.id.main_single_check:
                        menuItem.setChecked(true);
                        if (mPosition != 1) {
                            if (mPageView != null) {
                                mPageView.setCurrentItem(mPosition = 1, true);
                            }
                        }
                        break;
                    default:
                        break;
                }
                return mPosition != -1;
            }
        });

        mMainAdapter = new MainAdapter(getContext());
        mPageView.setAdapter(mMainAdapter);
        Menu menu = mBottomNavigationView.getMenu();
        int size = menu.size();
        Log.d(TAG, "init: size=" + size);
        List<MainType> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            MainType mainType = new MainType();
            mainType.index = i;
            mainType.layoutId = R.layout.layout_recycler;
            mainType.title = menu.getItem(i).getTitle() + "";
            data.add(mainType);
        }
        mMainAdapter.updateAll(data);
    }


    class MainAdapter extends BasePageAdapter<MainType, MainAdapter.MainHolder> {


        public MainAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        protected int getLayoutId(int viewType) {
            MainType mainType = mTargetData.get(viewType);
            if (mainType != null) {
                return mainType.layoutId;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        protected MainHolder getViewHolder(View itemView, int viewType) {
            return new MainHolder(itemView, viewType);
        }

        @Override
        protected void convert(MainHolder holder, int originalPos, MainType originalItem, int adjustedPos, MainType adjustedItem) {
            if (adjustedItem != null) {
                String title = adjustedItem.title;
                holder.textView.setText(title);
                RecyclerView.Adapter adapter = holder.recyclerView.getAdapter();
                RecyclerView.LayoutManager layoutManager = holder.recyclerView.getLayoutManager();
                if (layoutManager == null) {
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                }
                Log.d(TAG, "convert: adapter=" + adapter);
                if (adapter == null) {
                    holder.recyclerView.setAdapter(new TreeAdapter(mContext));
                }
            }
        }

        class MainHolder extends RecyclerView.ViewHolder {

            private final RecyclerView recyclerView;
            private final TextView textView;

            public MainHolder(@NonNull View itemView, int viewType) {
                super(itemView);
                recyclerView = itemView.findViewById(R.id.recyclerView);
                textView = itemView.findViewById(R.id.textView);
            }
        }
    }

    class TreeAdapter extends BaseAdapter<String, TreeAdapter.TreeHolder> {


        public TreeAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_tree;
        }

        @Override
        protected TreeHolder getViewHolder(View itemView, int viewType) {
            return new TreeHolder(itemView);
        }

        @Override
        protected void convert(TreeHolder holder, int position, String s) {
            holder.textView.setText("item " + position);
        }

        class TreeHolder extends RecyclerView.ViewHolder {

            private final TextView textView;
            private final AppCompatCheckBox checkbox;

            public TreeHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
                checkbox = itemView.findViewById(R.id.checkbox);
            }
        }
    }

    class MainType {
        int index;
        int layoutId;
        String type;
        String title;
    }
}
