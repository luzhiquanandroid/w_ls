package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.ShenChanPlanBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产计划已完成和待完成适配器
 */
public class ScPlanAdapter extends RecyclerView.Adapter<ScPlanAdapter.ViewHoder> {
    private List<ShenChanPlanBean.Status> list = new ArrayList<>();

    public ScPlanAdapter(List<ShenChanPlanBean.Status> list) {
        this.list = list;
    }

    @Override
    public ScPlanAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scplan_order, parent, false));
    }

    @Override
    public void onBindViewHolder(ScPlanAdapter.ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_order, tv_order_materName, tv_order_allnum;

        public ViewHoder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_order = itemView.findViewById(R.id.tv_order);
            tv_order_materName = itemView.findViewById(R.id.tv_order_materName);
            tv_order_allnum = itemView.findViewById(R.id.tv_order_allnum);
        }

        public void setData(List<ShenChanPlanBean.Status> list, final int position) {
            tv_order.setText("订单编号:" + list.get(position).getOrderCodeNick());
            tv_order_materName.setText(list.get(position).getMaterName());
            tv_order_allnum.setText(list.get(position).getPlannum() + list.get(position).getDicName());
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
