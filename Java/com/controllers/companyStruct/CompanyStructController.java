package com.controllers.companyStruct;

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

import com.models.companyStruct.CompanyStructAssignmentModel;
import com.models.companyStruct.CompanyStructModel;
import com.services.companyStruct.CompanyStructService;

@RestController
@RequestMapping("/companyStructure")
public class CompanyStructController {

	@Autowired
	CompanyStructService companyService;

	@RequestMapping(value = {
			"/addCompanyStructure" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addCompanyStructure(@RequestBody List<CompanyStructModel> companyModel) throws Exception {
		for (Integer i = 0; i < companyModel.size(); i++) {
			companyService.processTheIncommingModel(companyModel.get(i));
		}
	}

	@RequestMapping(value = {
			"/chaningChildToSubParent" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void chaningChildToSubParent(@RequestBody List<CompanyStructModel> companyModel,
			@RequestParam("code") String code) throws Exception {
		companyService.deleteChild(code);
		for (Integer i = 0; i < companyModel.size(); i++) {
			companyService.processTheIncommingModel(companyModel.get(i));
		}

	}

	@RequestMapping(value = {
			"/addCompanyStructureElement" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addCompanyStructureElement(@RequestBody CompanyStructModel companyModel) throws Exception {
		companyService.processTheIncommingModel(companyModel);
	}

	@RequestMapping(value = {
			"/showCompanyStructure" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> showCompanyStructure(@RequestParam("code") String code) {
		Map<String, Object> myMap = new HashMap<String, Object>();
		List<CompanyStructModel> myList = new ArrayList<CompanyStructModel>();
		try {
			Boolean isParent = companyService.isParent(code);
			Boolean isSub = companyService.isSubParent(code);
			if (isParent) {
				myList.addAll(companyService.getParentChain(code));
			} else if (isSub) {
				myList.addAll(companyService.getSubParentChain(code));
			} else {
				myList.addAll(companyService.getChildChain(code));
			}
			myMap.put("theChain", myList);

		} catch (Exception e) {
			myMap.put("theChain", null);
		}

		return myMap;
	}

	@RequestMapping(value = {
			"/getAllTheCompanyStructures" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getAllTheCompanyStructures() {
		Map<String, Object> myMap = new HashMap<String, Object>();
		List<CompanyStructModel> myList = new ArrayList<CompanyStructModel>();
		try {
			myList.addAll(companyService.getAllCompanyStructure());
			myMap.put("theChain", myList);

		} catch (Exception e) {
			myMap.put("theChain", null);
			e.printStackTrace();
		}

		return myMap;
	}

	@RequestMapping(value = {
			"/getCompanyStructureElement" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getCompanyStructureElement(@RequestParam("code") String code) {
		Map<String, Object> myMap = new HashMap<String, Object>();
		CompanyStructModel model = new CompanyStructModel();
		Boolean isParent = companyService.isParent(code);
		Boolean isSub = companyService.isSubParent(code);
		if (isParent) {
			model = companyService.getTheParent(code);
		} else if (isSub) {
			model = companyService.getTheSubParent(code);
		} else {
			model = companyService.getTheChild(code);
		}
		myMap.put("theModel", model);

		return myMap;
	}

	@RequestMapping(value = {
			"/updateCompanyStructure" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void updateCompanyStructure(@RequestBody CompanyStructModel companyModel) throws ParseException {

		companyService.updateCompanyStructure(companyModel);

	}

	@RequestMapping(value = {
			"/deleteCompanyStructure" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteCompanyStructure(@RequestParam("code") String code) {
		Boolean isParent = companyService.isParent(code);
		Boolean isSub = companyService.isSubParent(code);
		if (isParent) {
			companyService.deleteParent(code);
		} else if (isSub) {
			companyService.deleteSubParent(code);
		} else {
			companyService.deleteChild(code);
		}
	}

	@RequestMapping(value = {
			"/delemitCompanyStructure" }, method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String delemitCompanyStructure(@RequestParam("code") String code, @RequestParam("endDate") String endDate)
			throws ParseException {
		String isDelemited = "false";
		Boolean isParent = companyService.isParent(code);
		Boolean isSub = companyService.isSubParent(code);
		if (isParent) {
			companyService.delmitParent(code, endDate);
		} else if (isSub) {
			companyService.delmitSubParent(code, endDate);
		} else {
			companyService.delmitChild(code, endDate);
		}
		isDelemited = "true";

		return isDelemited;
	}

	@RequestMapping(value = {
			"/copyCompanyStructure" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void copyCompanyStructure(@RequestBody List<CompanyStructModel> companyModel,
			@RequestParam("code") String code) throws Exception {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String todayDate = dtf.format(now);
		companyService.delmitParent(code, todayDate);

		for (int i = 0; i < companyModel.size(); i++) {
			companyService.copyCompanyStructure(companyModel.get(i), todayDate);
		}
	}

	@RequestMapping(value = {
			"/assignPaytypeToCompanyStruct" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void assignPaytypeToCompanyStruct(@RequestBody List<CompanyStructAssignmentModel> assignmentModel)
			throws Exception {
		for (Integer i = 0; i < assignmentModel.size(); i++) {
			companyService.assignPaytypeToCompanyStruct(assignmentModel.get(i).getCode(),
					assignmentModel.get(i).getPayTypeCodes());
		}

	}

	@RequestMapping(value = {
			"/getAllPaytypesAssignedToCompanyStruct" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<CompanyStructAssignmentModel> getAllPaytypesAssignedToCompanyStruct(
			@RequestBody List<String> companyStructCodes) throws Exception {
		List<CompanyStructAssignmentModel> paytypeCodes = new ArrayList<CompanyStructAssignmentModel>();
		for (Integer i = 0; i < companyStructCodes.size(); i++) {
			CompanyStructAssignmentModel model = new CompanyStructAssignmentModel();
			String companyCode = companyStructCodes.get(i);
			List<String> paytypes = companyService.getAllPaytypesAssignedToCompanyStruct(companyCode);
			model.setCode(companyCode);
			model.setPayTypeCodes(paytypes);
			paytypeCodes.add(model);
		}
		return paytypeCodes;
	}

	@RequestMapping(value = {
			"/removePaytypeFromCompanyStuct" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void removePaytypeFromCompanyStuct(@RequestBody List<CompanyStructAssignmentModel> unassignmentModel)
			throws Exception {
		for (Integer i = 0; i < unassignmentModel.size(); i++) {
			companyService.removePaytypeFromCompanyStuct(unassignmentModel.get(i).getCode(),
					unassignmentModel.get(i).getPayTypeCodes());
		}
	}
}
