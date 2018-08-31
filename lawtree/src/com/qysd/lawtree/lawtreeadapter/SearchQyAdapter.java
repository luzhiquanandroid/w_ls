package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.MyQiyeBean;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的企业适配器
 */

public class SearchQyAdapter extends RecyclerView.Adapter {
    // 两种类型
    /**
     * 第一种HEAD 显示企业
     */
    public static final int HEAD = 0;
    /**
     * 第二种BOTTOM 显示人员
     */
    public static final int BOTTOM = 1;
    private List<WaiBuQiyeBean.Status> qyList = new ArrayList<>();
    private List<MyQiyeQunLiaoBean.Status> lxrList = new ArrayList<>();

    public SearchQyAdapter(List<WaiBuQiyeBean.Status> qyList,
                           List<MyQiyeQunLiaoBean.Status> lxrList) {
        this.qyList = qyList;
        this.lxrList = lxrList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new HeadViewHoder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_waibu_qiye, parent, false
            ));
        } else if (viewType == BOTTOM) {
            return new BottomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lianxiren, parent, false));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEAD) {
            HeadViewHoder headViewHoder = (HeadViewHoder) holder;
            headViewHoder.setData(qyList, position);
        } else if (getItemViewType(position) == BOTTOM) {
            BottomViewHolder bottomViewHolder = (BottomViewHolder) holder;
            bottomViewHolder.setData(lxrList, position - qyList.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= qyList.size() - 1) {
            return HEAD;
        } else {
            return BOTTOM;
        }
    }

    @Override
    public int getItemCount() {
        return qyList.size() + lxrList.size();
    }

    public class HeadViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_department;
        private HeadImageView iv_icon_waibu;//公司logo
        private TextView tv_comp_name;//公司名
        private TextView tv_gys, tv_kh, tv_wx;//供应商 客户 外协
        private TextView tv_name_tel;//业务联系人

        public HeadViewHoder(final View itemView) {
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
            iv_icon_waibu.doLoadImage(list.get(position).getLogoUrl(), R.drawable.ic_comp_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
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
                mOnItemClickListener.onItemClick(view, getAdapterPosition(), "HEAD");
            }
        }
    }

    /**
     * BOTTOM ViewHoder
     */
    public class BottomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_name, tv_department;
        private HeadImageView iv_person;

        public BottomViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_department = itemView.findViewById(R.id.tv_department);
            iv_person = itemView.findViewById(R.id.iv_person);
            itemView.setOnClickListener(this);
        }

        public void setData(final List<MyQiyeQunLiaoBean.Status> list, final int position) {
            tv_name.setText(list.get(position).getUserName());
            tv_department.setText(list.get(position).getUserName());
            iv_person.doLoadImage(list.get(position).getHeadUrl(), R.drawable.ic_lianxiren_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition() - lxrList.size()
                        , "BOTTOM");
            }
        }
    }

    /**
     * item的点击事件
     */
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position, String type);

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
