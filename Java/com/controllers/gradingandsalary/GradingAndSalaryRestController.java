package com.controllers.gradingandsalary;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.models.gradingandsalary.GradingAndSalaryModel;
import com.service.gradingandsalary.GradingAndSalaryService;

@RestController
@RequestMapping("/GradingStruct")
public class GradingAndSalaryRestController {

	@Autowired
	GradingAndSalaryService GradingAndSalaryService;
	
	@RequestMapping(value = { "/addGradingAndSalary" }, method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addGradingAndSalary(@RequestBody GradingAndSalaryModel GradingAndSalaryModel) throws Exception {	
		GradingAndSalaryService.addGradingAndSalary(GradingAndSalaryModel);
	}
	
	@RequestMapping(value = { "/getGradingAndSalary" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public GradingAndSalaryModel getGradingAndSalary(@RequestParam("grade") String grade) {
		return GradingAndSalaryService.getGradingAndSalary(grade);	
	}
	
	@RequestMapping(value = { "/getAllGradingAndSalary" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<GradingAndSalaryModel> getAllGradingAndSalary() {
		return GradingAndSalaryService.getAllGradingAndSalary();	
	}
	
	@RequestMapping(value = { "/updateGradingAndSalary" }, method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void updateGradingAndSalary(@RequestBody GradingAndSalaryModel GradingAndSalaryModel) throws ParseException {
		GradingAndSalaryService.updateGradingAndSalaryData(GradingAndSalaryModel);
	}
	
	@RequestMapping(value = { "/deleteGradingAndSalary" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteEmployeeStructure(@RequestParam("grade") String grade) {
		GradingAndSalaryService.deleteGradingAndSalary(grade);
	}
	
	@RequestMapping(value = { "/delemitGradingAndSalary" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void delemitGradingAndSalary(@RequestParam("grade") String grade,@RequestParam("endDate") String endDate) throws ParseException {
		GradingAndSalaryService.delimitGradingAndSalary(grade, endDate);
	}
	
	@RequestMapping(value = { "/copyGradingSalary" }, method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void copyEmployeeStructure(@RequestBody GradingAndSalaryModel GradingAndSalaryModel,@RequestParam("grade") String grade) throws ParseException, Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String todayDate=dtf.format(now);
		GradingAndSalaryService.delimitGradingAndSalary(grade, todayDate);
		GradingAndSalaryService.addGradingAndSalary(GradingAndSalaryModel);		
	}
}
