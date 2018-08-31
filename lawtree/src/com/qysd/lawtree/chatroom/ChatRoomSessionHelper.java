package com.qysd.lawtree.chatroom;

import com.qysd.lawtree.chatroom.viewholder.ChatRoomMsgViewHolderGuess;
import com.qysd.lawtree.session.action.GuessAction;
import com.qysd.lawtree.session.extension.GuessAttachment;
import com.qysd.uikit.business.session.actions.BaseAction;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.api.model.chatroom.ChatRoomSessionCustomization;

import java.util.ArrayList;

/**
 * UIKit自定义聊天室消息界面用法展示类
 * <p>
 * Created by huangjun on 2017/9/18.
 */

public class ChatRoomSessionHelper {
    private static ChatRoomSessionCustomization customization;

    public static void init() {
        registerViewHolders();
        NimUIKit.setCommonChatRoomSessionCustomization(getChatRoomSessionCustomization());
    }

    private static void registerViewHolders() {
        NimUIKit.registerChatRoomMsgItemViewHolder(GuessAttachment.class, ChatRoomMsgViewHolderGuess.class);
    }

    private static ChatRoomSessionCustomization getChatRoomSessionCustomization() {
        if (customization == null) {
            customization = new ChatRoomSessionCustomization();
            ArrayList<BaseAction> actions = new ArrayList<>();
            actions.add(new GuessAction());
//            actions.add(new ImageAction());
            customization.actions = actions;
        }

        return customization;
    }
}
