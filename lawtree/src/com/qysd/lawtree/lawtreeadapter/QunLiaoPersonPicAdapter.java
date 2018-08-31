package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.team.model.Team;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 群聊图片的适配器
 */

public class QunLiaoPersonPicAdapter extends RecyclerView.Adapter<QunLiaoPersonPicAdapter.ViewHoder> {
    private List<MyQiyeQunLiaoBean.Status> list = new ArrayList<>();

    public QunLiaoPersonPicAdapter(List<MyQiyeQunLiaoBean.Status> list) {
        this.list = list;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tuxiang_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HeadImageView iv_person_pic;

        public ViewHoder(View itemView) {
            super(itemView);
            iv_person_pic = itemView.findViewById(R.id.iv_person_pic);
            itemView.setOnClickListener(this);
        }

        public void setData(List<MyQiyeQunLiaoBean.Status> list, final int position) {
            iv_person_pic.doLoadImage(list.get(position).getHeadUrl(), R.drawable.ic_lianxiren_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    /**
     * item的点击事件
     */
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
