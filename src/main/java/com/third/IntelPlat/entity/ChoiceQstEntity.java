package com.third.IntelPlat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="CHOICE_QST")
public class ChoiceQstEntity
  implements Serializable
{
  private static final long serialVersionUID = -5960559873656299721L;
  @Id
  @Column(name="UID")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer uid;
  @ManyToOne
  @JoinColumn(name="TUTORIAL_UID")
  @JsonIgnore
  private TutorialEntity tutorial;
  @Column(name="QUESTION_NO")
  private String questionNo;
  @Lob
  @Column(name="CHOICE_CONTENT")
  private String choiceContent;
  @Column(name="CHOICE_LEVEL", length=5)
  private Integer choiceLevel;
  @Column(name="topic")
  private String topic;
  @Column(name="QUESTION_TYPE")
  private String questionType;
  @Column(name="PICTURE", length=250)
  private String picture;
  @Column(name="CHOICE_ANSW", length=250)
  private String choiceAnsw;
  @Column(name="SCORE", length=5)
  private Integer score;
  @Column(name="RADIO")
  private String radio;
  @Column(name="CHOICE_A")
  private String choiceA;
  @Column(name="CHOICE_B")
  private String choiceB;
  @Column(name="CHOICE_C")
  private String choiceC;
  @Column(name="CHOICE_D")
  private String choiceD;
  @Column(name="CREATE_TIME", nullable=false)
  private Date createTime;
  @Column(name="UPDATE_TIME")
  private Date updateTime;
  @Transient
  private Integer tutorialUid;
  @Transient
  private String tutorialName;
  @Transient
  private String tutorialNo;
  
  public Integer getUid()
  {
    return this.uid;
  }
  
  public void setUid(Integer uid)
  {
    this.uid = uid;
  }
  
  public TutorialEntity getTutorial()
  {
    return this.tutorial;
  }
  
  public void setTutorial(TutorialEntity tutorial)
  {
    this.tutorial = tutorial;
  }
  
  public String getQuestionNo()
  {
    return this.questionNo;
  }
  
  public void setQuestionNo(String questionNo)
  {
    this.questionNo = questionNo;
  }
  
  public String getChoiceContent()
  {
    return this.choiceContent;
  }
  
  public void setChoiceContent(String choiceContent)
  {
    this.choiceContent = choiceContent;
  }
  
  public Integer getChoiceLevel()
  {
    return this.choiceLevel;
  }
  
  public void setChoiceLevel(Integer choiceLevel)
  {
    this.choiceLevel = choiceLevel;
  }
  
  public String getTopic()
  {
    return this.topic;
  }
  
  public void setTopic(String topic)
  {
    this.topic = topic;
  }
  
  public String getQuestionType()
  {
    return this.questionType;
  }
  
  public void setQuestionType(String questionType)
  {
    this.questionType = questionType;
  }
  
  public String getPicture()
  {
    return this.picture;
  }
  
  public void setPicture(String picture)
  {
    this.picture = picture;
  }
  
  public String getChoiceAnsw()
  {
    return this.choiceAnsw;
  }
  
  public void setChoiceAnsw(String choiceAnsw)
  {
    this.choiceAnsw = choiceAnsw;
  }
  
  public Integer getScore()
  {
    return this.score;
  }
  
  public void setScore(Integer score)
  {
    this.score = score;
  }
  
  public String getRadio()
  {
    return this.radio;
  }
  
  public void setRadio(String radio)
  {
    this.radio = radio;
  }
  
  public String getChoiceA()
  {
    return this.choiceA;
  }
  
  public void setChoiceA(String choiceA)
  {
    this.choiceA = choiceA;
  }
  
  public String getChoiceB()
  {
    return this.choiceB;
  }
  
  public void setChoiceB(String choiceB)
  {
    this.choiceB = choiceB;
  }
  
  public String getChoiceC()
  {
    return this.choiceC;
  }
  
  public void setChoiceC(String choiceC)
  {
    this.choiceC = choiceC;
  }
  
  public Date getCreateTime()
  {
    return this.createTime;
  }
  
  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
  }
  
  public Date getUpdateTime()
  {
    return this.updateTime;
  }
  
  public void setUpdateTime(Date updateTime)
  {
    this.updateTime = updateTime;
  }
  
  public Integer getTutorialUid()
  {
    return this.tutorialUid;
  }
  
  public void setTutorialUid(Integer tutorialUid)
  {
    this.tutorialUid = tutorialUid;
  }
  
  public String getTutorialName()
  {
    return this.tutorialName;
  }
  
  public void setTutorialName(String tutorialName)
  {
    this.tutorialName = tutorialName;
  }
  
  public String getTutorialNo()
  {
    return this.tutorialNo;
  }
  
  public void setTutorialNo(String tutorialNo)
  {
    this.tutorialNo = tutorialNo;
  }
  
  public String getChoiceD()
  {
    return this.choiceD;
  }
  
  public void setChoiceD(String choiceD)
  {
    this.choiceD = choiceD;
  }
}
