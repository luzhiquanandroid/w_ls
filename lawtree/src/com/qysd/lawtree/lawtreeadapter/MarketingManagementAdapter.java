package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.MarketingManagementBean;
import com.qysd.lawtree.lawtreebean.RepertoryListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2017/11/16.
 */

public class MarketingManagementAdapter extends RecyclerView.Adapter<MarketingManagementAdapter.ViewHoder> {
    private List<MarketingManagementBean.Status> list = new ArrayList<>();//约见集合

    public MarketingManagementAdapter(List<MarketingManagementBean.Status> list) {
        this.list = list;
    }

    @Override
    public MarketingManagementAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_marketingmanagerment, parent, false));
    }

    @Override
    public void onBindViewHolder(MarketingManagementAdapter.ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //姓名 时间  类型 状态
        private TextView tv_name, tv_time,tv_num;
        private ImageView iv_header;//订单图片

        public ViewHoder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);

            itemView.setOnClickListener(this);
        }

        public void setData(List<MarketingManagementBean.Status> list, final int position) {
            tv_name.setText("客户： "+list.get(position).getCompName());
            tv_num.setText("订单编号： "+list.get(position).getOrdercodenick());
            tv_time.setText(list.get(position).getPerformdateStr());
//            tv_end_time.setText("结束时间 "+list.get(position).getGdtime());
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
