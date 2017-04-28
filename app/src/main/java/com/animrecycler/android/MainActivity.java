package com.animrecycler.android;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class MainActivity extends AppCompatActivity {
    public static final int[] USER_ICON_ARRAY = new int[]{R.mipmap.icon, R.mipmap.icon1, R.mipmap.icon2, R.mipmap.icon4,
            R.mipmap.icon5, R.mipmap.icon6, R.mipmap.icon7, R.mipmap.icon8, R.mipmap.icon9, R.mipmap.icon10, R.mipmap.icon22};
    private RecyclerView mRecyclerView, mCheckedRecyclerView;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private RecyclerAdapter mAdapter;
    private RecyclerAdapter2 mAdapter2;
    private List<UserModel> mDatas, mCheckedDatas;
    private int mTranslationY = 258;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTranslationY = dip2px(86);
        initView();
        initToobar();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mCheckedRecyclerView = (RecyclerView) findViewById(R.id.checked_recyclerview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initToobar() {
        this.setSupportActionBar(mToolbar);
        ActionBar ab = this.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setSubtitle(R.string.default_title_toolbar);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        mCheckedDatas = new ArrayList<>();
        String[] nameArrays = getResources().getStringArray(R.array.friends_array);
        for (int i = 0; i < USER_ICON_ARRAY.length; i++) {
            UserModel userModel = new UserModel();
            userModel.setName(nameArrays[i]);
            userModel.setIcon(USER_ICON_ARRAY[i]);
            userModel.setDescribe("个性签名" + i);
            userModel.setChecked(false);
            mDatas.add(userModel);
        }

        mAdapter = new RecyclerAdapter(this, mDatas);
        mAdapter2 = new RecyclerAdapter2(this, mCheckedDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(UserModel userModel) {
                if (!userModel.isChecked()) {
                    userModel.setChecked(true);
                    mCheckedDatas.add(userModel);
                    mAdapter2.notifyItemInserted(mCheckedDatas.size() - 1);
                    mCheckedRecyclerView.scrollToPosition(mCheckedDatas.size() - 1);
                    //如果是新增的第一个，执行下移动画
                    if (mCheckedDatas.size() == 1) {
                        updateCheckedLayout(0, mTranslationY);
                    }
                } else {
                    userModel.setChecked(false);
                    int tempPos = mCheckedDatas.indexOf(userModel);
                    mCheckedDatas.remove(userModel);
                    mAdapter2.notifyItemRemoved(tempPos);
                    //如果是移除了所有，执行上移动画
                    if (mCheckedDatas.size() == 0) {
                        updateCheckedLayout(mTranslationY, 0);
                    }
                }
                mAdapter.notifyDataSetChanged();
                updateToolbarSubTitle();
            }
        });
        //mRecyclerView.setItemAnimator(new ScaleInAnimator());
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCheckedRecyclerView.setLayoutManager(layoutManager);
        mAdapter2.setOnItemClickListener(new RecyclerAdapter2.OnItemClickListener() {
            @Override
            public void onClick(UserModel userModel) {
                int tempPos = mCheckedDatas.indexOf(userModel);
                userModel.setChecked(false);
                mCheckedDatas.remove(userModel);
                mAdapter2.notifyItemRemoved(tempPos);
                mAdapter.notifyDataSetChanged();
                updateToolbarSubTitle();
                //如果是移除了所有，执行上移动画
                if (mCheckedDatas.size() == 0) {
                    updateCheckedLayout(mTranslationY, 0);
                }
            }
        });
        //mCheckedRecyclerView.setItemAnimator(new ScaleInAnimator());
        mCheckedRecyclerView.setAdapter(mAdapter2);
    }

    private void updateToolbarSubTitle() {
        if (mCheckedDatas.size() <= 0) {
            mToolbar.setSubtitle(R.string.default_title_toolbar);
        } else {
            mToolbar.setSubtitle("Choose: " + mCheckedDatas.size() + "/11");
        }
    }

    private void updateCheckedLayout(final float start, final float end) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRecyclerView, "translationY", start, end);
        objectAnimator.setDuration(500L);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (start > end) {
                    mRecyclerView.setPadding(0, 0, 0, 0);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (end > start) {
                    mRecyclerView.setPadding(0, 0, 0, mTranslationY);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
