package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "GRADE")
public class GradeEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7078897805467603747L;
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	@OneToMany(mappedBy="grade")
	@JsonIgnore
	private List<TutorialEntity> tutorials;
	
	/**
	 * 年级名称
	 */
	@Column(name = "GRADE_NAME" , nullable = false, length = 50)
	private String gradeName;

	@Column(name = "CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name = "UPDATE_TIME")
	private Date updateTime;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public List<TutorialEntity> getTutorials() {
		return tutorials;
	}

	public void setTutorials(List<TutorialEntity> tutorials) {
		this.tutorials = tutorials;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
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
