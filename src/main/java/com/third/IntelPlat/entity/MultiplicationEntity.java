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
 * 乘法口诀试题表
 * @author Administrator
 *
 */
@Entity
@Table(name = "MULTIPLICATION")
public class MultiplicationEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6129265991532149321L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	//序号
	@Column(name = "SEQUENCE")
	private Integer sequence;
	//题目
	@Column(name = "CONTENT")
	private String content;
	//答案
	@Column(name = "ANSWER")
	private Integer answer;
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
	 * @return the answer
	 */
	public Integer getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(Integer answer) {
		this.answer = answer;
	}
	
	
}
