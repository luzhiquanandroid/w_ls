package com.qysd.lawtree.lawtreeadapter;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeactivity.MarketingManagementActivity;
import com.qysd.lawtree.lawtreeactivity.OrderActivity;
import com.qysd.lawtree.lawtreeactivity.RepertoryActivity;
import com.qysd.lawtree.lawtreeactivity.ShenChanDateActivity;
import com.qysd.lawtree.lawtreeactivity.ShenChanPlanActivity;
import com.qysd.lawtree.lawtreeactivity.TaskManagerActivity;
import com.qysd.lawtree.lawtreebean.YingyongBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的企业适配器
 */

public class YinyongAdapter extends RecyclerView.Adapter {
    // 两种类型
    /**
     * 第一种HEAD 显示部门列表
     */
    public static final int HEAD = 0;
    /**
     * 第二种BOTTOM 显示人员列表
     */
    public static final int BOTTOM = 1;
    private List<YingyongBean.Status> list = new ArrayList<>();

    public YinyongAdapter(List<YingyongBean.Status> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new HeadViewHoder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_yingyong_tv, parent, false
            ));
        } else if (viewType == BOTTOM) {
            return new BottomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_yingyong_item, parent, false));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEAD) {
            HeadViewHoder headViewHoder = (HeadViewHoder) holder;
            headViewHoder.setData(list, position);
        } else if (getItemViewType(position) == BOTTOM) {
            BottomViewHolder bottomViewHolder = (BottomViewHolder) holder;
            bottomViewHolder.setData(list, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //0 1
        //2 3
        //4 5
        if ((position + 1) % 2 == 1) {
            return HEAD;
        } else {
            return BOTTOM;
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + list.size();
    }

    public class HeadViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_type_title;


        public HeadViewHoder(final View itemView) {
            super(itemView);
            tv_type_title = itemView.findViewById(R.id.tv_type_title);
            itemView.setOnClickListener(this);
        }

        public void setData(List<YingyongBean.Status> list, int position) {
            //0 2 4
            tv_type_title.setText(list.get(position / 2).getAppName());
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
        private RecyclerView recyclerview;

        public BottomViewHolder(View itemView) {
            super(itemView);
            recyclerview = itemView.findViewById(R.id.recyclerview);
            itemView.setOnClickListener(this);
        }

        public void setData(final List<YingyongBean.Status> list, final int fposition) {
            //1 3 5
            manager = new GridLayoutManager(itemView.getContext(), 4);
            recyclerview.setLayoutManager(manager);
            myQiyeAdapter = new YingYongItemAdapter(list.get((fposition - 1) / 2).getSubMenuItems());
            recyclerview.setAdapter(myQiyeAdapter);
            myQiyeAdapter.setOnItemClickListener(new YingYongItemAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Toast.makeText(itemView.getContext(), "" + list.get(( fposition - 1) / 2).getSubMenuItems().get(position).getAppName(), Toast.LENGTH_SHORT).show();
                    switch (list.get((fposition - 1) / 2).getSubMenuItems().get(position).getAppCode()) {
                        case "A001-01":
                            itemView.getContext().startActivity(new Intent(itemView.getContext(), ShenChanPlanActivity.class));
                            break;
                        case "A001-02":
                            itemView.getContext().startActivity(new Intent(itemView.getContext(), TaskManagerActivity.class));
                            break;
                        case "A001-03":
                            itemView.getContext().startActivity(new Intent(itemView.getContext(), ShenChanDateActivity.class));
                            break;
                        case "A002-01":
                            itemView.getContext().startActivity(new Intent(itemView.getContext(), RepertoryActivity.class));
                            break;
                        case "A003-01":
                            itemView.getContext().startActivity(new Intent(itemView.getContext(), OrderActivity.class));
                            break;
                        case "A003-02":
                            itemView.getContext().startActivity(new Intent(itemView.getContext(), MarketingManagementActivity.class));
                            break;
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
//            if (mOnItemClickListener != null) {
//                mOnItemClickListener.onItemClick(view, getAdapterPosition() - departmentInfoExtList.size()
//                        , "BOTTOM");
//            }
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

    private GridLayoutManager manager;
    private YingYongItemAdapter myQiyeAdapter;


}
