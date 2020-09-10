package com.models.payrollstruct;

public class PayrollStructModel {
	private String startDate;
	private String endDate;
	private String code;
	private String name;
	private String interval;
	private String country;
	private String currency;
	private String company;
	private String taxSettlement;
	private String payrollValuation;
	private String noOfDays;
	
	public PayrollStructModel() {	}

	public PayrollStructModel(String startDate, String endDate, String code, String name, String interval,
			String country, String currency, String company, String taxSettlement, String payrollValuation,
			String noOfDays) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.code = code;
		this.name = name;
		this.interval = interval;
		this.country = country;
		this.currency = currency;
		this.company = company;
		this.taxSettlement = taxSettlement;
		this.payrollValuation = payrollValuation;
		this.noOfDays = noOfDays;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
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

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getNoOfDays() {
		return noOfDays;
	}


	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}
	

}
