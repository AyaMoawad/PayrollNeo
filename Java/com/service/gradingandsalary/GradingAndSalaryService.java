package com.service.gradingandsalary;

import java.text.ParseException;
import java.util.List;

import com.models.gradingandsalary.GradingAndSalaryModel;

public interface GradingAndSalaryService {
	public void addGradingAndSalary(GradingAndSalaryModel GradingAndSalaryModel) throws ParseException;
	public GradingAndSalaryModel getGradingAndSalary(String grade);
	public void deleteGradingAndSalary(String grade);
	public void delimitGradingAndSalary(String grade, String endDate) throws ParseException;
	public void updateGradingAndSalaryData(GradingAndSalaryModel GradingAndSalaryModel) throws ParseException;
	public List<GradingAndSalaryModel> getAllGradingAndSalary();
}
