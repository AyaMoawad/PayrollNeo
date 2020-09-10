package com.models.empStuct;


public class EmployeeStructModel {
	
	private String startDate;
	private String endDate;
	private String code;
	private String name;
	private Boolean hasParent;
	private String parentCode;
	private Boolean hasChild;
	
	public EmployeeStructModel() {
		
	}
	
	public EmployeeStructModel(String startDate, String endDate, String code, String name, Boolean hasParent,
			String parentCode, Boolean hasChild) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.code = code;
		this.name = name;
		this.hasParent = hasParent;
		this.parentCode = parentCode;
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

	public Boolean getHasParent() {
		return hasParent;
	}

	public void setHasParent(Boolean hasParent) {
		this.hasParent = hasParent;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	
	
	
	
}
