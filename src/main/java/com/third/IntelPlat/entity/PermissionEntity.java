package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "PERMISSION")
public class PermissionEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4777167738656710328L;
	
	@Id
	@Column(name = "UID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;// 自增长的编号
	
	@OneToMany(mappedBy="permissionRole")
	@JsonIgnore
	private Set<PermissionRoleEntity> permissionRoles;
	
	@Column(name = "PERMISSION_NAME", nullable=false, length=100)
	private String permissionName;
	
	@Column(name = "PERMISSION_IDENTIFY", nullable=false, length=100)
	private String permissionIdentify;
	
	@Column(name = "REMARK", length=250)
	private String remark;
	
	@Column(name = "CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	
	@JsonIgnore
	@Version
	@Column(name = "VERSION")
	private Integer version;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Set<PermissionRoleEntity> getPermissionRoles() {
		return permissionRoles;
	}

	public void setPermissionRoles(Set<PermissionRoleEntity> permissionRoles) {
		this.permissionRoles = permissionRoles;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionIdentify() {
		return permissionIdentify;
	}

	public void setPermissionIdentify(String permissionIdentify) {
		this.permissionIdentify = permissionIdentify;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("uid", uid);
		json.put("permissionName", permissionName);
		json.put("permissionIdentify", permissionIdentify);
		json.put("remark", remark);
		json.put("createTime", createTime);
		json.put("updateTime", updateTime);
		return json.toString();
	}
	
	
}
