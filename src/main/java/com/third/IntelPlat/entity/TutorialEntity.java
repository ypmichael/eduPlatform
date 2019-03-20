package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 知识点表
 * @author Administrator
 *
 */
@Entity
@Table(name = "TUTORIAL")
public class TutorialEntity implements Serializable,Comparable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8817158042022527653L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	@ManyToOne
	@JoinColumn(name="GRADE_UID")
	@JsonIgnore
	private GradeEntity grade;
	
	@OneToMany(mappedBy="tutorial")
	private List<ChoiceQstEntity> choiceQsts;
	
	@Column(name = "TUTORIAL_NO")
    private String tutorialNo;
	
	/**知识点名称*/
	@Lob
	@Column(name = "TUTORIAL_NAME")
    private String tutorialName;
	
    @Column(name = "TUTORIAL_LEVEL", length=5)
    private Integer tutorialLevel;
    
    @ManyToOne
    @JoinColumn(name="PARENT_UID")
    @JsonIgnore
    private TutorialEntity parent;
    
    /**子组织*/
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="PARENT_UID")
    @JsonIgnore
    private List<TutorialEntity> children = new ArrayList<TutorialEntity>();

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public GradeEntity getGrade() {
		return grade;
	}

	public void setGrade(GradeEntity grade) {
		this.grade = grade;
	}

	public List<ChoiceQstEntity> getChoiceQsts() {
		return choiceQsts;
	}

	public void setChoiceQsts(List<ChoiceQstEntity> choiceQsts) {
		this.choiceQsts = choiceQsts;
	}

	public String getTutorialNo() {
		return tutorialNo;
	}

	public void setTutorialNo(String tutorialNo) {
		this.tutorialNo = tutorialNo;
	}

	public String getTutorialName() {
		return tutorialName;
	}

	public void setTutorialName(String tutorialName) {
		this.tutorialName = tutorialName;
	}

	public Integer getTutorialLevel() {
		return tutorialLevel;
	}

	public void setTutorialLevel(Integer tutorialLevel) {
		this.tutorialLevel = tutorialLevel;
	}

	public TutorialEntity getParent() {
		return parent;
	}

	public void setParent(TutorialEntity parent) {
		this.parent = parent;
	}

	public List<TutorialEntity> getChildren() {
		return children;
	}

	public void setChildren(List<TutorialEntity> children) {
		this.children = children;
	}

	@Override
	public int compareTo(Object o) {
		return this.uid - ((TutorialEntity)o).getUid();
	}
}
