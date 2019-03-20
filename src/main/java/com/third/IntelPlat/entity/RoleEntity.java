package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Table(name = "ROLE")
public class RoleEntity  implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2054442847506350297L;
	
	@Id
	@Column(name = "UID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;// 自增长的编号
	
	@OneToMany(cascade={CascadeType.REMOVE} ,mappedBy="roleUser")
	@JsonIgnore
	private Set<UserRoleEntity> roleUsers;
	
	@OneToMany(cascade={CascadeType.REMOVE} ,mappedBy="rolePermission")
	@JsonIgnore
	private Set<PermissionRoleEntity> rolePermissions;
	
	@OneToMany(cascade={CascadeType.REMOVE} ,mappedBy="roleDivision")
	@JsonIgnore
	private Set<DivisionRoleEntity> roleDivisions;
	
	@Column(name = "ROLE_NAME", nullable=false, length = 100)
	private String roleName;
	
	@Column(name = "REMARK", length = 250)
	private String remark;
	
	@Column(name = "CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	
	@JsonIgnore
	@Version
	@Column(name = "VERSION")
	private Integer version;
	
	public RoleEntity() {
		super();
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Set<UserRoleEntity> getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(Set<UserRoleEntity> roleUsers) {
		this.roleUsers = roleUsers;
	}

	public Set<PermissionRoleEntity> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(Set<PermissionRoleEntity> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

	public Set<DivisionRoleEntity> getRoleDivisions() {
		return roleDivisions;
	}

	public void setRoleDivisions(Set<DivisionRoleEntity> roleDivisions) {
		this.roleDivisions = roleDivisions;
	}
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
		json.put("roleName", roleName);
		json.put("remark", remark);
		json.put("createTime", createTime);
		json.put("updateTime", updateTime);
		return json.toString();
	}

	public RoleEntity(Integer uid, String roleName) {
		super();
		this.uid = uid;
		this.roleName = roleName;
	}
}
