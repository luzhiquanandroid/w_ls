package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class YingyongBean {
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
        private String appIcon;
        private String appName;
        private String permId;
        private List<SubMenuItems> subMenuItems;

        public String getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(String appIcon) {
            this.appIcon = appIcon;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPermId() {
            return permId;
        }

        public void setPermId(String permId) {
            this.permId = permId;
        }

        public List<SubMenuItems> getSubMenuItems() {
            return subMenuItems;
        }

        public void setSubMenuItems(List<SubMenuItems> subMenuItems) {
            this.subMenuItems = subMenuItems;
        }

        public class SubMenuItems {
           private String  appIcon;
           private String appName;
           private String permId;
           private String appCode;
           private String appPath;

            public String getAppIcon() {
                return appIcon;
            }

            public void setAppIcon(String appIcon) {
                this.appIcon = appIcon;
            }

            public String getAppName() {
                return appName;
            }

            public void setAppName(String appName) {
                this.appName = appName;
            }

            public String getPermId() {
                return permId;
            }

            public void setPermId(String permId) {
                this.permId = permId;
            }

            public String getAppCode() {
                return appCode;
            }

            public void setAppCode(String appCode) {
                this.appCode = appCode;
            }

            public String getAppPath() {
                return appPath;
            }

            public void setAppPath(String appPath) {
                this.appPath = appPath;
            }
        }
    }
//    {
//        "code": "1",
//            "status": [
//        {
//            "appIcon": "",
//                "appName": "生产模块",
//                "permId": 18,
//                "subMenuItems": [
//            {
//                "appIcon": "http://192.168.1.123:8080/static/app_image/shengchanjihua.png",
//                    "appName": "生产计划",
//                    "permId": 39,
//                    "subMenuItems": [],
//                "appCode": "A001-01",
//                    "appPath": "http://192.168.1.123:8080/productionPlan/selectProductionPlanList"
//            },
//      ],
//            "appCode": "A001",
//                "appPath": ""
//        },
//        {
//            "appIcon": "",
//                "appName": "库存模块",
//                "permId": 16,
//                "subMenuItems": [
//            {
//                "appIcon": "http://192.168.1.123:8080/static/app_image/kucunchaxun.png",
//                    "appName": "库存查询",
//                    "permId": 36,
//                    "subMenuItems": [],
//                "appCode": "A002-01",
//                    "appPath": "http://192.168.1.123:8080/repertory/searchRepertoryDetailList"
//            }
//      ],
//            "appCode": "A002",
//                "appPath": ""
//        },
//        {
//            "appIcon": "",
//                "appName": "订单模块",
//                "permId": 17,
//                "subMenuItems": [
//            {
//                "appIcon": "http://192.168.1.123:8080/static/app_image/dingdanguanli.png",
//                    "appName": "订单管理",
//                    "permId": 34,
//                    "subMenuItems": [],
//                "appCode": "A003-01",
//                    "appPath": "http://192.168.1.123:8080"
//            }
//      ],
//            "appCode": "A003",
//                "appPath": ""
//        }
//  ]
//    }
}
