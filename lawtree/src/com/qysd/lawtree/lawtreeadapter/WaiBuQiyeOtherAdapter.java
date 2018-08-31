package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeOtherBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;


/**
 */

public class WaiBuQiyeOtherAdapter extends RecyclerView.Adapter<WaiBuQiyeOtherAdapter.ViewHoder> {
    private List<WaiBuQiyeOtherBean.Status> list = new ArrayList<>();
    private String type;

    public WaiBuQiyeOtherAdapter(List<WaiBuQiyeOtherBean.Status> list) {
        this.list = list;
    }

    public WaiBuQiyeOtherAdapter(List<WaiBuQiyeOtherBean.Status> list, String type) {
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_other_lianxiren, parent, false));
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
        private TextView tv_wbqy_other_tel, tv_wbqy_other_name, tv_wbqy_other_position;//公司名

        public ViewHoder(View itemView) {
            super(itemView);
            tv_wbqy_other_tel = itemView.findViewById(R.id.tv_wbqy_other_tel);
            tv_wbqy_other_name = itemView.findViewById(R.id.tv_wbqy_other_name);
            tv_wbqy_other_position = itemView.findViewById(R.id.tv_wbqy_other_position);
            itemView.setOnClickListener(this);
        }

        public void setData(List<WaiBuQiyeOtherBean.Status> list, final int position) {
            tv_wbqy_other_tel.setText(list.get(position).getMobilenum());
            tv_wbqy_other_name.setText(list.get(position).getName());
            tv_wbqy_other_position.setText(list.get(position).getPosition());
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
