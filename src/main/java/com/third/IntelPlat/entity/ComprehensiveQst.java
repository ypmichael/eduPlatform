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
 * 综合题
 * @author Administrator
 *
 */
@Entity
@Table(name = "COMPREHENSIVE_QST")
public class ComprehensiveQst implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5960559873656299721L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	@Column(name = "QUESTION_NO")
	private String questionNo;
	
	/**
	 * 试题内容
	 */
	@Lob
	@Column(name = "CHOICE_CONTENT")
	private String choiceContent;
	
    @Column(name = "QUESTION_TYPE")
    private String questionType;
	
	/**
	 * 正确答案
	 */
	@Column(name = "CHOICE_ANSW", length=250)
	private String choiceAnsw;
	
	@Column(name = "CHOICE_A")
	private String choiceA;
	
	@Column(name = "CHOICE_B")
	private String choiceB;
	
	@Column(name = "CHOICE_C")
	private String choiceC;
	
	@Column(name = "CHOICE_D")
	private String choiceD;
	
	@Column(name = "KNOWLEDGE_A")
	private String knowledgeA;
	
	@Column(name = "KNOWLEDGE_B")
	private String knowledgeB;
	
	@Column(name = "KNOWLEDGE_C")
	private String knowledgeC;
	
	@Column(name = "KNOWLEDGE_D")
	private String knowledgeD;
	
	@Column(name = "PICTURE", length=250)
	private String picture;
	
	@Column(name = "GRADE_UID")
	private String gradeUid;

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
	 * @return the questionNo
	 */
	public String getQuestionNo() {
		return questionNo;
	}

	/**
	 * @param questionNo the questionNo to set
	 */
	public void setQuestionNo(String questionNo) {
		this.questionNo = questionNo;
	}

	/**
	 * @return the choiceContent
	 */
	public String getChoiceContent() {
		return choiceContent;
	}

	/**
	 * @param choiceContent the choiceContent to set
	 */
	public void setChoiceContent(String choiceContent) {
		this.choiceContent = choiceContent;
	}

	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	/**
	 * @return the choiceAnsw
	 */
	public String getChoiceAnsw() {
		return choiceAnsw;
	}

	/**
	 * @param choiceAnsw the choiceAnsw to set
	 */
	public void setChoiceAnsw(String choiceAnsw) {
		this.choiceAnsw = choiceAnsw;
	}

	/**
	 * @return the choiceA
	 */
	public String getChoiceA() {
		return choiceA;
	}

	/**
	 * @param choiceA the choiceA to set
	 */
	public void setChoiceA(String choiceA) {
		this.choiceA = choiceA;
	}

	/**
	 * @return the choiceB
	 */
	public String getChoiceB() {
		return choiceB;
	}

	/**
	 * @param choiceB the choiceB to set
	 */
	public void setChoiceB(String choiceB) {
		this.choiceB = choiceB;
	}

	/**
	 * @return the choiceC
	 */
	public String getChoiceC() {
		return choiceC;
	}

	/**
	 * @param choiceC the choiceC to set
	 */
	public void setChoiceC(String choiceC) {
		this.choiceC = choiceC;
	}

	/**
	 * @return the choiceD
	 */
	public String getChoiceD() {
		return choiceD;
	}

	/**
	 * @param choiceD the choiceD to set
	 */
	public void setChoiceD(String choiceD) {
		this.choiceD = choiceD;
	}

	/**
	 * @return the knowledgeA
	 */
	public String getKnowledgeA() {
		return knowledgeA;
	}

	/**
	 * @param knowledgeA the knowledgeA to set
	 */
	public void setKnowledgeA(String knowledgeA) {
		this.knowledgeA = knowledgeA;
	}

	/**
	 * @return the knowledgeB
	 */
	public String getKnowledgeB() {
		return knowledgeB;
	}

	/**
	 * @param knowledgeB the knowledgeB to set
	 */
	public void setKnowledgeB(String knowledgeB) {
		this.knowledgeB = knowledgeB;
	}

	/**
	 * @return the knowledgeC
	 */
	public String getKnowledgeC() {
		return knowledgeC;
	}

	/**
	 * @param knowledgeC the knowledgeC to set
	 */
	public void setKnowledgeC(String knowledgeC) {
		this.knowledgeC = knowledgeC;
	}

	/**
	 * @return the knowledgeD
	 */
	public String getKnowledgeD() {
		return knowledgeD;
	}

	/**
	 * @param knowledgeD the knowledgeD to set
	 */
	public void setKnowledgeD(String knowledgeD) {
		this.knowledgeD = knowledgeD;
	}

	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
	 * @return the gradeUid
	 */
	public String getGradeUid() {
		return gradeUid;
	}

	/**
	 * @param gradeUid the gradeUid to set
	 */
	public void setGradeUid(String gradeUid) {
		this.gradeUid = gradeUid;
	}
}
