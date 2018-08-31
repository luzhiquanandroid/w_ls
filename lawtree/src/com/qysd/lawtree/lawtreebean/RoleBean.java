package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class RoleBean {
    //    {
//        "code": "1",
//            "status": [
//        {
//            "roleId": 2,
//                "roleName": "物料管理员"
//        },
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
        private String roleId;
        private String roleName;

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }
}
