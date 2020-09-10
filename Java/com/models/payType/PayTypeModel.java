package com.models.payType;

public class PayTypeModel {

	private String startDate;
	private String endDate;
	private String code;
	private String name;
	private String interval;
	private String type;
	private String inputValue;
	private String unit;
	private String taxes;
	
	public PayTypeModel() {	}

	public PayTypeModel(String startDate, String endDate, String code, String name, String interval, String type,
			String inputValue) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.code = code;
		this.name = name;
		this.interval = interval;
		this.type = type;
		this.inputValue = inputValue;
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

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}
	
	

}
