package com.qysd.lawtree.lawtreeadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreebean.ApplicationBean;
import com.qysd.lawtree.lawtreeutils.GlideUtils;

import java.util.List;

public class ApplicationViewAdapter extends RecyclerView.Adapter<ApplicationViewAdapter.ViewHolder>
		implements View.OnClickListener, View.OnLongClickListener {

	private Context context;
	private List<ApplicationBean.ApplicationList> list;
	private int i = 0;

	public ApplicationViewAdapter(Context context, List<ApplicationBean.ApplicationList> list) {
		this.context = context;
		this.list = list;
	}

	private OnRecyclerViewItemClickListener mOnItemClickListener;
	private OnRecyclerViewItemLongClickListener mOnItemLongClickListener;

	// define interface
	public static interface OnRecyclerViewItemClickListener {
		void onItemClick(View view, int position);
	}

	public static interface OnRecyclerViewItemLongClickListener {
		void onItemLongClick(View view, int position);
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		if (mOnItemLongClickListener != null) {
			// 注意这里使用getTag方法获取数据
			mOnItemLongClickListener.onItemLongClick(v, (Integer) v.getTag());
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mOnItemClickListener != null) {
			// 注意这里使用getTag方法获取数据
			mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
		}
	}

	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(
			OnRecyclerViewItemLongClickListener listener) {
		this.mOnItemLongClickListener = listener;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
		// TODO Auto-generated method stub
		GlideUtils.loadRoundCircleImage(context,list.get(position).getAppIcon(),viewHolder.iv_image);
//		for (int j = 0; j< (int) GetUserInfo.getData(context,"SCLYL",0); j++){
//			if (GetUserInfo.getData(context,"SCLY"+j,"").equals(list.get(position).getFid())){
//				viewHolder.tv_tab.setSelected(true);
//			}
//		}

			// 将数据保存在itemView的Tag中，以便点击时进行获取
		viewHolder.itemView.setTag(position);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_goodatfield, viewGroup, false);
		ViewHolder vh = new ViewHolder(view);
		// 将创建的View注册点击事件
		view.setOnClickListener(this);
		view.setOnLongClickListener(this);
		return vh;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView iv_image;

		public ViewHolder(View view) {
			super(view);
			iv_image = (ImageView) view.findViewById(R.id.iv_image);
		}
	}

}
