package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class ProductLIstBean {
    private String code;
    private List<ProductList> status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ProductList> getStatus() {
        return status;
    }

    public void setStatus(List<ProductList> status) {
        this.status = status;
    }

    public class ProductList{
//        "salesOrderId": 19,
//                "cate": 1,
//                "productId": 45,
//                "unitName": "个",
//                "totalPrice": "0",
//                "num": 10,
//                "memo": "",
//                "cateName": "定制",
//                "productName": "钢钉",
//                "perPrice": "0",
//                "productCode": "4127974543754",
//                "norms": "GD-1001",
//                "unitId": 5
        private String salesOrderId;
        private String cate;
        private String productId;
        private String unitName;
        private String totalPrice;
        private String num;
        private String memo;
        private String cateName;
        private String productName;
        private String perPrice;
        private String productCode;
        private String norms;
        private String unitId;

        public String getSalesOrderId() {
            return salesOrderId;
        }

        public void setSalesOrderId(String salesOrderId) {
            this.salesOrderId = salesOrderId;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPerPrice() {
            return perPrice;
        }

        public void setPerPrice(String perPrice) {
            this.perPrice = perPrice;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getNorms() {
            return norms;
        }

        public void setNorms(String norms) {
            this.norms = norms;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }
    }
}
