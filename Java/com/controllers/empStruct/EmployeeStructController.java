package com.controllers.empStruct;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.models.empStuct.EmployeeStructAssignmentModel;
import com.models.empStuct.EmployeeStructModel;
import com.rest.errorhandling.NotFoundException;
import com.services.empStruct.EmployeeStructService;

@RestController
@RequestMapping("/employeeStructure")
public class EmployeeStructController {

	@Autowired
	EmployeeStructService empService;

	@RequestMapping(value = {
			"/addEmployeeStructure" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addEmployeeStructure(@RequestBody List<EmployeeStructModel> employeeModel) throws Exception {
		for (Integer i = 0; i < employeeModel.size(); i++) {
			empService.processTheIncommingModel(employeeModel.get(i));
		}

	}

	@RequestMapping(value = {
			"/chaningChildToSubParent" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void chaningChildToSubParent(@RequestBody List<EmployeeStructModel> employeeModel,
			@RequestParam("code") String code) throws Exception {
		empService.deleteChild(code);
		for (Integer i = 0; i < employeeModel.size(); i++) {
			empService.processTheIncommingModel(employeeModel.get(i));
		}

	}

	@RequestMapping(value = {
			"/addEmployeeStructureElement" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addEmployeeStructureElement(@RequestBody EmployeeStructModel employeeModel) throws Exception {
		empService.processTheIncommingModel(employeeModel);
	}

	@RequestMapping(value = {
			"/showEmployeeStructure" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> showEmployeeStructure(@RequestParam("code") String code) {
		Map<String, Object> myMap = new HashMap<String, Object>();
		List<EmployeeStructModel> myList = new ArrayList<EmployeeStructModel>();
		try {
			Boolean isParent = empService.isParent(code);
			Boolean isSub = empService.isSubParent(code);

			if (isParent) {
				myList.addAll(empService.getParentChain(code));
			} else if (isSub) {
				myList.addAll(empService.getSubParentChain(code));
			} else {
				myList.addAll(empService.getChildChain(code));
			}
			myMap.put("theChain", myList);

		} catch (Exception e) {
			myMap.put("theChain", null);
			e.printStackTrace();
		}

		return myMap;
	}

	@RequestMapping(value = {
			"/getAllTheEmployeeStructures" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getAllTheEmployeeStructures() {
		Map<String, Object> myMap = new HashMap<String, Object>();
		List<EmployeeStructModel> myList = new ArrayList<EmployeeStructModel>();
		try {
			myList.addAll(empService.getAllEmployeeStructure());
			myMap.put("theChain", myList);

		} catch (Exception e) {
			myMap.put("theChain", null);
			e.printStackTrace();
		}

		return myMap;
	}

	@RequestMapping(value = {
			"/getEmployeeStructureElement" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getEmployeeStructureElement(@RequestParam("code") String code) {
		Map<String, Object> myMap = new HashMap<String, Object>();
		EmployeeStructModel model = new EmployeeStructModel();
		Boolean isParent = empService.isParent(code);
		Boolean isSub = empService.isSubParent(code);

		try {
			if (isParent) {
				model = empService.getTheParent(code);
			} else if (isSub) {
				model = empService.getTheSubParent(code);
			} else {
				model = empService.getTheChild(code);
			}
			myMap.put("theModel", model);
		} catch (Exception e) {
			throw new NotFoundException("Not fount element");
		}
		return myMap;
	}

	@RequestMapping(value = {
			"/updateEmployeeStructure" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void updateEmployeeStructure(@RequestBody EmployeeStructModel employeeModel) throws ParseException {
		empService.updateEmployeeStructure(employeeModel);
	}

	@RequestMapping(value = {
			"/deleteEmployeeStructure" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteEmployeeStructure(@RequestParam("code") String code) {
		Boolean isParent = empService.isParent(code);
		Boolean isSub = empService.isSubParent(code);

		if (isParent) {
			empService.deleteParent(code);
		} else if (isSub) {
			empService.deleteSubParent(code);
		} else {
			empService.deleteChild(code);
		}

	}

	@RequestMapping(value = {
			"/delemitEmployeeStructure" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String delemitEmployeeStructure(@RequestParam("code") String code, @RequestParam("endDate") String endDate)
			throws ParseException {
		String isDelemited = "false";
		Boolean isParent = empService.isParent(code);
		Boolean isSub = empService.isSubParent(code);

		if (isParent) {
			empService.delmitParent(code, endDate);
		} else if (isSub) {
			empService.delmitSubParent(code, endDate);
		} else {
			empService.delmitChild(code, endDate);
		}
		isDelemited = "true";

		return isDelemited;
	}

	@RequestMapping(value = {
			"/copyEmployeeStructure" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void copyEmployeeStructure(@RequestBody List<EmployeeStructModel> employeeModel,
			@RequestParam("code") String code) throws ParseException, Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String todayDate = dtf.format(now);
		empService.delmitParent(code, todayDate);
		for (int i = 0; i < employeeModel.size(); i++) {
			empService.copyEmployeeStructure(employeeModel.get(i), todayDate);
		}
	}
	
	
	@RequestMapping(value = {
			"/assignPaytypeToEmployeeStruct" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void assignPaytypeToEmployeeStruct(@RequestBody List<EmployeeStructAssignmentModel> assignmentModel) throws Exception {
		for (Integer i = 0; i < assignmentModel.size(); i++) {
			empService.assignPaytypeToEmployeeStruct(assignmentModel.get(i).getCode(),assignmentModel.get(i).getPayTypeCodes());
		}

	}

	
	@RequestMapping(value = {
			"/getAllPaytypesAssignedToEmpStruct" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<EmployeeStructAssignmentModel> getAllPaytypesAssignedToEmpStruct(@RequestBody List<String> empStructCodes)
			throws Exception {
		List<EmployeeStructAssignmentModel> paytypeCodes = new ArrayList<EmployeeStructAssignmentModel>();
		for (Integer i = 0; i < empStructCodes.size(); i++) {
			EmployeeStructAssignmentModel model =new EmployeeStructAssignmentModel();
			String empCode = empStructCodes.get(i);
			List<String> paytypes=empService.getAllPaytypesAssignedToEmpStruct(empCode);
			model.setCode(empCode);
			model.setPayTypeCodes(paytypes);
			paytypeCodes.add(model);
		}
		return paytypeCodes;
	}
	

	@RequestMapping(value = {
			"/removePaytypeFromEmpStuct" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void removePaytypeFromEmpStuct(@RequestBody List<EmployeeStructAssignmentModel> unassignmentModel) throws Exception {
		for (Integer i = 0; i < unassignmentModel.size(); i++) {
			empService.removePaytypeFromEmpStuct(unassignmentModel.get(i).getCode(),unassignmentModel.get(i).getPayTypeCodes());
		}
	}

}
