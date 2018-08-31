package com.qysd.uikit.business.chatroom.viewholder;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.qysd.uikit.R;
import com.qysd.uikit.api.NimUIKit;
import com.qysd.uikit.business.session.emoji.MoonUtil;
import com.qysd.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.qysd.uikit.common.util.sys.ScreenUtil;

/**
 * Created by hzxuwen on 2016/1/18.
 */
public class ChatRoomMsgViewHolderText extends ChatRoomMsgViewHolderBase {

    protected TextView bodyTextView;


    public ChatRoomMsgViewHolderText(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_text;
    }

    @Override
    protected void inflateContentView() {
        bodyTextView = findViewById(R.id.nim_message_item_text_body);
    }

    protected String getDisplayText() {
        return message.getContent();
    }

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    protected void bindContentView() {
        bodyTextView.setTextColor(Color.BLACK);
        bodyTextView.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
    }
}
