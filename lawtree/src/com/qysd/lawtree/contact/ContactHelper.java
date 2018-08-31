package com.qysd.lawtree.contact;

import android.content.Context;
import android.content.Intent;

import com.qysd.lawtree.contact.activity.UserProfileActivity;
import com.qysd.lawtree.lawtreeactivity.LianXiRenDetailActivity;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.api.model.contact.ContactEventListener;

/**
 * UIKit联系人列表定制展示类
 * <p/>
 * Created by huangjun on 2015/9/11.
 */
public class ContactHelper {

    public static void init() {
        setContactEventListener();
    }

    private static void setContactEventListener() {
        NimUIKit.setContactEventListener(new ContactEventListener() {
            @Override
            public void onItemClick(Context context, String account) {
                //UserProfileActivity.start(context, account);
                Intent intent = new Intent(context, LianXiRenDetailActivity.class);
                intent.putExtra("type", "typeId");
                intent.putExtra("account", account);
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(Context context, String account) {

            }

            @Override
            public void onAvatarClick(Context context, String account) {
                //UserProfileActivity.start(context, account);
                Intent intent = new Intent(context, LianXiRenDetailActivity.class);
                intent.putExtra("type", "typeId");
                intent.putExtra("account", account);
                context.startActivity(intent);
            }
        });
    }

}
