package com.qysd.lawtree.lawtreeutils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysd.lawtree.R;
import com.qysd.lawtree.lawtreefragment.ApplicationFragment;
import com.qysd.lawtree.lawtreefragment.ApplicationFragment1;
import com.qysd.lawtree.lawtreefragment.HomeFragment;
import com.qysd.lawtree.lawtreefragment.MeFragment;
import com.qysd.lawtree.lawtreefragment.LianXirFragment;
import com.qysd.lawtree.main.fragment.SessionListFragment;
import com.qysd.uikit.business.recent.RecentContactsFragment;

import static com.qysd.lawtree.main.model.MainTab.RECENT_CONTACTS;


/**
 * Created by zhouwei on 17/4/23.
 */

public class DataGenerator {

    public static final int[] mTabRes = new int[]{
            R.drawable.tab_xiaoxi_selector,
            R.drawable.tab_lianxiren_selector,
            R.drawable.tab_yingyong_selector,
            R.drawable.tab_wode_selector};
    public static final int[] mTabResPressed = new int[]{
            R.drawable.ic_xiaoxi_pre,
            R.drawable.ic_lianxiren_pre,
            R.drawable.ic_yingyong_pre,
            R.drawable.ic_wode_pre};
    public static final String[] mTabTitle = new String[]{"消息", "联系人", "应用", "我的"};

    public static Fragment[] getFragments(String from) {
        Fragment fragments[] = new Fragment[4];
        SessionListFragment sessionListFragment = new SessionListFragment();
        sessionListFragment.attachTabData(RECENT_CONTACTS);
        //fragments[0] = sessionListFragment;
        fragments[0] = HomeFragment.newInstance(from);
        fragments[1] = LianXirFragment.newInstance(from);
        fragments[2] = ApplicationFragment.newInstance(from);
        fragments[3] = MeFragment.newInstance(from);
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     *
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content, null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
