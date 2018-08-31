package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.MsgOrderDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYSD_AD on 2017/11/16.
 */

public class MsgOrderDetailAdapter extends RecyclerView.Adapter<MsgOrderDetailAdapter.ViewHoder> {
    private List<MsgOrderDetailBean.MsgList.OrderMaterielList> list = new ArrayList<>();//约见集合

    public MsgOrderDetailAdapter(List<MsgOrderDetailBean.MsgList.OrderMaterielList> list) {
        this.list = list;
    }

    @Override
    public MsgOrderDetailAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MsgOrderDetailAdapter.ViewHoder holder, int position) {
        holder.setData(list, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //姓名 时间  类型 状态
        private TextView tv_num, tv_type,tv_name,tv_xinghao,tv_nums;
        private ImageView iv_header;//订单图片

        public ViewHoder(View itemView) {
            super(itemView);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_xinghao = (TextView) itemView.findViewById(R.id.tv_xinghao);
            tv_nums = (TextView) itemView.findViewById(R.id.tv_nums);

            itemView.setOnClickListener(this);
        }

        public void setData(List<MsgOrderDetailBean.MsgList.OrderMaterielList> list, final int position) {
            tv_num.setText((position+1)+"");
            tv_type.setText(list.get(position).getMaterielType());
            tv_name.setText(list.get(position).getMatername());
            tv_xinghao.setText(list.get(position).getNorms());
            tv_nums.setText(list.get(position).getNum());
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
