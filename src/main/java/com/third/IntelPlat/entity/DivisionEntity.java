package com.third.IntelPlat.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "DIVISION")
public class DivisionEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "UID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;// 自增长的编号
	
	@Column(name = "DIVISION_NAME", nullable=false, length=20)
	String divisionName;
	
	@Column(name = "DESCRIPTION", length=100)
	String description;
	
    @ManyToOne
    @JoinColumn(name="PARENT_DIVISION_UID")
    @JsonIgnore
    private DivisionEntity parent;
    
    /**子组织*/
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="PARENT_DIVISION_UID")
    private List<DivisionEntity> children = new ArrayList<DivisionEntity>();
	
	@OneToMany(mappedBy="division")
	@JsonIgnore
	private Set<UserEntity> users;
	
	@OneToMany(mappedBy="divisionRole")
	@JsonIgnore
	private Set<DivisionRoleEntity> divisionRoles;
	
	@Column(name = "CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	
	@Transient
	private List<DivisionEntity> divisions;
	
	@Transient
	private List<RoleEntity> roles;
	
	@JsonIgnore
	@Version
	@Column(name = "VERSION")
	private Integer version;
	
	@Transient
	private Integer value;
	
	@Transient
	private String label;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DivisionEntity getParent() {
		return parent;
	}

	public void setParent(DivisionEntity parent) {
		this.parent = parent;
	}

	public List<DivisionEntity> getChildren() {
		return children;
	}

	public void setChildren(List<DivisionEntity> children) {
		this.children = children;
	}

	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	public Set<DivisionRoleEntity> getDivisionRoles() {
		return divisionRoles;
	}

	public void setDivisionRoles(Set<DivisionRoleEntity> divisionRoles) {
		this.divisionRoles = divisionRoles;
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

	public List<DivisionEntity> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<DivisionEntity> divisions) {
		this.divisions = divisions;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public DivisionEntity() {
		super();
	}
	

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put("uid", uid);
		json.put("divisionName", divisionName);
		json.put("description", description);
		return json;
	}

	public DivisionEntity(Integer uid, Integer parentDivisionUid, String divisionName, String description,
			Date createTime, Date updateTime) {
		super();
		this.uid = uid;
		this.divisionName = divisionName;
		this.description = description;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
	
}
