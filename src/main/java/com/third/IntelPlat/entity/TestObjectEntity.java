package com.third.IntelPlat.entity;

import com.third.IntelPlat.common.ExcelResources;

public class TestObjectEntity
{
  private String gradeId;
  private String moduleName;
  private String tutorialNo;
  private String tutorialName;
  private String subtutorialNo;
  private String subtutorialName;
  private String questionType;
  private String questionNo;
  private String quenstionContent;
  private String picture;
  private String choiceA;
  private String choiceB;
  private String choiceC;
  private String choiceD;
  private String Answer;
  
  @ExcelResources(order=1, title="Grade")
  public String getGradeId()
  {
    return this.gradeId;
  }
  
  public void setGradeId(String gradeId)
  {
    this.gradeId = gradeId;
  }
  
  @ExcelResources(order=2, title="Topic")
  public String getModuleName()
  {
    return this.moduleName;
  }
  
  public void setModuleName(String moduleName)
  {
    this.moduleName = moduleName;
  }
  
  @ExcelResources(order=3, title="Tutorial No")
  public String getTutorialNo()
  {
    return this.tutorialNo;
  }
  
  public void setTutorialNo(String tutorialNo)
  {
    this.tutorialNo = tutorialNo;
  }
  
  @ExcelResources(order=4, title="Tutorial Name")
  public String getTutorialName()
  {
    return this.tutorialName;
  }
  
  public void setTutorialName(String tutorialName)
  {
    this.tutorialName = tutorialName;
  }
  
  @ExcelResources(order=5, title="Subtutorial No")
  public String getSubtutorialNo()
  {
    return this.subtutorialNo;
  }
  
  public void setSubtutorialNo(String subtutorialNo)
  {
    this.subtutorialNo = subtutorialNo;
  }
  
  @ExcelResources(order=6, title="Subtutorial Name")
  public String getSubtutorialName()
  {
    return this.subtutorialName;
  }
  
  public void setSubtutorialName(String subtutorialName)
  {
    this.subtutorialName = subtutorialName;
  }
  
  @ExcelResources(order=7, title="Question type")
  public String getQuestionType()
  {
    return this.questionType;
  }
  
  public void setQuestionType(String questionType)
  {
    this.questionType = questionType;
  }
  
  @ExcelResources(order=8, title="Question No")
  public String getQuestionNo()
  {
    return this.questionNo;
  }
  
  public void setQuestionNo(String questionNo)
  {
    this.questionNo = questionNo;
  }
  
  @ExcelResources(order=9, title="Quenstion Content")
  public String getQuenstionContent()
  {
    return this.quenstionContent;
  }
  
  public void setQuenstionContent(String quenstionContent)
  {
    this.quenstionContent = quenstionContent;
  }
  
  @ExcelResources(order=10, title="Picture")
  public String getPicture()
  {
    return this.picture;
  }
  
  public void setPicture(String picture)
  {
    this.picture = picture;
  }
  
  @ExcelResources(order=11, title="Choice A")
  public String getChoiceA()
  {
    return this.choiceA;
  }
  
  public void setChoiceA(String choiceA)
  {
    this.choiceA = choiceA;
  }
  
  @ExcelResources(order=12, title="Choice B")
  public String getChoiceB()
  {
    return this.choiceB;
  }
  
  public void setChoiceB(String choiceB)
  {
    this.choiceB = choiceB;
  }
  
  @ExcelResources(order=13, title="Choice C")
  public String getChoiceC()
  {
    return this.choiceC;
  }
  
  public void setChoiceC(String choiceC)
  {
    this.choiceC = choiceC;
  }
  
  @ExcelResources(order=14, title="Choice D")
  public String getChoiceD()
  {
    return this.choiceD;
  }
  
  public void setChoiceD(String choiceD)
  {
    this.choiceD = choiceD;
  }
  
  @ExcelResources(order=15, title="Answer")
  public String getAnswer()
  {
    return this.Answer;
  }
  
  public void setAnswer(String answer)
  {
    this.Answer = answer;
  }
}
