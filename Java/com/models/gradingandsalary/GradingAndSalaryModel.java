package com.models.gradingandsalary;

public class GradingAndSalaryModel {

	private String startDate;
	private String endDate;
	private String grade;
	private String level;
	private String min;
	private String mid;
	private String max;
	private String basicSalary;//payTypeCode
	
	public GradingAndSalaryModel() {	}

	public GradingAndSalaryModel(String startDate, String endDate, String grade, String level, String min, String mid,
			String max, String basicSalary) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.grade = grade;
		this.level = level;
		this.min = min;
		this.mid = mid;
		this.max = max;
		this.basicSalary = basicSalary;
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(String basicSalary) {
		this.basicSalary = basicSalary;
	}

	
	
}
