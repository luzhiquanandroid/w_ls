package com.qysd.lawtree.lawtreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.GxBean;
import com.qysd.lawtree.lawtreebean.RoleBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 工序适配器
 */

public class GxAllAdapter extends RecyclerView.Adapter<GxAllAdapter.ViewHoder> {
    private List<GxBean.Status> list = new ArrayList<>();
    public static HashMap<String, Boolean> isSelected;
    public static List<GxBean.Status> selectedGxBeanList;
    public List<String> gxId = new ArrayList<>();

    public GxAllAdapter(List<GxBean.Status> list, List<String> gxId) {
        this.list = list;
        this.gxId = gxId;

        selectedGxBeanList = new ArrayList<>();
        isSelected = new HashMap<String, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            GxBean.Status a = (GxBean.Status) list.get(i);
            isSelected.put(a.getProcedureid(), false);
        }
        if (gxId.size() > 0) {
            for (int i = 0; i < gxId.size(); i++) {
                isSelected.put(gxId.get(i), true);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < gxId.size(); j++) {
                if (gxId.get(j).equals(list.get(i).getProcedureid())) {
                    selectedGxBeanList.add(list.get(i));
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

        public void setData(List<GxBean.Status> list, final int position) {
            tv_role.setText(list.get(position).getProcedurename());
            checkbox.setChecked(isSelected.get(list.get(position).getProcedureid()));
        }

        @Override
        public void onClick(View view) {
            checkbox.setChecked(!checkbox.isChecked());
            isSelected.put(list.get(getAdapterPosition()).getProcedureid(), checkbox.isChecked());
            if (isSelected.get(list.get(getAdapterPosition()).getProcedureid())) {
                selectedGxBeanList.add(list.get(getAdapterPosition()));
            } else {
                for (int i = 0; i < selectedGxBeanList.size(); i++) {
                    if (list.get(getAdapterPosition()).getProcedureid().equals(selectedGxBeanList.get(i).getProcedureid())) {
                        selectedGxBeanList.remove(i);
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
