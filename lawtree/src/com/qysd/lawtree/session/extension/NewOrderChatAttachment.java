package com.qysd.lawtree.session.extension;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class NewOrderChatAttachment extends CustomAttachment {

    public String getDataOrder() {
        return dataOrder;
    }

    public void setDataOrder(String dataOrder) {
        this.dataOrder = dataOrder;
    }

    private String dataOrder;

    public NewOrderChatAttachment() {
        super(CustomAttachmentType.NewOrder);
    }

    @Override
    protected void parseData(JSONObject data) {
        Log.e("songlonglong",data.toJSONString());
        setDataOrder(data.toJSONString());
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        return object;
    }


}
