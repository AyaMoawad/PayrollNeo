package com.entities.payrollstruct;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="payroll_struct")
public class PayrollStruct {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="payroll_id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "commonID_id")
	private PayrollStructCommId commID;
	
	@Column(name="`interval`")
	private String interval;
	
	@Column(name="country")
	private String country;;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="company")
	private String company;
	
	@Column(name="tax_settlement")
	private String taxSettlement;
	
	@Column(name="payroll_valuation")
	private String payrollValuation;
	
	@Column(name="noOfFixedDays")
	private int noOfFixedDays;
	
	
	public PayrollStruct() {
		
	}

	public PayrollStruct(int id, PayrollStructCommId commID, String interval, String country, String currency,
			String company, String taxSettlement, String payrollValuation, int noOfFixedDays) {
		this.id = id;
		this.commID = commID;
		this.interval = interval;
		this.country = country;
		this.currency = currency;
		this.company = company;
		this.taxSettlement = taxSettlement;
		this.payrollValuation = payrollValuation;
		this.noOfFixedDays = noOfFixedDays;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PayrollStructCommId getCommID() {
		return commID;
	}

	public void setCommID(PayrollStructCommId commID) {
		this.commID = commID;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTaxSettlement() {
		return taxSettlement;
	}

	public void setTaxSettlement(String taxSettlement) {
		this.taxSettlement = taxSettlement;
	}

	public String getPayrollValuation() {
		return payrollValuation;
	}

	public void setPayrollValuation(String payrollValuation) {
		this.payrollValuation = payrollValuation;
	}

	public int getNoOfFixedDays() {
		return noOfFixedDays;
	}

	public void setNoOfFixedDays(int noOfFixedDays) {
		this.noOfFixedDays = noOfFixedDays;
	}

	
	

}
