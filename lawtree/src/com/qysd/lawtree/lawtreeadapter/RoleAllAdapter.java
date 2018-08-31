package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.RoleBean;
import com.qysd.lawtree.lawtreebean.WaiBuQiyeBean;
import com.qysd.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 角色适配器
 */

public class RoleAllAdapter extends RecyclerView.Adapter<RoleAllAdapter.ViewHoder> {
    private List<RoleBean.Status> list = new ArrayList<>();
    public static HashMap<String, Boolean> isSelected;
    public static List<RoleBean.Status> selectedRoleBeanList;
    public List<String> roleId = new ArrayList<>();

    public RoleAllAdapter(List<RoleBean.Status> list, List<String> roleId) {
        this.list = list;
        this.roleId = roleId;

        selectedRoleBeanList = new ArrayList<>();
        isSelected = new HashMap<String, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            RoleBean.Status a = (RoleBean.Status) list.get(i);
            isSelected.put(a.getRoleId(), false);
        }
        if (roleId.size() > 0) {
            for (int i = 0; i < roleId.size(); i++) {
                isSelected.put(roleId.get(i), true);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < roleId.size(); j++) {
                if (roleId.get(j).equals(list.get(i).getRoleId())) {
                    selectedRoleBeanList.add(list.get(i));
                }
            }
        }
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_role_select, parent, false));
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
        private TextView tv_role;
        private CheckBox checkbox;

        public ViewHoder(View itemView) {
            super(itemView);
            tv_role = itemView.findViewById(R.id.tv_role);
            checkbox = itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
        }

        public void setData(List<RoleBean.Status> list, final int position) {
            tv_role.setText(list.get(position).getRoleName());
            checkbox.setChecked(isSelected.get(list.get(position).getRoleId()));
        }

        @Override
        public void onClick(View view) {
            checkbox.setChecked(!checkbox.isChecked());
            isSelected.put(list.get(getAdapterPosition()).getRoleId(), checkbox.isChecked());
            if (isSelected.get(list.get(getAdapterPosition()).getRoleId())) {
                selectedRoleBeanList.add(list.get(getAdapterPosition()));
            } else {
                for (int i = 0; i < selectedRoleBeanList.size(); i++) {
                    if (list.get(getAdapterPosition()).getRoleId().equals(selectedRoleBeanList.get(i).getRoleId())) {
                        selectedRoleBeanList.remove(i);
                    }
                }
            }

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
