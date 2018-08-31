package com.qysd.lawtree.lawtreeactivity;

import android.app.Dialog;
import android.app.Service;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.ArraySet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreeadapter.ScDateAdapter;
import com.qysd.lawtree.lawtreebase.BaseActivity;
import com.qysd.lawtree.lawtreebean.ShenChanPaiDanBean;
import com.qysd.lawtree.lawtreeutil.httputils.UserCallback;
import com.qysd.lawtree.lawtreeutils.Constants;
import com.qysd.lawtree.lawtreeutils.GetUserInfo;
import com.qysd.lawtree.lawtreeutils.OnRecyclerItemClickListener;
import com.qysd.lawtree.lawtreeview.wight.VpSwipeRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 生产排单
 */
public class ShenChanDateActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager manager;
    private ScDateAdapter scDateAdapter;
    private ShenChanPaiDanBean shenChanPaiDanBean;
    private Gson gson = new Gson();
    private TextView tv_title_right;//右侧保存按钮
    private List<String> allList = new ArrayList<>();
    private List<ShenChanPaiDanBean.Status> list = new ArrayList<>();
    private VpSwipeRefreshLayout swipe_refresh;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_shen_chan_date);
    }

    @Override
    protected void initView() {
        initTitle(R.drawable.ic_left_jt, "生产排单", "保存");
        tv_title_right = findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        swipe_refresh = findViewById(R.id.swipe_refresh);
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    protected void bindListener() {
        swipe_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        loadData();
    }

    /**
     * 请求网络数据
     */
    private void loadData() {
        swipe_refresh.setRefreshing(true);
        OkHttpUtils.get().url(Constants.baseUrl + "productionPlan/productionSortBillList")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("scpd", response.toString());
                        swipe_refresh.setRefreshing(false);
                        if (response.toString().contains("没有数据")) {
                            return;
                        }
                        list.clear();
                        allList.clear();
                        shenChanPaiDanBean = gson.fromJson(response.toString(), ShenChanPaiDanBean.class);
                        if ("1".equals(shenChanPaiDanBean.getCode())) {
                            list = shenChanPaiDanBean.getStatus();
                            setAdapter(list);
                        }
                    }
                });
    }


    @Override
    protected void initNav() {

    }

    @Override
    public void onClick(View v) {

    }

    private ItemTouchHelper mItemTouchHelper;

    private void setAdapter(final List<ShenChanPaiDanBean.Status> list) {
        manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        scDateAdapter = new ScDateAdapter(list);
        mRecyclerView.setAdapter(scDateAdapter);
        //条目点击接口
        scDateAdapter.setOnItemClickListener(new ScDateAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
            }
        });

        scDateAdapter.setOnAcceptClickListener(new ScDateAdapter.OnAcceptClickListener() {
            @Override
            public void onAccept(View view, int position, ScDateAdapter.ViewHoder vh) {
                swipe_refresh.setEnabled(false);
                tv_title_right.setVisibility(View.VISIBLE);
                //判断被拖拽的是否是前两个，如果不是则执行拖拽
                //if (vh.getLayoutPosition() != 0) {
                mItemTouchHelper.startDrag(vh);
                //获取系统震动服务
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                vib.vibrate(70);
                //scDateAdapter.notifyDataSetChanged();
                //}
            }
        });
        //置顶操作
        scDateAdapter.setOnUpClickListener(new ScDateAdapter.OnUpClickListener() {
            @Override
            public void onUp(View view, int position, ScDateAdapter.ViewHoder viewHoder) {
                tv_title_right.setVisibility(View.VISIBLE);
                //当前置顶的集合
                ShenChanPaiDanBean.Status status = list.get(position);
                list.remove(position);
                list.add(0, status);
                scDateAdapter.notifyDataSetChanged();
            }
        });
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(list, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(list, i, i - 1);
                    }
                }
                scDateAdapter.notifyItemMoved(fromPosition, toPosition);
//                if (toPosition == 0) {
//                    scDateAdapter.notifyDataSetChanged();
//                }
                Log.e("fromPosition", fromPosition + "---toPosition" + toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                return super.canDropOver(recyclerView, current, target);
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                //mSaveDialog(viewHolder);
                swipe_refresh.setEnabled(true);
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
                scDateAdapter.notifyDataSetChanged();
            }
        });

        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onClickTitleRight(View v) {
        super.onClickTitleRight(v);
        saveData();
        Toast.makeText(this, "保存数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onClickTitleLeft(View v) {
        //super.onClickTitleLeft(v);
        //如果保存显示
        if (tv_title_right.getVisibility() == View.VISIBLE) {
            mSaveDialog();
        } else {
            finish();
        }
    }

    private void saveData() {
        String allPid = "";
        for (int i = 0; i < list.size(); i++) {
            allList.add(list.get(i).getPlanId());
        }
        for (int i = 0; i < allList.size(); i++) {
            if (i == allList.size() - 1) {
                allPid = allPid + allList.get(i);
            } else {
                allPid = allPid + allList.get(i) + ",";
            }

        }
        OkHttpUtils.post().url(Constants.baseUrl + "productionPlan/saveProductionSortBill")
                .addParams("userId", (String) GetUserInfo.getData(this, "userId", ""))
                .addParams("params", allPid)
                .build()
                .execute(new UserCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("svaescpd", response.toString());
                        if (response.contains("保存失败")) {
                            Toast.makeText(ShenChanDateActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ShenChanDateActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            tv_title_right.setVisibility(View.INVISIBLE);
                            list.clear();
                            allList.clear();
                            shenChanPaiDanBean = gson.fromJson(response.toString(), ShenChanPaiDanBean.class);
                            if ("1".equals(shenChanPaiDanBean.getCode())) {
                                list = shenChanPaiDanBean.getStatus();
                                setAdapter(list);
                            }
                        }
                    }
                });

    }

    @Override
    public void onRefresh() {
        tv_title_right.setVisibility(View.INVISIBLE);
        loadData();
    }

    /**
     * 弹出是否保存的对话框
     */
    private Dialog mSaveDialog = null;

    public void mSaveDialog() {
        mSaveDialog = new Dialog(this, R.style.AlertDialogStyle);
        View view = View.inflate(this, R.layout.dialog_paidan_save, null);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveDialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaveDialog.dismiss();
                tv_title_right.setVisibility(View.INVISIBLE);
                loadData();
            }
        });
        mSaveDialog.setContentView(view);
        mSaveDialog.setCanceledOnTouchOutside(true);
        mSaveDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mSaveDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mSaveDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (tv_title_right.getVisibility() == View.VISIBLE) {
                mSaveDialog();
            } else {
                finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
