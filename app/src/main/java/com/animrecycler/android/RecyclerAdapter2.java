package com.animrecycler.android;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lebron on 17-4-25.
 */

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.RecyclerViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<UserModel> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerAdapter2(Context context, List<UserModel> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = list;
    }

    @Override
    public RecyclerAdapter2.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(mLayoutInflater.inflate(R.layout.checked_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final UserModel userModel = mDatas.get(position);
        Log.i("Lebron", " current position " + position);
        holder.nameView.setText(userModel.getName());
        holder.iconView.setImageResource(userModel.getIcon());
        holder.checkedView.setVisibility(userModel.isChecked() ? View.VISIBLE : View.GONE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.itemView,"scaleX",0,1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.itemView,"scaleY",0,1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.setDuration(500);
        animatorSet.start();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.itemView,"scaleX",1,0);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.itemView,"scaleY",1,0);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scaleX).with(scaleY);
                animatorSet.setDuration(500);
                animatorSet.start();
                mOnItemClickListener.onClick(userModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView nameView;
        ImageView checkedView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.user_icon_img);
            checkedView = (ImageView) itemView.findViewById(R.id.user_checked_img);
            nameView = (TextView) itemView.findViewById(R.id.user_name_txv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(UserModel userModel);
    }
}
