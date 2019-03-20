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
@Table(name = "DIVISION_ROLE")
public class DivisionRoleEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -123623027841770720L;
	
	@Id
	@Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;	
	
	@ManyToOne
	@JoinColumn(name="ROLE_UID")
	@JsonIgnore
	private RoleEntity roleDivision;
	
	@ManyToOne
	@JoinColumn(name="DIVISION_UID")
	@JsonIgnore
	private DivisionEntity divisionRole;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public RoleEntity getRoleDivision() {
		return roleDivision;
	}

	public void setRoleDivision(RoleEntity roleDivision) {
		this.roleDivision = roleDivision;
	}

	public DivisionEntity getDivisionRole() {
		return divisionRole;
	}

	public void setDivisionRole(DivisionEntity divisionRole) {
		this.divisionRole = divisionRole;
	}
}
