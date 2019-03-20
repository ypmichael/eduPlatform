package com.third.IntelPlat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 乘法口诀练习日志记录表
 * @author Administrator
 *
 */
@Entity
@Table(name = "MARK_RECORD")
public class MarkRecordEntity implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6979317837812094525L;
	
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
	 * 试题详情
	 */
	@Lob
	@Column(name = "CONTENT")
	private String content;
	
	/**
	 * 分数
	 */
	@Column(name = "score")
	private String score;

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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	
	
	
}
