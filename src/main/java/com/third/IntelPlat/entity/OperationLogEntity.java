package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "OPERATION_LOG")
public class OperationLogEntity implements Serializable{
	/**
	 * 操作日志实体
	 */
	private static final long serialVersionUID = -7852610064178127434L;

	@Id
	@Column(name = "UID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	@Column(name = "ACCOUNT" , nullable = false, length = 20)
	private String account;
	
	@Column(name = "TIME", nullable = false)
	private Date time;
	
	@Column(name = "OPERATION" , nullable = false, length = 200)
	private String operation;
	
	@Column(name = "TYPE" , nullable = false, length = 50)
	private String type;
	
	@Column(name = "MODEL" , nullable = false , length = 100)
	private String model;
	
	@JsonIgnore
	@Version
	@Column(name = "VERSION" , nullable = false)
	private Integer version;
	
	public OperationLogEntity(String account, Date time, String operation, String type,
			String model) {
		super();
		this.account = account;
		this.time = time;
		this.operation = operation;
		this.type = type;
		this.model = model;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
