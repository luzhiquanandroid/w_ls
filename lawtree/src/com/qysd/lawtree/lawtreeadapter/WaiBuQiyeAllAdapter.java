package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 联系人外部企业适配器
 */

public class WaiBuQiyeAllAdapter extends RecyclerView.Adapter<WaiBuQiyeAllAdapter.ViewHoder> {
    private List<WaiBuQiyeBean.Status> list = new ArrayList<>();

    public WaiBuQiyeAllAdapter(List<WaiBuQiyeBean.Status> list) {
        this.list = list;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_waibu_qiye, parent, false));
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
        private HeadImageView iv_icon_waibu;//公司logo
        private TextView tv_comp_name;//公司名
        private TextView tv_gys, tv_kh, tv_wx;//供应商 客户 外协
        private TextView tv_name_tel;//业务联系人

        public ViewHoder(View itemView) {
            super(itemView);
            iv_icon_waibu = itemView.findViewById(R.id.iv_icon_waibu);
            tv_comp_name = itemView.findViewById(R.id.tv_comp_name);
            tv_gys = itemView.findViewById(R.id.tv_gys);
            tv_kh = itemView.findViewById(R.id.tv_kh);
            tv_wx = itemView.findViewById(R.id.tv_wx);
            tv_name_tel = itemView.findViewById(R.id.tv_name_tel);
            itemView.setOnClickListener(this);
        }

        public void setData(List<WaiBuQiyeBean.Status> list, final int position) {
            iv_icon_waibu.loadAvatar(list.get(position).getLogoUrl());
            tv_comp_name.setText(list.get(position).getCompName());
            tv_name_tel.setText(list.get(position).getUserName() + "(" + list.get(position).getMobileNum() + ")");
            switch (list.get(position).getProperty()) {
                case "1":
                    tv_kh.setVisibility(View.VISIBLE);
                    tv_gys.setVisibility(View.GONE);
                    break;
                case "2":
                    tv_kh.setVisibility(View.GONE);
                    tv_gys.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    tv_kh.setVisibility(View.VISIBLE);
                    tv_gys.setVisibility(View.VISIBLE);
                    break;
                default:
                    tv_kh.setVisibility(View.GONE);
                    tv_gys.setVisibility(View.GONE);
                    break;
            }
            if ("1".equals(list.get(position).getIfHelp())) {
                tv_wx.setVisibility(View.VISIBLE);
            } else {
                tv_wx.setVisibility(View.GONE);
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
