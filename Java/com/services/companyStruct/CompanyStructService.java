package com.services.companyStruct;

import java.text.ParseException;
import java.util.List;

import com.entities.companyStruct.CompanyStructChild;
import com.entities.companyStruct.CompanyStructParent;
import com.entities.companyStruct.CompanyStructSubparent;
import com.models.companyStruct.CompanyStructModel;

public interface CompanyStructService {

	public void processTheIncommingModel(CompanyStructModel employee) throws Exception;

	public CompanyStructModel getTheParent(String code);

	public List<CompanyStructModel> getTheSubParentsOfParent(String code);

	public List<CompanyStructModel> getTheChildrenOfSubParent(String subCode);

	public List<CompanyStructModel> getTheChildrenOfParent(String parentCode);

	public CompanyStructParent getParent(String code);

	public CompanyStructSubparent getSubParent(String code);

	public Boolean isParent(String parentCode);

	public Boolean isSubParent(String parentCode);

	public CompanyStructModel getParentOfSub(CompanyStructSubparent sub);

	public CompanyStructModel getParentOfChild(CompanyStructChild child);

	public List<CompanyStructModel> getTheSubParentsOfSubParent(String parentCode);

	public List<CompanyStructModel> getParentChain(String code);

	public List<CompanyStructModel> getSubParentChain(String code);

	public CompanyStructModel getTheSubParent(String code);

	public List<CompanyStructModel> getChildChain(String code);

	public CompanyStructModel getTheChild(String code);

	public void updateCompanyStructure(CompanyStructModel employee)throws ParseException ;

	public void deleteParent(String code);
	public void deleteSubParent(String code);
	public void deleteChild(String code);

	public List<CompanyStructSubparent> getSubOfSub(String code);

	public void delmitParent(String code, String endDate) throws ParseException;
	public void delmitSubParent(String code, String endDate) throws ParseException;
	public void delmitChild(String code, String endDate) throws ParseException;

	public void copyCompanyStructure(CompanyStructModel employeeStructModel, String todayDate) throws ParseException, Exception;

	public List<CompanyStructModel> getAllCompanyStructure();

	public void assignPaytypeToCompanyStruct(String code, List<String> payTypeCodes);

	public List<String> getAllPaytypesAssignedToCompanyStruct(String companyCode);

	public void removePaytypeFromCompanyStuct(String code, List<String> payTypeCodes);

}
