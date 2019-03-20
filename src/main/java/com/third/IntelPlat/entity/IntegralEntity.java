package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分表
 * @author Administrator
 *
 */
@Entity
@Table(name = "INTEGRAL")
public class IntegralEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6900603317320439136L;
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	/**
	 * 积分值
	 */
	@Column(name = "INTEGRAL_VALUE" , nullable = false, length = 10)
	private Integer IntegralValue;
	
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME", nullable = false)
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	@Column(name = "UPDATE_TIME")
	private Date updateTime;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getIntegralValue() {
		return IntegralValue;
	}

	public void setIntegralValue(Integer integralValue) {
		IntegralValue = integralValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
