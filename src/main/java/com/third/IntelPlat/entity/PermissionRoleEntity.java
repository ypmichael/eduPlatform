package com.third.IntelPlat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "ROLE_PERMISSION")
public class PermissionRoleEntity  implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3819720684016584199L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;	
	
	@ManyToOne
	@JoinColumn(name="ROLE_UID")
	@JsonIgnore
	private RoleEntity rolePermission;
	
	@ManyToOne
	@JoinColumn(name="PERMISSION_UID")
	@JsonIgnore
	private PermissionEntity permissionRole;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public RoleEntity getRolePermission() {
		return rolePermission;
	}

	public void setRolePermission(RoleEntity rolePermission) {
		this.rolePermission = rolePermission;
	}

	public PermissionEntity getPermissionRole() {
		return permissionRole;
	}

	public void setPermissionRole(PermissionEntity permissionRole) {
		this.permissionRole = permissionRole;
	}
	
	
}
