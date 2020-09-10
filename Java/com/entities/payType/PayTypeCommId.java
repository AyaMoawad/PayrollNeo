package com.entities.payType;

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

import org.hibernate.annotations.GenericGenerator;

import com.entities.companyStruct.CompanyCommonID;
import com.entities.empStruct.CommonID;

@Entity
@Table(name = "paytype_commId")
public class PayTypeCommId {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name ="paytype_commId_id")
	private int id;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="code")
	private String code;
	
	@Column(name="name")
	private String name;
	
	@NotNull(message = "is required")
	@Column(name="delimited")
	private int deleted;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable(
			name="emp_paytype_assignment",
			joinColumns=@JoinColumn(name="paytype_commId_id"),
			inverseJoinColumns=@JoinColumn(name="commonID_id")			
			)
	private List<CommonID> employees;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable(
			name="company_paytype_assignment",
			joinColumns=@JoinColumn(name="paytype_commId_id"),
			inverseJoinColumns=@JoinColumn(name="companyCommonID_id")			
			)
	private List<CompanyCommonID> companies; 	
	
	
	public PayTypeCommId() {
	}

	public PayTypeCommId(Date startDate, Date endDate, String code, String name) {
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

	public List<CommonID> getEmployees() {
		return employees;
	}

	public void setEmployees(List<CommonID> employees) {
		this.employees = employees;
	}
	
	public void addEmployee(CommonID employee) {
		if(employees == null) {
			employees =new ArrayList<CommonID>();
		}
		employees.add(employee);
		
	}
	
	public List<CompanyCommonID> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyCommonID> companies) {
		this.companies = companies;
	}
	
	public void addCompanies(CompanyCommonID company) {
		if(companies == null) {
			companies =new ArrayList<CompanyCommonID>();
		}
		companies.add(company);
		
	}

}
