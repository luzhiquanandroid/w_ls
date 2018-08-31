package com.qysd.lawtree.lawtreebean;

public class LianXiRenDetailBean {
    //    {
//        "code": "1",
//            "status": {
//        "birthDay": {
//            "date": 9,
//                    "hours": 0,
//                    "seconds": 0,
//                    "month": 3,
//                    "timezoneOffset": -480,
//                    "year": 118,
//                    "minutes": 0,
//                    "time": 1523203200000,
//                    "day": 1
//        },
//        "idCard": "34042119920806121X",
//                "sex": 1,
//                "deptId": 0,
//                "headUrl": "http://192.168.1.152/images/4cf50bff8b2a4e928a6b1fa3d547af30.png",
//                "sign": "哈哈哈哈还哈",
//                "userName": "刘明明",
//                "compName": "律树",
//                "userId": "dfad7dc2bc0145dc90c2bde32f219c56",
//                "mobileNum": "13141103344",
//                "reqStatus": 2, 0未同意 1好友 2删除 3未发送邀请
//                "compId": "2",
//                "birthDayStr": "2018年04月09日",
//                "position": "高级iOS开发工程师"
//    }
//    }
    private String code;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class Status {
        private String birthDayStr;

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

        public String getBirthDayStr() {
            return birthDayStr;
        }

        public void setBirthDayStr(String birthDayStr) {
            this.birthDayStr = birthDayStr;
        }

        public class BirthDay {
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
