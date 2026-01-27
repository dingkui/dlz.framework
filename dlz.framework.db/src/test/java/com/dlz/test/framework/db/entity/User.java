package com.dlz.test.framework.db.entity;

import com.dlz.framework.db.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class User {
    private Long id;
    private String name;
    private Integer age;
    private Date createTime;
    private String sex;
    private String score;
    private String deptId;
    private String level;
    private int retryCount;
    private String address;
    private String phone;
    private String vip;
    private String email;
    private String remark;
    private String status;
    private String createUser;
    private String updateUser;
    private Date updateTime;
    private String deleteFlag;
    private String deleteUser;
    private Date deleteTime;
    private String isExpired;
    private String code;
    private String type;
    private String city;
    private String description;

}
