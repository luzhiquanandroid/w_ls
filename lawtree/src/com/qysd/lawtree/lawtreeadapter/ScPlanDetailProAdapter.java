package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.ShenChanPlanDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产计划详情进度
 */
public class ScPlanDetailProAdapter extends RecyclerView.Adapter<ScPlanDetailProAdapter.ViewHoder> {
    private List<ShenChanPlanDetailBean.Status.ProductionPlanList> list = new ArrayList<>();

    public ScPlanDetailProAdapter(List<ShenChanPlanDetailBean.Status.ProductionPlanList> list) {
        this.list = list;
    }

    @Override
    public ScPlanDetailProAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_pro, parent, false));
    }

    @Override
    public void onBindViewHolder(ScPlanDetailProAdapter.ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private TextView tv_procedureName;
        private View view_show;
        private ImageView iv_icon;

        public ViewHoder(View itemView) {
            super(itemView);
            view_show = itemView.findViewById(R.id.view_show);
            tvName = itemView.findViewById(R.id.tv_name);
            tv_procedureName = itemView.findViewById(R.id.tv_procedureName);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            itemView.setOnClickListener(this);
        }

        public void setData(List<ShenChanPlanDetailBean.Status.ProductionPlanList> list, final int position) {
            if (position == list.size() - 1) {
                view_show.setVisibility(View.GONE);
            } else {
                view_show.setVisibility(View.VISIBLE);
            }
            tv_procedureName.setText(list.get(position).getProcedureName());
            tvName.setText(list.get(position).getWorkerName());
            if ("1".equals(list.get(position).getState())) {
                iv_icon.setImageResource(R.drawable.ic_pro_complet_ing);
            } else if ("2".equals(list.get(position).getState())) {
                iv_icon.setImageResource(R.drawable.ic_pro_complet);
            }
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
