package com.qysd.lawtree.login;

import com.qysd.lawtree.DemoCache;
import com.qysd.lawtree.redpacket.NIMRedPacketClient;
import com.qysd.uikit.common.ui.drop.DropManager;
import com.qysd.uikit.api.NimUIKit;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        DemoCache.clear();
        DropManager.getInstance().destroy();
        NIMRedPacketClient.clear();
    }
}
