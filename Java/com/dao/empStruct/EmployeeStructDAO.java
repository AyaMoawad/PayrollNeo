package com.dao.empStruct;

import java.util.List;

import com.entities.empStruct.CommonID;
import com.entities.empStruct.EmpStructChild;
import com.entities.empStruct.EmpStructParent;
import com.entities.empStruct.EmpStructSubparent;

public interface EmployeeStructDAO {
	
	//add parent at the parent table
	public void addParent(EmpStructParent parent);
	
	//get the parent/subParent and use addSubParent method then save it to DB
	public void addSubParentToParent(EmpStructSubparent subParent , String parentCode);
	public void addSubParentToSubParent(EmpStructSubparent subParent);
	
	//get the parent/subParent and use addChild method then save it to DB
	public void addChildToParent(EmpStructChild child,String parentCode );
	public void addChildToSubParent(EmpStructChild child, String parentCode);
	
	//get an Employee by the unique code from specific table
	public EmpStructParent getParent(String code);
	public EmpStructSubparent getSubParent(String code);
	public EmpStructChild getChild(String code);
	public CommonID getEmpStruct(String empStructCode);
	
	//Select all from subParent table that has parentCode of that parentCode
	public List<EmpStructSubparent> getSubParentsOfSubParents(String parentCode);
	
	//get the parent then use the getSubParents method 
	public List<EmpStructSubparent> getSubParentsOfParent(String parentCode);
	
	//try to find out if this employee is a parent/subParent
	public Boolean isParent(String parentCode);
	public Boolean isSubParent(String parentCode);

	public EmpStructParent getParentById(int id);
	public EmpStructChild getChildById(int id);
	public EmpStructSubparent getSubById(int id);

	public void deleteParent(String code);
	public void deleteSubParent(String code);
	public void deleteChild(String code);

	public List<CommonID> getAllEmployeeStructure();
	

}
