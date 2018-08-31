package com.qysd.lawtree.lawtreeadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.ApplicationBean;
import com.qysd.lawtree.lawtreebean.MyQiyeBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;

import static com.qysd.uikit.common.ui.imageview.HeadImageView.DEFAULT_AVATAR_THUMB_SIZE;


/**
 * 我的企业适配器
 */

public class ApplicationAdapter extends RecyclerView.Adapter {
    // 两种类型
    /**
     * 第一种HEAD 显示部门列表
     */
    public static final int FIRST = 0;
    /**
     * 第二种BOTTOM 显示人员列表
     */
    public static final int SECOND = 1;

    private Context contex;
    private List<ApplicationBean.ApplicationDataInfo> applicationDataInfos = new ArrayList<>();

    public ApplicationAdapter(List<ApplicationBean.ApplicationDataInfo> applicationDataInfos,Context contex) {
        this.applicationDataInfos = applicationDataInfos;
        this.contex = contex;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FIRST) {
            return new FirstViewHoder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_application_text, parent, false
            ));
        } else if (viewType == SECOND) {
            return new SecondViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_application_recyclerview, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == FIRST) {
            FirstViewHoder firstViewHoder = (FirstViewHoder) holder;
            firstViewHoder.setData(applicationDataInfos,position);
        } else if (getItemViewType(position) == SECOND) {
            SecondViewHolder secondViewHolder = (SecondViewHolder) holder;
            secondViewHolder.setData(applicationDataInfos.get(position/2).getSubMenuItems(),position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("position",position+"hhhhhhhh");
        //012345   0 2 4
        if (position % 2 == 0) {
            return FIRST;
        } else {
            return SECOND;
        }
    }

    @Override
    public int getItemCount() {
        return applicationDataInfos.size()+applicationDataInfos.size() ;
    }

    public class FirstViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title;


        public FirstViewHoder(final View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        public void setData(List<ApplicationBean.ApplicationDataInfo> list, int position) {
            tv_title.setText(list.get(position/2).getAppName());
        }

        @Override
        public void onClick(View view) {

        }
    }

    /**
     * BOTTOM ViewHoder
     */
    public class SecondViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title, tv_department;
        private HeadImageView iv_person;
        private RecyclerView recyclerView;


        public SecondViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerview);
//            tv_name = itemView.findViewById(R.id.tv_name);
//            tv_department = itemView.findViewById(R.id.tv_department);
//            iv_person = itemView.findViewById(R.id.iv_person);
            itemView.setOnClickListener(this);
        }

        private GridLayoutManager manager;
        private ItemApplicationAdapter itemApplicationAdapter;
        public void setData(final List<ApplicationBean.ApplicationList> list, final int position) {
           //tv_title.setText(list.get(position).getAppName());
            manager = new GridLayoutManager(contex,4);
            recyclerView.setLayoutManager(manager);
            itemApplicationAdapter = new ItemApplicationAdapter(list,contex);
            recyclerView.setAdapter(itemApplicationAdapter);

        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition()
                        , "SECOND");
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
