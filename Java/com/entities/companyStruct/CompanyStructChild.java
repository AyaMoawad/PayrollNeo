package com.entities.companyStruct;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name ="company_struct_child")
public class CompanyStructChild {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="companyChild_id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "companyCommonID_id")
	private CompanyCommonID commID;
	

	@ManyToOne(cascade = {CascadeType.PERSIST,
			  CascadeType.MERGE,
			  CascadeType.REFRESH,
			  CascadeType.DETACH })
	@JoinColumn(name = "companyParent_id")
	private CompanyStructParent parent;
	
	
	@ManyToOne(cascade = {CascadeType.PERSIST,
			  CascadeType.MERGE,
			  CascadeType.REFRESH,
			  CascadeType.DETACH })
	@JoinColumn(name="companySubparent_id")
	private CompanyStructSubparent subParent;
	
	
	public CompanyStructChild() {
		// TODO Auto-generated constructor stub
	}
	

	public CompanyStructChild(CompanyCommonID commID) {
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

	public CompanyStructParent getParent() {
		return parent;
	}

	public void setParent(CompanyStructParent parent) {
		this.parent = parent;
	}

	public CompanyStructSubparent getSubParent() {
		return subParent;
	}

	public void setSubParent(CompanyStructSubparent subParent) {
		this.subParent = subParent;
	}

	
}
