package com.second.hand.trading.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * sh_user
 * @author myl
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "sh_user")
public class UserModel implements Serializable {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号（手机号）
     */
    @TableField("account_number")
    private String accountNumber;

    /**
     * 登录密码
     */
    @TableField("user_password")
    private String userPassword;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 注册时间
     */
    @TableField("sign_in_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signInTime;
    @TableField("user_status")
    private Byte userStatus;
    @TableField("phone")
    private String phone;
    @TableField("email")
    private String email;
    @TableField("city")
    private String city;
    @TableField("sex")
    private String sex;
    @TableField("bank_account")
    private String bankAccount;
    @TableField("wallet")
    private BigDecimal wallet;
    @TableField("integral")
    private Integer integral;
    @TableField("business_license")
    private String businessLicense;
    @TableField("id_card")
    private String idCard;
    @TableField("level")
    private Integer level;
    @TableField("type")
    private Integer type;

    private static final long serialVersionUID = 1L;



    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", signInTime=" + signInTime +
                ", userStatus=" + userStatus +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", sex='" + sex + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                '}';
    }
}
