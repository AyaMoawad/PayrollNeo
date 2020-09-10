package com.entities.empStruct;

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
@Table(name ="emp_struct_child")
public class EmpStructChild {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="child_id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "commonID_id")
	private CommonID commID;
	

	@ManyToOne(cascade = {CascadeType.PERSIST,
			  CascadeType.MERGE,
			  CascadeType.REFRESH,
			  CascadeType.DETACH })
	@JoinColumn(name = "parent_id")
	private EmpStructParent parent;
	
	public EmpStructParent getParent() {
		return parent;
	}


	public void setParent(EmpStructParent parent) {
		this.parent = parent;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST,
			  CascadeType.MERGE,
			  CascadeType.REFRESH,
			  CascadeType.DETACH })
	@JoinColumn(name="subparent_id")
	private EmpStructSubparent subParent;
	
	public EmpStructChild() {
		// TODO Auto-generated constructor stub
	}
	
	
	public EmpStructChild(CommonID commID) {
		this.commID = commID;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CommonID getCommID() {
		return commID;
	}

	public void setCommID(CommonID commID) {
		this.commID = commID;
	}


	public EmpStructSubparent getSubParent() {
		return subParent;
	}

	public void setSubParent(EmpStructSubparent subParent) {
		this.subParent = subParent;
	}
	
}
