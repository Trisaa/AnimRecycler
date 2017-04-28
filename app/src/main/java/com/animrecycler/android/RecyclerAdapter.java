package com.animrecycler.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lebron on 17-4-25.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<UserModel> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerAdapter(Context context, List<UserModel> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = list;
    }

    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(mLayoutInflater.inflate(R.layout.contacts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final UserModel userModel = mDatas.get(position);
        holder.nameView.setText(userModel.getName());
        holder.describeView.setText(userModel.getDescribe());
        holder.iconView.setImageResource(userModel.getIcon());
        holder.checkedView.setVisibility(userModel.isChecked() ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        TextView describeView;
        ImageView checkedView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.user_icon_img);
            checkedView = (ImageView) itemView.findViewById(R.id.user_checked_img);
            nameView = (TextView) itemView.findViewById(R.id.user_name_txv);
            describeView = (TextView) itemView.findViewById(R.id.user_describe_txv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(UserModel userModel);
    }
}
