package com.third.IntelPlat.entity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;


@Entity
@Table(name = "STUDENTS")
public class StudentsEntity implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -407084018063115046L;
	@Id
	@Column(name = "UID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;// 自增长的编号
	
	@JoinColumn(name="DIVISION_UID")
	private Integer divisionId;
	
	@Column(name = "ACCOUNT", nullable=false, length=50)
	private String account;

	@Column(name = "PASSWORD", nullable=false, length=200)
	private String passWord;
	
	@Column(name = "USER_TYPE", length=5)
	private Integer userType;
	
	@Column(name = "SEX", length=1)
	private Integer sex;
	
	/**
	 * 第一联系人
	 */
	@Column(name = "CONTACT", length=50)
	private String contact;
	/**
	 * 联系人电话号码
	 */
	@Column(name = "CELLPHONE", length=15)
	private String cellPhone;
	
	/**
	 * 联系人邮箱
	 */
	@Column(name = "EMAIL", length=50)
	private String email;
	
	/**
	 * 是否在线
	 */
	@Column(name = "IS_ONLINE", length=50)
	private String isOnline;
	
	@Column(name="REMARK", length=200)
	private String remark;
	
	@Column(name = "STATUS", nullable=false, length=1)
	private Integer status;
	
	@Column(name = "GRADE")
	private String grade;
	
	@Column(name = "CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name = "TOKEN")
	private String token;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
