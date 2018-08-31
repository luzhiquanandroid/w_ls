package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.ShenChanPlanBean;
import com.qysd.lawtree.lawtreebean.YingyongBean;
import com.qysd.lawtree.lawtreeutils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产计划已完成和待完成适配器
 */
public class YingYongItemAdapter extends RecyclerView.Adapter<YingYongItemAdapter.ViewHoder> {
    private List<YingyongBean.Status.SubMenuItems> list = new ArrayList<>();

    public YingYongItemAdapter(List<YingyongBean.Status.SubMenuItems> list) {
        this.list = list;
    }

    @Override
    public YingYongItemAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_yingyong_item_icon, parent, false));
    }

    @Override
    public void onBindViewHolder(YingYongItemAdapter.ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_name;
        private ImageView iv_iamge;

        public ViewHoder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_iamge = itemView.findViewById(R.id.iv_image);
            itemView.setOnClickListener(this);
        }

        public void setData(List<YingyongBean.Status.SubMenuItems> list, final int position) {
            GlideUtils.loadImage(itemView.getContext(), list.get(position).getAppIcon(), iv_iamge);
            tv_name.setText(list.get(position).getAppName());
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
