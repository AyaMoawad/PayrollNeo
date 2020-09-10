package com.entities.empStruct;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="emp_struct_subparent")
public class EmpStructSubparent {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="subparent_id")
	private int id;
	
	@Column(name = "has_parent")
	private int hasParent;
	
	
	@Column(name="parent_code")
	private String parentCode;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "commonID_id")
	private CommonID commID;
	

	@ManyToOne(cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.REFRESH,
					CascadeType.DETACH })
	@JoinColumn(name = "parent_id")
	private EmpStructParent parent;
		
	public EmpStructSubparent() {
		
	}
	
	@JsonIgnore
	@OneToMany(mappedBy = "subParent" ,
			cascade = CascadeType.ALL,
			  fetch = FetchType.EAGER)
	private List<EmpStructChild> children;
	
	
	public EmpStructSubparent(int hasParent, String parentCode, CommonID commID) {
		this.hasParent = hasParent;
		this.parentCode = parentCode;
		this.commID = commID;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHasParent() {
		return hasParent;
	}

	public void setHasParent(int hasParent) {
		this.hasParent = hasParent;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public EmpStructParent getParent() {
		return parent;
	}

	public void setParent(EmpStructParent parent) {
		this.parent = parent;
	}
	

	public CommonID getCommID() {
		return commID;
	}

	public void setCommID(CommonID commID) {
		this.commID = commID;
	}


	public List<EmpStructChild> getChildren() {
		return children;
	}


	public void setChildren(List<EmpStructChild> children) {
		this.children = children;
	}
	
	public void addChild(EmpStructChild child) {
		if(children == null) {
			children =new ArrayList<EmpStructChild>();
		}else {
			children.add(child);
			child.setSubParent(this);
		}
	}

}
