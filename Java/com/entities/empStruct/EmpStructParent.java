package com.entities.empStruct;

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
@Table(name="emp_struct_parent")
public class EmpStructParent {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="parent_id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "commonID_id")
	private CommonID commID;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parent",
			cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EmpStructSubparent> subParents;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parent"
				,cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EmpStructChild> children;
	
	
	public EmpStructParent() {
		
	}

	public EmpStructParent(CommonID commID) {
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
	
	public List<EmpStructSubparent> getSubParents() {
		return subParents;
	}

	public void setSubParents(List<EmpStructSubparent> subParents) {
		this.subParents = subParents;
	}

	public void addSubParent( EmpStructSubparent subParent) {
		if(subParents == null) {
			subParents = new ArrayList<EmpStructSubparent>();
		}else {
			subParents.add(subParent);
			subParent.setParent(this);
		}
		
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
			child.setParent(this);
		}
	}
}
