package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.RepertoryListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2017/11/16.
 */

public class RepertoryListAdapter extends RecyclerView.Adapter<RepertoryListAdapter.ViewHoder> {
    private List<RepertoryListBean.LeaveList> list = new ArrayList<>();//约见集合

    public RepertoryListAdapter(List<RepertoryListBean.LeaveList> list) {
        this.list = list;
    }

    @Override
    public RepertoryListAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repertorylist, parent, false));
    }

    @Override
    public void onBindViewHolder(RepertoryListAdapter.ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //姓名 时间  类型 状态
        private TextView tv_name, tv_size,tv_num;
        private ImageView iv_header;//订单图片

        public ViewHoder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);

            itemView.setOnClickListener(this);
        }

        public void setData(List<RepertoryListBean.LeaveList> list, final int position) {
            tv_name.setText(list.get(position).getMaterName());
            tv_num.setText(list.get(position).getNumber()+list.get(position).getUinitName());
            tv_size.setText(list.get(position).getNorms());
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
