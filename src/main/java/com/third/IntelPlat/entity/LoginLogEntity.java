package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "LOGIN_LOG")
public class LoginLogEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6567277567976167674L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	@Column(name = "USER_ID")
	private Integer userId;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "LOGIN_TIME")
	private Date loginTime;
	
	@Column(name = "create_Time")
	private Date createTime;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
