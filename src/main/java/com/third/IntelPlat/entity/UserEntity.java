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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER")
public class UserEntity implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//DELETE(0), ACTIVATE(1), DISABLE(2);//0:账户已删除 1:启用状态 2:禁用状态 
	public static final Integer STATUS_DELETE = 0;
	public static final Integer STATUS_ACTIVATE = 1;
	public static final Integer STATUS_DISABLE = 2;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;	
	
	@ManyToOne
	@JoinColumn(name="DIVISION_UID")
	@JsonIgnore
	private DivisionEntity division;
	
	@JsonIgnore
	@OneToMany(mappedBy="userRole")
	private Set<UserRoleEntity> userRoles;
	
	@Column(name = "ACCOUNT", nullable=false, length=50)
	private String userName;

	@Column(name = "PASSWORD", nullable=false, length=200)
	private String password;
	
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
	
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	
	@Column(name = "OPERATOR", nullable = false)
	private String operator;
	
	@Column(name = "LOGIN_TIME")
	private Date loginTime;
	
	@Column(name = "TOKEN")
	private String token;
	
	@Transient
	private List<RoleEntity> roles;
	
	@Transient
	private Integer divisionUid;
	
	@Transient
	private String divisionName;
	
	@Version
	@Column(name = "VERSION")
	@JsonIgnore
	private Integer version;
	
	public UserEntity() {
		super();
	}
	
	public Integer getUid() {
		return uid;
	}


	public void setUid(Integer uid) {
		this.uid = uid;
	}


	public DivisionEntity getDivision() {
		return division;
	}


	public void setDivision(DivisionEntity division) {
		this.division = division;
	}


	public Set<UserRoleEntity> getUserRoles() {
		return userRoles;
	}


	public void setUserRoles(Set<UserRoleEntity> userRoles) {
		this.userRoles = userRoles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}


	public Date getLoginTime() {
		return loginTime;
	}


	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public List<RoleEntity> getRoles() {
		return roles;
	}


	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}


	public Integer getDivisionUid() {
		return divisionUid;
	}


	public void setDivisionUid(Integer divisionUid) {
		this.divisionUid = divisionUid;
	}


	public String getDivisionName() {
		return divisionName;
	}


	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}


	public Integer getVersion() {
		return version;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("uid", uid);
		json.put("account", userName);
		json.put("userType", userType);
		json.put("sex", sex);
		json.put("contact", contact);
		json.put("cellPhone", cellPhone);
		json.put("email", email);
		json.put("isOnline", isOnline);
		json.put("token", token);
		json.put("divisionUid", division == null ? null : division.getUid());
		json.put("status", status);
		json.put("operator", operator);
		json.put("loginTime", loginTime);
		json.put("createTime", createTime);
		json.put("remark", remark);
		json.put("grade", grade);
		return json.toString();
	}

	public UserEntity(Integer uid, String account, String remark, Integer status, Date createTime,
			Date updateTime, String operator, Date loginTime, String token, List<RoleEntity> roles, Integer divisionUid, String divisionName, 
			Integer userType, Integer sex, String contact, String cellPhone, String email) {
		super();
		this.uid = uid;
		this.userName = account;
		this.remark = remark;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.operator = operator;
		this.loginTime = loginTime;
		this.token = token;
		this.roles = roles;
		this.divisionUid = divisionUid;
		this.divisionName = divisionName;
		this.userType = userType;
		this.sex = sex;
		this.contact = contact;
		this.cellPhone = cellPhone;
		this.email = email;
	}
	
}
