package com.services.empStruct;

import java.text.ParseException;
import java.util.List;

import com.entities.empStruct.EmpStructChild;
import com.entities.empStruct.EmpStructParent;
import com.entities.empStruct.EmpStructSubparent;
import com.models.empStuct.EmployeeStructModel;

public interface EmployeeStructService {

	public void processTheIncommingModel(EmployeeStructModel employee) throws Exception;

	public EmployeeStructModel getTheParent(String code);

	public List<EmployeeStructModel> getTheSubParentsOfParent(String code);

	public List<EmployeeStructModel> getTheChildrenOfSubParent(String subCode);

	public List<EmployeeStructModel> getTheChildrenOfParent(String parentCode);

	public EmpStructParent getParent(String code);

	public EmpStructSubparent getSubParent(String code);

	public Boolean isParent(String parentCode);

	public Boolean isSubParent(String parentCode);

	public EmployeeStructModel getParentOfSub(EmpStructSubparent sub);

	public EmployeeStructModel getParentOfChild(EmpStructChild child);

	public List<EmployeeStructModel> getTheSubParentsOfSubParent(String parentCode);

	public List<EmployeeStructModel> getParentChain(String code);

	public List<EmployeeStructModel> getSubParentChain(String code);

	public EmployeeStructModel getTheSubParent(String code);

	public List<EmployeeStructModel> getChildChain(String code);

	public EmployeeStructModel getTheChild(String code);

	public void updateEmployeeStructure(EmployeeStructModel employee) throws ParseException;

	public void deleteParent(String code);
	public void deleteSubParent(String code);
	public void deleteChild(String code);

	public List<EmpStructSubparent> getSubOfSub(String code);

	public void delmitParent(String code, String endDate) throws ParseException;
	public void delmitSubParent(String code, String endDate) throws ParseException;
	public void delmitChild(String code, String endDate) throws ParseException;

	public void copyEmployeeStructure(EmployeeStructModel employeeStructModel, String todayDate) throws ParseException, Exception;

	public List<EmployeeStructModel> getAllEmployeeStructure();
	
	public void assignPaytypeToEmployeeStruct(String empStructCode , List<String> paytypeCodes);
	public List<String> getAllPaytypesAssignedToEmpStruct(String empStructCode);
	public void removePaytypeFromEmpStuct(String empStructCode,List<String> unassignedPaytypes);


}
