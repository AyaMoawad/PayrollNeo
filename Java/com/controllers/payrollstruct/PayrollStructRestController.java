package com.controllers.payrollstruct;

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

import com.models.payrollstruct.PayrollStructModel;
import com.service.payrollstruct.PayrollStructService;

@RestController
@RequestMapping("/PayrollStruct")
public class PayrollStructRestController {

	@Autowired
	PayrollStructService PayrollStructService;
	
	@RequestMapping(value = { "/addPayrollStruct" }, method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addPayrollStruct(@RequestBody PayrollStructModel PayrollStructModel) throws Exception {	
		PayrollStructService.addPayrollStruct(PayrollStructModel);
	}
	
	@RequestMapping(value = { "/getPayrollStruct" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PayrollStructModel getPayrollStruct(@RequestParam("code") String code) {
		return PayrollStructService.getPayrollStruct(code);	
	}
	
	@RequestMapping(value = { "/getAllPayrollStruct" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<PayrollStructModel> getAllPayrollStruct() {
		return PayrollStructService.getAllPayrollStruct();	
	}
	
	@RequestMapping(value = { "/updatePayrollStruct" }, method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void updatePayrollStruct(@RequestBody PayrollStructModel PayrollStructModel) throws ParseException {
		PayrollStructService.updatePayrollStructData(PayrollStructModel);
	}
	
	@RequestMapping(value = { "/deletePayrollStruct" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteEmployeeStructure(@RequestParam("code") String code) {
		PayrollStructService.deletePayrollStruct(code);
	}
	
	@RequestMapping(value = { "/delemitPayrollStruct" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void delemitPayrollStruct(@RequestParam("code") String code,@RequestParam("endDate") String endDate) throws ParseException {
		PayrollStructService.delimitPayrollStruct(code, endDate);
	}
	
	@RequestMapping(value = { "/copyPayrollStruct" }, method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void copyEmployeeStructure(@RequestBody PayrollStructModel PayrollStructModel,@RequestParam("code") String code) throws ParseException, Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String todayDate=dtf.format(now);
		PayrollStructService.delimitPayrollStruct(code, todayDate);
		PayrollStructService.addPayrollStruct(PayrollStructModel);		
	}
}
