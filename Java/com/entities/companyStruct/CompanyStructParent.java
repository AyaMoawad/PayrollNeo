package com.entities.companyStruct;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="company_struct_parent")
public class CompanyStructParent {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="companyParent_id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "companyCommonID_id")
	private CompanyCommonID commID;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parent",
			cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<CompanyStructSubparent> subParents;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parent"
				,cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<CompanyStructChild> children;
	
	
	public CompanyStructParent() {
		
	}

	public CompanyStructParent(CompanyCommonID commID) {
		this.commID = commID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CompanyCommonID getCommID() {
		return commID;
	}

	public void setCommID(CompanyCommonID commID) {
		this.commID = commID;
	}
	
	public List<CompanyStructSubparent> getSubParents() {
		return subParents;
	}

	public void setSubParents(List<CompanyStructSubparent> subParents) {
		this.subParents = subParents;
	}

	public void addSubParent(CompanyStructSubparent subParent) {
		if(subParents == null) {
			subParents = new ArrayList<CompanyStructSubparent>();
		}else {
			subParents.add(subParent);
			subParent.setParent(this);
		}
		
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
			child.setParent(this);
		}
	}
}
