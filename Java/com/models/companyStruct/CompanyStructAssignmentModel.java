package com.models.companyStruct;

import java.util.List;

public class CompanyStructAssignmentModel {
	
	
	private String code; //theCompStructCode XD
	private List<String> payTypeCodes;
	
	public CompanyStructAssignmentModel() {}
	
	public CompanyStructAssignmentModel(String code, List<String> payTypeCodes) {
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
