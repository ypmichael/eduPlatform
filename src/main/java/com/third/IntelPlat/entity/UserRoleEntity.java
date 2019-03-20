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
@Table(name="USER_ROLE")
public class UserRoleEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1757259859701438880L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	@ManyToOne
	@JoinColumn(name="USER_UID")
	@JsonIgnore
	private UserEntity userRole;
	
	@ManyToOne
	@JoinColumn(name="ROLE_UID")
	@JsonIgnore
	private RoleEntity roleUser;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public UserEntity getUserRole() {
		return userRole;
	}

	public void setUserRole(UserEntity userRole) {
		this.userRole = userRole;
	}

	public RoleEntity getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(RoleEntity roleUser) {
		this.roleUser = roleUser;
	}
	
}
