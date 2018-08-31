package com.qysd.lawtree.lawtreeadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.ApplicationBean;
import com.qysd.lawtree.lawtreebean.RepertoryListBean;
import com.qysd.lawtree.lawtreeutils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2017/11/16.
 */

public class ItemApplicationAdapter extends RecyclerView.Adapter<ItemApplicationAdapter.ViewHoder> {
    private Context context;
    private List<ApplicationBean.ApplicationList> list = new ArrayList<>();//约见集合

    public ItemApplicationAdapter(List<ApplicationBean.ApplicationList> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ItemApplicationAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_application_grid_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemApplicationAdapter.ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //姓名 时间  类型 状态
        private TextView tv_name, tv_size,tv_num;
        private ImageView iv_image;//订单图片

        public ViewHoder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
//            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
//            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
//            tv_num = (TextView) itemView.findViewById(R.id.tv_num);

            itemView.setOnClickListener(this);
        }

        public void setData(List<ApplicationBean.ApplicationList> list, final int position) {
//            tv_name.setText(list.get(position).getCreateByName());
//            tv_size.setText(list.get(position).getCreatetimes());
//            tv_num.setText(list.get(position).getUnitName());
//            tv_end_time.setText("结束时间 "+list.get(position).getGdtime());
            GlideUtils.loadRoundCircleImage(context,list.get(position).getAppIcon(),iv_image);
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
