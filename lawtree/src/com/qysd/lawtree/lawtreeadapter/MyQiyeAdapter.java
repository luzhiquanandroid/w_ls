package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.MyQiyeBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;

import static com.qysd.uikit.common.ui.imageview.HeadImageView.DEFAULT_AVATAR_THUMB_SIZE;


/**
 * 我的企业适配器
 */

public class MyQiyeAdapter extends RecyclerView.Adapter {
    // 两种类型
    /**
     * 第一种HEAD 显示部门列表
     */
    public static final int HEAD = 0;
    /**
     * 第二种BOTTOM 显示人员列表
     */
    public static final int BOTTOM = 1;
    private List<MyQiyeBean.DepartmentInfoExt> departmentInfoExtList = new ArrayList<>();
    private List<MyQiyeBean.DepartInfoAndUserInfo> departInfoAndUserInfoList = new ArrayList<>();

    public MyQiyeAdapter(List<MyQiyeBean.DepartmentInfoExt> departmentInfoExtList,
                         List<MyQiyeBean.DepartInfoAndUserInfo> departInfoAndUserInfoList) {
        this.departmentInfoExtList = departmentInfoExtList;
        this.departInfoAndUserInfoList = departInfoAndUserInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new HeadViewHoder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_my_qiye_department, parent, false
            ));
        } else if (viewType == BOTTOM) {
            return new BottomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_qiye_lianxiren, parent, false));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEAD) {
            HeadViewHoder headViewHoder = (HeadViewHoder) holder;
            headViewHoder.setData(departmentInfoExtList, position);
        } else if (getItemViewType(position) == BOTTOM) {
            BottomViewHolder bottomViewHolder = (BottomViewHolder) holder;
            bottomViewHolder.setData(departInfoAndUserInfoList, position - departmentInfoExtList.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= departmentInfoExtList.size() - 1) {
            return HEAD;
        } else {
            return BOTTOM;
        }
    }

    @Override
    public int getItemCount() {
        return departmentInfoExtList.size() + departInfoAndUserInfoList.size();
    }

    public class HeadViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_department;


        public HeadViewHoder(final View itemView) {
            super(itemView);
            tv_department = itemView.findViewById(R.id.tv_department);
            itemView.setOnClickListener(this);
        }

        public void setData(List<MyQiyeBean.DepartmentInfoExt> list, int position) {
            tv_department.setText(list.get(position).getDeptname() + "(" + list.get(position).getUserNum()
                    + ")");
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

        public void setData(final List<MyQiyeBean.DepartInfoAndUserInfo> list, final int position) {
            tv_name.setText(list.get(position).getUserName());
            tv_department.setText(list.get(position).getUserName());
            iv_person.doLoadImage(list.get(position).getHeadUrl(), R.drawable.ic_lianxiren_default, DEFAULT_AVATAR_THUMB_SIZE);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition() - departmentInfoExtList.size()
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
