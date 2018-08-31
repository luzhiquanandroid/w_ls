package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeactivity.QunLiaoMyQiyeActivity;
import com.qysd.lawtree.lawtreeactivity.QunLiaoWaibuQiyeActivity;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeOtherBean;
import com.qysd.lawtree.lawtreebusbean.QunLiaoEventBusBean;

import java.util.ArrayList;
import java.util.List;


/**
 */

public class SearchLxrAdapter extends RecyclerView.Adapter<SearchLxrAdapter.ViewHoder> {
    private List<MyQiyeQunLiaoBean.Status> list = new ArrayList<>();

    public SearchLxrAdapter(List<MyQiyeQunLiaoBean.Status> list) {
        this.list = list;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lianxiren, parent, false));
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
        private TextView tv_department;
        private TextView tv_name;

        public ViewHoder(View itemView) {
            super(itemView);
            tv_department = itemView.findViewById(R.id.tv_department);
            tv_name = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(this);
        }

        public void setData(List<MyQiyeQunLiaoBean.Status> list, final int position) {
            tv_department.setText(list.get(position).getPosition());
            tv_name.setText(list.get(position).getUserName());
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
