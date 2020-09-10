package com.models.empStuct;

import java.util.List;

public class EmployeeStructAssignmentModel {
	
	private String code; //theEmpStructCode XD
	private List<String> payTypeCodes;
	
	public EmployeeStructAssignmentModel() {}
	
	public EmployeeStructAssignmentModel(String code, List<String> payTypeCodes) {
		this.code = code;
		this.payTypeCodes = payTypeCodes;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getPayTypeCodes() {
		return payTypeCodes;
	}
	public void setPayTypeCodes(List<String> payTypeCodes) {
		this.payTypeCodes = payTypeCodes;
	}
	
	
	
}
