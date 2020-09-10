package com.dao.companyStruct;

import java.util.List;

import com.entities.companyStruct.CompanyCommonID;
import com.entities.companyStruct.CompanyStructChild;
import com.entities.companyStruct.CompanyStructParent;
import com.entities.companyStruct.CompanyStructSubparent;

public interface CompanyStructDAO {
	
	//add parent at the parent table
	public void addParent(CompanyStructParent parent);
	
	//get the parent/subParent and use addSubParent method then save it to DB
	public void addSubParentToParent(CompanyStructSubparent subParent , String parentCode);
	public void addSubParentToSubParent(CompanyStructSubparent subParent);
	
	//get the parent/subParent and use addChild method then save it to DB
	public void addChildToParent(CompanyStructChild child,String parentCode );
	public void addChildToSubParent(CompanyStructChild child, String parentCode);
	
	//get an Company by the unique code from specific table
	public CompanyStructParent getParent(String code);
	public CompanyStructSubparent getSubParent(String code);
	public CompanyStructChild getChild(String code);
	
	//Select all from subParent table that has parentCode of that parentCode
	public List<CompanyStructSubparent> getSubParentsOfSubParents(String parentCode);
	
	//get the parent then use the getSubParents method 
	public List<CompanyStructSubparent> getSubParentsOfParent(String parentCode);
	
	//try to find out if this Company is a parent/subParent
	public Boolean isParent(String parentCode);
	public Boolean isSubParent(String parentCode);

	public CompanyStructParent getParentById(int id);
	public CompanyStructChild getChildById(int id);
	public CompanyStructSubparent getSubById(int id);

	public String deleteParent(String code);
	public String deleteSubParent(String code);
	public String deleteChild(String code);

	public List<CompanyCommonID> getAllCompanyStructure();

	public CompanyCommonID getCompanyStruct(String companyStructCode);
	

}
