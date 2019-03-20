package com.third.IntelPlat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 知识点关系表
 * @author Administrator
 *
 */
@Entity
@Table(name = "TUTORIAL_RELATION")
public class TutorialRelationEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5021372370114981201L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	/**
	 * 知识点编号
	 */
	@Column(name = "TUTORIAL_NO")
    private String tutorialNo;
	
	/**
	 * 知识点关系
	 */
	@Column(name = "TUTORIAL_RELATION")
    private String tutorialRelation;

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
	 * @return the tutorialNo
	 */
	public String getTutorialNo() {
		return tutorialNo;
	}

	/**
	 * @param tutorialNo the tutorialNo to set
	 */
	public void setTutorialNo(String tutorialNo) {
		this.tutorialNo = tutorialNo;
	}

	/**
	 * @return the tutorialRelation
	 */
	public String getTutorialRelation() {
		return tutorialRelation;
	}

	/**
	 * @param tutorialRelation the tutorialRelation to set
	 */
	public void setTutorialRelation(String tutorialRelation) {
		this.tutorialRelation = tutorialRelation;
	}
}
