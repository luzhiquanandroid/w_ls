package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.SelectPersonBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;
import com.qysd.uikit.common.util.string.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 新的好友适配器
 */

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ViewHoder> {
    private List<SelectPersonBean.Status> list = new ArrayList<>();

    public NewFriendAdapter(List<SelectPersonBean.Status> list) {
        this.list = list;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_new_friend, parent, false));
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
        private HeadImageView iv_person;
        private TextView tv_name, tv_department;
        private TextView tv_accept, tv_accept_state;

        public ViewHoder(View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_department = itemView.findViewById(R.id.tv_department);
            tv_accept = itemView.findViewById(R.id.tv_accept);
            tv_accept_state = itemView.findViewById(R.id.tv_accept_state);
            itemView.setOnClickListener(this);
        }

        public void setData(List<SelectPersonBean.Status> list, final int position) {
            iv_person.doLoadImage(list.get(position).getHeadUrl(), R.drawable.ic_lianxiren_default, HeadImageView.DEFAULT_AVATAR_THUMB_SIZE);
            tv_name.setText(list.get(position).getUserName());
            if (StringUtil.isEmpty(list.get(position).getCompName()) && StringUtil.isEmpty(list.get(position).getPosition())) {
                tv_department.setText("");
            } else {
                tv_department.setText(
                        StringUtil.isEmpty(list.get(position).getCompName()) ? "" : list.get(position).getCompName() +
                                (StringUtil.isEmpty(list.get(position).getPosition()) ? "" : ("—" + list.get(position).getPosition()))
                );
            }
            //0未接受  1已同意
            if ("0".equals(list.get(position).getReqStatus())) {
                tv_accept.setVisibility(View.VISIBLE);
                tv_accept_state.setVisibility(View.GONE);
            } else if ("1".equals(list.get(position).getReqStatus())) {
                tv_accept.setVisibility(View.GONE);
                tv_accept_state.setVisibility(View.VISIBLE);
            }
            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAcceptClickListener != null) {
                        onAcceptClickListener.onAccept(v, position);
                    }
                }
            });
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

    /**
     * 接受好友邀请点击事件
     */
    private OnAcceptClickListener onAcceptClickListener;

    public interface OnAcceptClickListener {
        void onAccept(View view, int position);
    }

    public void setOnAcceptClickListener(OnAcceptClickListener listener) {
        this.onAcceptClickListener = listener;
    }
}
