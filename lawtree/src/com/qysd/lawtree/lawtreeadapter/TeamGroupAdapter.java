package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.team.model.Team;
import com.qysd.lawtree.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 群组适配器
 */

public class TeamGroupAdapter extends RecyclerView.Adapter<TeamGroupAdapter.ViewHoder> {
    private List<Team> list = new ArrayList<>();

    public TeamGroupAdapter(List<Team> list) {
        this.list = list;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team_group, parent, false));
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
        private ImageView ic_team;
        private TextView tv_team_name, tv_team_number;

        public ViewHoder(View itemView) {
            super(itemView);
            ic_team = itemView.findViewById(R.id.ic_team);
            tv_team_name = itemView.findViewById(R.id.tv_team_name);
            tv_team_number = itemView.findViewById(R.id.tv_team_number);
            itemView.setOnClickListener(this);
        }

        public void setData(List<Team> list, final int position) {
            tv_team_name.setText(list.get(position).getName());
            tv_team_number.setText(list.get(position).getMemberCount() + "人");
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
