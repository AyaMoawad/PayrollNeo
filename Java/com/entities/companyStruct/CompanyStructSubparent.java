package com.entities.companyStruct;

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
@Table(name="company_struct_subparent")
public class CompanyStructSubparent {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="companySubparent_id")
	private int id;
	
	@Column(name = "has_parent")
	private int hasParent;
	
	@Column(name="parent_code")
	private String parentCode;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "companyCommonID_id")
	private CompanyCommonID commID;
	
	@ManyToOne(cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.REFRESH,
					CascadeType.DETACH })
	@JoinColumn(name = "companyParent_id")
	private CompanyStructParent parent;
	
	@JsonIgnore
	@OneToMany(mappedBy = "subParent" ,
			cascade = CascadeType.ALL,
			  fetch = FetchType.EAGER)
	private List<CompanyStructChild> children;
	
	public CompanyStructSubparent() {
		
	}
	
	
	public CompanyStructSubparent(int hasParent, String parentCode, CompanyCommonID commID) {
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

	public CompanyStructParent getParent() {
		return parent;
	}

	public void setParent(CompanyStructParent parent) {
		this.parent = parent;
	}
	

	public CompanyCommonID getCommID() {
		return commID;
	}

	public void setCommID(CompanyCommonID commID) {
		this.commID = commID;
	}


	public List<CompanyStructChild> getChildren() {
		return children;
	}


	public void setChildren(List<CompanyStructChild> children) {
		this.children = children;
	}
	
	public void addChild(CompanyStructChild child) {
		if(children == null) {
			children =new ArrayList<CompanyStructChild>();
		}else {
			children.add(child);
			child.setSubParent(this);
		}
	}

}
