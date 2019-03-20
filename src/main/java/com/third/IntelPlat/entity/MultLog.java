package com.third.IntelPlat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 错题记录
 * 乘法口诀日志记录表
 * @author Administrator
 *
 */
@Entity
@Table(name = "MULT_LOG")
public class MultLog implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2596061401477992608L;

	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	/**
	 * 用户编号
	 */
	@Column(name = "USER_ID")
	private Integer userId;
	
	/**
	 * 题目序列号
	 */
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	/**
	 * 错题记录数
	 */
	@Column(name = "COUNT")
	private Integer count;

	/**
	 * @return the uid
	 */
	public Integer getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	
}
