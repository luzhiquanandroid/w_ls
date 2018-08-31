package com.qysd.lawtree.lawtreebean;

import java.io.Serializable;
import java.util.List;

/**
 * 发起群聊 我的企业和外部企业共用的bean
 */

public class MyQiyeQunLiaoBean {
//    {
//        "code": "1",
//            "num": 200,
//            "status": [{
//        "deptId": 0,
//                "deptName": "",
//                "userId": "7c4951d11ce74a279c81967d96fd5f7b",
//                "userName": "明",
//                "position": "大Boss",
//                "jobNum": "",
//                "mobileNum": "13141103345",
//                "sign": "个性签名",
//                "idCard": "34042119920806121X",
//                "birthDay": 946656000000,
//                "sex": 1,
//                "compName": "无法无天公司",
//                "reqStatus": 3,
//                "birthDayStr": "",
//                "headUrl": "http://39.106.154.149:90/images/3b6d9117989a4046b5bdf1b8b5a53c1e.png",
//                "compId": "16",
//                "procedureId": ""
//    }]
//    }
    private String code;
    private List<Status> status;

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class Status implements Serializable {
        private String compId;
        private String compName;
        private String deptId;

        private String headUrl;
        private String idCard;
        private String mobileNum;
        private String position;
        private String reqStatus;
        private String sex;
        private String sign;
        private String userId;
        private String userName;
        //private BirthDay birthDay;

        public String getCompId() {
            return compId;
        }

        public void setCompId(String compId) {
            this.compId = compId;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getReqStatus() {
            return reqStatus;
        }

        public void setReqStatus(String reqStatus) {
            this.reqStatus = reqStatus;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

//        public BirthDay getBirthDay() {
//            return birthDay;
//        }
//
//        public void setBirthDay(BirthDay birthDay) {
//            this.birthDay = birthDay;
//        }

        public class BirthDay implements Serializable {
            private String date;
            private String day;
            private String hours;
            private String minutes;
            private String month;
            private String seconds;
            private String time;
            private String timezoneOffset;
            private String year;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getHours() {
                return hours;
            }

            public void setHours(String hours) {
                this.hours = hours;
            }

            public String getMinutes() {
                return minutes;
            }

            public void setMinutes(String minutes) {
                this.minutes = minutes;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getSeconds() {
                return seconds;
            }

            public void setSeconds(String seconds) {
                this.seconds = seconds;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(String timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
