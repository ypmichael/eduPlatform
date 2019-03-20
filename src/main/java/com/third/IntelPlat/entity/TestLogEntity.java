package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_LOG")
public class TestLogEntity implements Serializable {
	private static final int CORRECT = 1;
	private static final int ERROR = 0;
	private static final long serialVersionUID = 929311209105675347L;
	@Id
	@Column(name = "UID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	@Column(name = "USER_UID")
	private Integer userUid;
	@Column(name = "USER_NAME", length = 50)
	private String userName;
	@Column(name = "TUTORIAL_UID")
	private Integer tutorialUid;
	@Column(name = "TUTORIAL_NO", length = 250)
	private String tutorialNo;
	@Lob
	@Column(name = "TUTORIAL_NAME")
	private String tutorialName;
	@Column(name = "QUESTION_NO")
	private Integer questionNo;
	@Lob
	@Column(name = "CHOICE_CONTENT")
	private String choiceContent;
	@Column(name = "PICTURE", length = 250)
	private String picture;
	@Column(name = "radio", length = 250)
	private String radio;
	@Column(name = "RESULT")
	private int result;
	@Column(name = "ANSW")
	private String answ;
	@Column(name = "QUESTION_TYPE")
	private String questionType;
	@Column(name = "STUDENT_OPT_ANSW", length = 50)
	private String studentOptAnsw;
	@Column(name = "TIME", length = 50)
	private Long duration;
	@Column(name = "START_TIME", nullable = false)
	private Date startTime;
	@Column(name = "END_TIME")
	private Date endTime;
	@Column(name = "QUESTION_NUMBER")
	private String questionNumber;
	@Column(name = "CREATE_TIME")
	private Date createTime;

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getUserUid() {
		return this.userUid;
	}

	public void setUserUid(Integer userUid) {
		this.userUid = userUid;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getTutorialUid() {
		return this.tutorialUid;
	}

	public void setTutorialUid(Integer tutorialUid) {
		this.tutorialUid = tutorialUid;
	}

	public String getTutorialNo() {
		return this.tutorialNo;
	}

	public void setTutorialNo(String tutorialNo) {
		this.tutorialNo = tutorialNo;
	}

	public String getTutorialName() {
		return this.tutorialName;
	}

	public void setTutorialName(String tutorialName) {
		this.tutorialName = tutorialName;
	}

	public Integer getQuestionNo() {
		return this.questionNo;
	}

	public void setQuestionNo(Integer questionNo) {
		this.questionNo = questionNo;
	}

	public String getChoiceContent() {
		return this.choiceContent;
	}

	public void setChoiceContent(String choiceContent) {
		this.choiceContent = choiceContent;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getRadio() {
		return this.radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public int getResult() {
		return this.result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getAnsw() {
		return this.answ;
	}

	public void setAnsw(String answ) {
		this.answ = answ;
	}

	public String getStudentOptAnsw() {
		return this.studentOptAnsw;
	}

	public void setStudentOptAnsw(String studentOptAnsw) {
		this.studentOptAnsw = studentOptAnsw;
	}

	public Long getDuration() {
		return this.duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getQuestionNumber() {
		return this.questionNumber;
	}

	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}
}
