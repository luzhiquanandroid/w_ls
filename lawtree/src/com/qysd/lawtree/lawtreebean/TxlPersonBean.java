package com.qysd.lawtree.lawtreebean;

import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/16.
 */

public class TxlPersonBean {
    //    {
//        "code": "1",
//            "status": [
//        {
//            "headUrl": "",
//                "mobileNum": "17710740963",
//                "status": 1, 1未注册显示邀请 2 注册但不是好友显示添加 3已是好友，显示已添加
//                "userName": "吕子乔"
//        }
//    ]
//    }
    private String code;
    private List<Status> status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public class Status {
        private String headUrl;
        private String mobileNum;
        private String status;
        private String userName;

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
