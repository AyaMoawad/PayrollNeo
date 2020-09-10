package com.entities.empStruct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.entities.payType.PayTypeCommId;

@Entity
@Table(name = "commonID")
public class CommonID {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name ="commonID_id")
	private int id;
	
	@NotNull(message = "is required !")
	@Column(name="start_date")
	private Date startDate;
	
	@NotNull(message = "is required !")
	@Column(name="end_date")
	private Date endDate;
	
	@NotNull(message = "is required !")
	@Size(min = 1 , max = 45, message = "is required !")
	@Column(name="code")
	private String code;
	
	@NotNull(message = "is required !")
	@Size(min = 1 , max = 45, message = "is required !")
	@Column(name="name")
	private String name;
	
	@NotNull(message = "is required")
	@Column(name="deleted")
	private int deleted;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable(
			name="emp_paytype_assignment",
			joinColumns=@JoinColumn(name="commonID_id"),
			inverseJoinColumns=@JoinColumn(name="paytype_commId_id")			
			)
	private List<PayTypeCommId> paytypes; 
	
	public CommonID() {
	}
	
	public CommonID(Date startDate, Date endDate, String code, String name) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.code = code;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDeleted() {
		return deleted;
	}
	
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public List<PayTypeCommId> getPaytypes() {
		return paytypes;
	}

	public void setPaytypes(List<PayTypeCommId> paytypes) {
		this.paytypes = paytypes;
	}
	
	public void addPaytype(PayTypeCommId paytype) {
		if(paytypes == null) {
			paytypes =new ArrayList<PayTypeCommId>();
		}
		paytypes.add(paytype);	
	}
}
