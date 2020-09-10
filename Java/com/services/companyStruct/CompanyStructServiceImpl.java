package com.services.companyStruct;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.companyStruct.CompanyStructDAO;
import com.dao.payType.PayTypeDAO;
import com.entities.companyStruct.CompanyCommonID;
import com.entities.companyStruct.CompanyStructChild;
import com.entities.companyStruct.CompanyStructParent;
import com.entities.companyStruct.CompanyStructSubparent;
import com.entities.payType.PayType;
import com.entities.payType.PayTypeCommId;
import com.models.companyStruct.CompanyStructModel;
import com.rest.errorhandling.NotFoundException;
import com.rest.errorhandling.UniqunessException;

@Service
public class CompanyStructServiceImpl implements CompanyStructService {

	@Autowired
	private CompanyStructDAO companyDAO;
	
	@Autowired
	private PayTypeDAO paytypeDAO;

	@Override
	@Transactional
	public void processTheIncommingModel(CompanyStructModel company) throws Exception {
		if (company.getName() != null && company.getCode() != null && company.getEndDate() != null
				&& company.getStartDate() != null) {

			// conversion of dates
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(company.getStartDate());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(company.getEndDate());

			// commonID data
			CompanyCommonID commId = new CompanyCommonID(startDate, endDate, company.getCode(), company.getName());
			commId.setDeleted(0);

			String parentCode = company.getParentCode();

			// check if this model has parent and type of his parent
			Boolean hisParentIsParent = companyDAO.isParent(parentCode);
			Boolean hisParentIsSubParent = companyDAO.isSubParent(parentCode);

			// Process the model to know if it is a parent/SubParent/Child
			if (!company.getHasParent() && company.getHasChild()) {
				// the model has no parent but has a child ==> save as parent
				CompanyStructParent parent = new CompanyStructParent(commId);
				try {
					companyDAO.addParent(parent);
				}catch(Exception e) {
					throw new UniqunessException("the parent code : "+parent.getCommID().getCode()+" is already used!");
				}
			} else if (company.getHasParent() && company.getHasChild() && hisParentIsParent) {
				// the model has parent also has child and his parent is parent ==> save as
				// subParent
				CompanyStructSubparent subParent = new CompanyStructSubparent(0, null, commId);
				try {
					companyDAO.addSubParentToParent(subParent, parentCode);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+subParent.getCommID().getCode()+" is already used!");
				}
			} else if (company.getHasParent() && company.getHasChild() && hisParentIsSubParent) {
				// the model has parent also has child and his parent is subParent ==> save as
				// subParent
				CompanyStructSubparent subParent = new CompanyStructSubparent(1, parentCode, commId);
				try {
					companyDAO.addSubParentToSubParent(subParent);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+subParent.getCommID().getCode()+" is already used!");
				}
			} else if (company.getHasParent() && !company.getHasChild() && hisParentIsParent) {

				// the model has parent and has no child and his parent is parent ==>save as
				// child to parent
				CompanyStructChild child = new CompanyStructChild(commId);
				
				try {
					companyDAO.addChildToParent(child, parentCode);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+child.getCommID().getCode()+" is already used!");
				}

			} else if (company.getHasParent() && !company.getHasChild() && hisParentIsSubParent) {

				// the model has parent and has no child and his parent is subParent ==>save as
				// child to subParent
				CompanyStructChild child = new CompanyStructChild(commId);
				try {
					companyDAO.addChildToSubParent(child, parentCode);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+child.getCommID().getCode()+" is already used!");
				}
			}
		} else if(company.getName() == null) {
			throw new UniqunessException("the company name is missing!");
		}else if(company.getCode() == null) {
			throw new UniqunessException("the company code is missing!");
		}else if(company.getStartDate() == null) {
			throw new UniqunessException("the company start date is missing!");
		}else if(company.getEndDate() == null) {
			throw new UniqunessException("the company end date is missing!");
		}else {
			throw new UniqunessException("ERROR! can't save/update the company structure");
		}
	}

	@Override
	@Transactional
	public List<CompanyStructModel> getTheSubParentsOfSubParent(String parentCode) {
		List<CompanyStructModel> listOfSubParents = new ArrayList<CompanyStructModel>();
		List<CompanyStructSubparent> subParents = companyDAO.getSubParentsOfSubParents(parentCode);
		if (subParents != null) {
			for (Integer i = 0; i < subParents.size(); i++) {
				// create a model
				CompanyStructModel model = new CompanyStructModel();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				String startDate = dateFormat.format(subParents.get(i).getCommID().getStartDate());
				String endDate = dateFormat.format(subParents.get(i).getCommID().getEndDate());

				model.setParentCode(parentCode);
				model.setHasChild(true);
				model.setHasParent(true);
				model.setStartDate(startDate);
				model.setEndDate(endDate);
				model.setCode(subParents.get(i).getCommID().getCode());
				model.setName(subParents.get(i).getCommID().getName());
				listOfSubParents.add(model);

				if (subParents.get(i).getChildren() != null) {
					listOfSubParents.addAll(getTheChildrenOfSubParent(subParents.get(i).getCommID().getCode()));
				}

				if (companyDAO.getSubParentsOfSubParents(subParents.get(i).getCommID().getCode()) != null) {
					listOfSubParents.addAll(getTheSubParentsOfSubParent(subParents.get(i).getCommID().getCode()));
				}
			}
		}
		return listOfSubParents;
	}

	@Override
	@Transactional
	public CompanyStructModel getTheParent(String code) {
		try {
			CompanyStructParent parent = companyDAO.getParent(code);
			CompanyStructModel model = new CompanyStructModel();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String startDate = dateFormat.format(parent.getCommID().getStartDate());
			String endDate = dateFormat.format(parent.getCommID().getEndDate());

			model.setHasChild(true);
			model.setHasParent(false);
			model.setParentCode(null);
			model.setStartDate(startDate);
			model.setEndDate(endDate);
			model.setCode(parent.getCommID().getCode());
			model.setName(parent.getCommID().getName());

			return model;
		}catch(Exception e) {
			throw new  NotFoundException("Cannot find parent with code: "+code);
		}
	}

	@Override
	@Transactional
	public CompanyStructModel getTheSubParent(String code) {
		try {
			CompanyStructSubparent subParent = companyDAO.getSubParent(code);
			CompanyStructModel model = new CompanyStructModel();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String startDate = dateFormat.format(subParent.getCommID().getStartDate());
			String endDate = dateFormat.format(subParent.getCommID().getEndDate());
			CompanyStructParent parent = subParent.getParent();
			model.setHasChild(true);
			model.setHasParent(true);
			if (parent != null) {
				model.setParentCode(parent.getCommID().getCode());
			} else {
				model.setParentCode(subParent.getParentCode());
			}
			model.setStartDate(startDate);
			model.setEndDate(endDate);
			model.setCode(subParent.getCommID().getCode());
			model.setName(subParent.getCommID().getName());

			return model;
		}catch(Exception e) {throw new  NotFoundException("Cannot find subParent with code: "+code);}
	}

	@Override
	@Transactional
	public CompanyStructModel getTheChild(String code) {
		try {
			CompanyStructChild child = companyDAO.getChild(code);
			CompanyStructModel model = new CompanyStructModel();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String startDate = dateFormat.format(child.getCommID().getStartDate());
			String endDate = dateFormat.format(child.getCommID().getEndDate());

			CompanyStructParent hisParentIsParent = child.getParent();
			CompanyStructSubparent hisParentIsSub = child.getSubParent();
			model.setHasChild(false);
			model.setHasParent(true);

			if (hisParentIsParent != null) {
				model.setParentCode(child.getParent().getCommID().getCode());
			} else if (hisParentIsSub != null) {
				model.setParentCode(child.getSubParent().getCommID().getCode());
			}

			model.setStartDate(startDate);
			model.setEndDate(endDate);
			model.setCode(child.getCommID().getCode());
			model.setName(child.getCommID().getName());

			return model;
		}catch(Exception e) {
			throw new  NotFoundException("Cannot find child with code: "+code);
		}
	}

	@Override
	@Transactional
	public List<CompanyStructModel> getTheSubParentsOfParent(String code) {
		CompanyStructParent parent ;
		try {parent = companyDAO.getParent(code);}catch(Exception e) {throw new NotFoundException("Cannot find parent with code:"+code);}
		List<CompanyStructSubparent> subParentsOfParent = parent.getSubParents();
		List<CompanyStructModel> listOfSubParents = new ArrayList<CompanyStructModel>();
		if (subParentsOfParent != null) {
			for (Integer i = 0; i < subParentsOfParent.size(); i++) {
				CompanyStructModel model = new CompanyStructModel();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				String startDate = dateFormat.format(subParentsOfParent.get(i).getCommID().getStartDate());
				String endDate = dateFormat.format(subParentsOfParent.get(i).getCommID().getEndDate());

				model.setHasParent(true);
				model.setParentCode(code);
				model.setHasChild(true);

				model.setStartDate(startDate);
				model.setEndDate(endDate);
				model.setCode(subParentsOfParent.get(i).getCommID().getCode());
				model.setName(subParentsOfParent.get(i).getCommID().getName());

				listOfSubParents.add(model);

			}
		}
		return listOfSubParents;
	}

	@Override
	@Transactional
	public List<CompanyStructModel> getTheChildrenOfSubParent(String subCode) {
		CompanyStructSubparent subParent ;
		try {subParent= companyDAO.getSubParent(subCode);}catch(Exception e) {throw new NotFoundException("Cannot find subParent with code:"+subCode);}
		List<CompanyStructChild> childrenOfSub = subParent.getChildren();
		List<CompanyStructModel> listOfChildren = new ArrayList<CompanyStructModel>();
		if (childrenOfSub != null) {
			for (Integer i = 0; i < childrenOfSub.size(); i++) {
				CompanyStructModel model = new CompanyStructModel();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				String startDate = dateFormat.format(childrenOfSub.get(i).getCommID().getStartDate());
				String endDate = dateFormat.format(childrenOfSub.get(i).getCommID().getEndDate());

				model.setHasParent(true);
				model.setParentCode(subCode);
				model.setHasChild(false);

				model.setStartDate(startDate);
				model.setEndDate(endDate);
				model.setCode(childrenOfSub.get(i).getCommID().getCode());
				model.setName(childrenOfSub.get(i).getCommID().getName());
				listOfChildren.add(model);

			}
		}

		return listOfChildren;

	}

	@Override
	@Transactional
	public List<CompanyStructModel> getTheChildrenOfParent(String parentCode) {
		CompanyStructParent parent ;
		try {parent = companyDAO.getParent(parentCode);}catch(Exception e) {throw new NotFoundException("Cannot find parent with code:"+parentCode);}
		
		List<CompanyStructChild> childrenOfParent = parent.getChildren();
		List<CompanyStructModel> listOfChildren = new ArrayList<CompanyStructModel>();
		if (childrenOfParent != null) {
			for (Integer i = 0; i < childrenOfParent.size(); i++) {
				CompanyStructModel model = new CompanyStructModel();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				String startDate = dateFormat.format(childrenOfParent.get(i).getCommID().getStartDate());
				String endDate = dateFormat.format(childrenOfParent.get(i).getCommID().getEndDate());

				model.setHasParent(true);
				model.setParentCode(parentCode);
				model.setHasChild(false);

				model.setStartDate(startDate);
				model.setEndDate(endDate);
				model.setCode(childrenOfParent.get(i).getCommID().getCode());
				model.setName(childrenOfParent.get(i).getCommID().getName());
				listOfChildren.add(model);

			}
		}
		return listOfChildren;

	}

	@Override
	@Transactional
	public CompanyStructModel getParentOfSub(CompanyStructSubparent sub) {
		CompanyStructModel parentOfSub;
		if (sub.getHasParent() == 0) {
			CompanyStructParent parent = sub.getParent();
			parentOfSub = (getTheParent(parent.getCommID().getCode()));
		} else {
			CompanyStructSubparent subParent ;
			try {subParent = companyDAO.getSubParent(sub.getParentCode());}catch(Exception e) {
				subParent=null;
			}
			parentOfSub = (getTheSubParent(subParent.getCommID().getCode()));
		}
		return parentOfSub;
	}

	@Override
	@Transactional
	public CompanyStructModel getParentOfChild(CompanyStructChild child) {
		CompanyStructModel parentOfChild = null;
		if (child.getParent() != null) {
			CompanyStructModel parent = getTheParent(child.getParent().getCommID().getCode());
			parentOfChild = parent;
		} else if (child.getSubParent() != null) {
			CompanyStructModel sub = getTheSubParent(child.getSubParent().getCommID().getCode());
			parentOfChild = sub;
		}

		return parentOfChild;
	}

	@Override
	@Transactional
	public CompanyStructParent getParent(String code) {
		try {
			CompanyStructParent parent = companyDAO.getParent(code);
			return parent;
		}catch(Exception e) {
			throw new NotFoundException("Cannot get parent with code:"+code+" is not saved");
		}
	}

	@Override
	@Transactional
	public CompanyStructSubparent getSubParent(String code) {
		try {
			CompanyStructSubparent subParent = companyDAO.getSubParent(code);
			return subParent;
		}catch(Exception e) {
			throw new NotFoundException("Cannot get parent with code:"+code+" is not saved");
		}
	}
	@Override
	@Transactional
	public Boolean isSubParent(String parentCode) {
		Boolean isSub = companyDAO.isSubParent(parentCode);
		return isSub;
	}
	@Override
	@Transactional
	public Boolean isParent(String parentCode) {
		Boolean isParent = companyDAO.isParent(parentCode);
		return isParent;
	}

	/*
	 * Getting The Chain
	 */
	@Override
	@Transactional
	public List<CompanyStructModel> getParentChain(String code) {
		List<CompanyStructModel> parentMap = new ArrayList<CompanyStructModel>();
		// getting parent
		CompanyStructModel parent = getTheParent(code);
		CompanyStructParent parentObject = getParent(code);
		List<CompanyStructModel> subOfParent = getTheSubParentsOfParent(parentObject.getCommID().getCode());
		List<CompanyStructModel> childrenOfParent = getTheChildrenOfParent(parentObject.getCommID().getCode());
		List<CompanyStructSubparent> subOfParentObject = parentObject.getSubParents();

		for (Integer i = 0; i < subOfParentObject.size(); i++) {
			List<CompanyStructModel> subOfSub = getTheSubParentsOfSubParent(
					subOfParentObject.get(i).getCommID().getCode());
			List<CompanyStructModel> childrenOfSubMap = getTheChildrenOfSubParent(
					subOfParentObject.get(i).getCommID().getCode());

			parentMap.addAll(childrenOfSubMap);
			parentMap.addAll(subOfSub);
		}
		parentMap.add(parent);
		parentMap.addAll(subOfParent);
		parentMap.addAll(childrenOfParent);

		return parentMap;
	}

	@Override
	@Transactional
	public List<CompanyStructModel> getSubParentChain(String code) {
		List<CompanyStructModel> subparentChainMap = new ArrayList<CompanyStructModel>();
		CompanyStructModel subParent = getTheSubParent(code);
		CompanyStructSubparent subObject;
		try {subObject = companyDAO.getSubParent(code);}catch(Exception e) {throw new NotFoundException("Cannot find subParent with code:" +code);}
		subparentChainMap.add(subParent);

		subparentChainMap.addAll(getTheChildrenOfSubParent(code));

		List<CompanyStructModel> subOfSub = getTheSubParentsOfSubParent(subObject.getCommID().getCode());
		subparentChainMap.addAll(subOfSub);

		while (subObject.getHasParent() != 0) {
			subparentChainMap.add(getParentOfSub(subObject));
			subObject = companyDAO.getSubParent(subObject.getParentCode());
		}
		subparentChainMap.add(getParentOfSub(subObject));

		return subparentChainMap;
	}

	@Override
	@Transactional
	public List<CompanyStructModel> getChildChain(String code) {
		List<CompanyStructModel> childChainMap = new ArrayList<CompanyStructModel>();

		// getting child
		CompanyStructModel child = getTheChild(code);
		childChainMap.add(child);
		CompanyStructChild childObject ;
		try {childObject = companyDAO.getChild(code);}catch(Exception e) {throw new NotFoundException("Cannot find child with code: "+code);}
		CompanyStructParent hisParent = childObject.getParent();
		CompanyStructSubparent hisSubParent = childObject.getSubParent();
		if (hisParent != null) {
			childChainMap.add(getTheParent(childObject.getParent().getCommID().getCode()));
		} else if (hisSubParent != null) {
			childChainMap.add(getTheSubParent(hisSubParent.getCommID().getCode()));
			while (hisSubParent.getHasParent() != 0) {
				childChainMap.add(getParentOfSub(hisSubParent));
				hisSubParent = companyDAO.getSubParent(hisSubParent.getParentCode());
			}
			childChainMap.add(getParentOfSub(hisSubParent));
		}

		return childChainMap;

	}

	/*
	 * process the update employeeStructure
	 * 
	 */
	@Override
	@Transactional
	public void updateCompanyStructure(CompanyStructModel company) throws ParseException {
		// process to check the model(parent/sub/child)
		if (company.getName() != null && company.getCode() != null && company.getEndDate() != null
				&& company.getStartDate() != null && company.getHasParent() != null
				&& company.getHasChild() != null) {
			// conversion of dates
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(company.getStartDate());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(company.getEndDate());
			String parentCode = company.getParentCode();
			// check if this model has parent and type of his parent
			Boolean hisParentIsParent = companyDAO.isParent(parentCode);
			Boolean hisParentIsSubParent = companyDAO.isSubParent(parentCode);

			if (!company.getHasParent() && company.getHasChild()) {
				// the model has no parent but has a child ==> update parent
				CompanyStructParent parent = companyDAO.getParent(company.getCode());
				parent.getCommID().setName(company.getName());
				parent.getCommID().setStartDate(startDate);
				parent.getCommID().setEndDate(endDate);
				companyDAO.addParent(parent);
			} else if (company.getHasParent() && company.getHasChild() && hisParentIsParent) {
				// the model has parent also has child and his parent is parent ==> save as
				// subParent
				CompanyStructSubparent subParent = companyDAO.getSubParent(company.getCode());
				subParent.getCommID().setName(company.getName());
				subParent.getCommID().setStartDate(startDate);
				subParent.getCommID().setEndDate(endDate);
				companyDAO.addSubParentToParent(subParent, parentCode);
			} else if (company.getHasParent() && company.getHasChild() && hisParentIsSubParent) {
				CompanyStructSubparent subParent = companyDAO.getSubParent(company.getCode());
				subParent.getCommID().setName(company.getName());
				subParent.getCommID().setStartDate(startDate);
				subParent.getCommID().setEndDate(endDate);
				companyDAO.addSubParentToSubParent(subParent);
			} else if (company.getHasParent() && !company.getHasChild() && hisParentIsParent) {
				// the model has parent and has no child and his parent is parent ==>save as
				// child to parent
				CompanyStructChild child = companyDAO.getChild(company.getCode());
				child.getCommID().setName(company.getName());
				child.getCommID().setStartDate(startDate);
				child.getCommID().setEndDate(endDate);
				companyDAO.addChildToParent(child, parentCode);
			} else if (company.getHasParent() && !company.getHasChild() && hisParentIsSubParent) {
				// the model has parent and has no child and his parent is subParent ==>save as
				// child to subParent
				CompanyStructChild child = companyDAO.getChild(company.getCode());

				child.getCommID().setName(company.getName());
				child.getCommID().setStartDate(startDate);
				child.getCommID().setEndDate(endDate);
				companyDAO.addChildToSubParent(child, parentCode);
			}
		}
	}

	@Override
	@Transactional
	public List<CompanyStructSubparent> getSubOfSub(String code) {
		try {
			CompanyStructSubparent subParent = companyDAO.getSubParent(code);
			List<CompanyStructSubparent> listOfSubParents = new ArrayList<CompanyStructSubparent>();
			List<CompanyStructSubparent> subParents = companyDAO.getSubParentsOfSubParents(subParent.getCommID().getCode());
			if (subParents != null) {
				for (Integer i = 0; i < subParents.size(); i++) {
					listOfSubParents.add(subParents.get(i));

					if (companyDAO.getSubParentsOfSubParents(subParents.get(i).getCommID().getCode()) != null) {
						listOfSubParents.addAll(getSubOfSub(subParents.get(i).getCommID().getCode()));
					}
				}
			}
			return listOfSubParents;
		}catch(Exception e) {
			throw new NotFoundException("The subParent with code :"+code+" is not saved");

		}
	}

	@Override
	@Transactional
	public void deleteParent(String code) {
		try {
			CompanyStructParent parent = getParent(code);
			List<CompanyStructSubparent> subs = parent.getSubParents();
			List<CompanyStructSubparent> subOfsub = new ArrayList<CompanyStructSubparent>();
			for (int i = 0; i < subs.size(); i++) {
				subOfsub.addAll(getSubOfSub(subs.get(i).getCommID().getCode()));
			}
			for (int i = 0; i < subOfsub.size(); i++) {
				companyDAO.deleteSubParent(subOfsub.get(i).getCommID().getCode());
			}
			companyDAO.deleteParent(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot Delete! -the parent code :"+code+" is not saved");

		}
	}

	@Override
	@Transactional
	public void deleteSubParent(String code) {
		try {
			CompanyStructSubparent sub = companyDAO.getSubParent(code);
			CompanyStructParent parent = sub.getParent();
			if (parent != null) {
				List<CompanyStructSubparent> subList = parent.getSubParents();
				subList.remove(sub);
			}
			List<CompanyStructSubparent> subParents = getSubOfSub(code);
			companyDAO.deleteSubParent(sub.getCommID().getCode());
			for (int i = 0; i < subParents.size(); i++) {
				companyDAO.deleteSubParent(subParents.get(i).getCommID().getCode());
			}
		}catch(Exception e) {
			throw new NotFoundException("Cannot Delete! -the subParent code :"+code+" is not saved");
		}
	}

	@Override
	@Transactional
	public void deleteChild(String code) {
		try {
			CompanyStructChild child = companyDAO.getChild(code);
			CompanyStructSubparent sub = child.getSubParent();
			CompanyStructParent parent = child.getParent();
			List<CompanyStructChild> childrenList = new ArrayList<CompanyStructChild>();
			if (parent != null) {
				childrenList = parent.getChildren();
			} else {
				childrenList = sub.getChildren();
			}
			childrenList.remove(child);
			companyDAO.deleteChild(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot Delete! -the child code :"+code+" is not saved");
		}
		
	}

	@Override
	@Transactional
	public void delmitParent(String code, String endDate) throws ParseException {
		Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		CompanyStructParent parent;
		try {
			parent = companyDAO.getParent(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot delimit parent with code:"+code+" not found");
		}	
		parent.getCommID().setEndDate(enddate);
		parent.getCommID().setDeleted(1);
	
	}

	@Override
	@Transactional
	public void delmitSubParent(String code, String endDate) throws ParseException {
		Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		CompanyStructSubparent sub;
		try {
			sub = companyDAO.getSubParent(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot delimit subParent with code:"+code+" not found");
		}
		sub.getCommID().setEndDate(enddate);
		sub.getCommID().setDeleted(1);
		
	}

	@Override
	@Transactional
	public void delmitChild(String code, String endDate) throws ParseException {
		Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		CompanyStructChild child;
		try {child = companyDAO.getChild(code);}catch(Exception e) {
			throw new NotFoundException("Cannot delimit child with code:"+code+" not found");
		}
		child.getCommID().setEndDate(enddate);
		child.getCommID().setDeleted(1);
	}

	@Override
	@Transactional
	public void copyCompanyStructure(CompanyStructModel companyStructModel, String todayDate) throws Exception {
		CompanyStructModel newModel = new CompanyStructModel();
		
		newModel.setCode(companyStructModel.getCode());
		newModel.setEndDate(companyStructModel.getEndDate());
		newModel.setStartDate(companyStructModel.getStartDate());
		newModel.setName(companyStructModel.getName());
		newModel.setHasParent(companyStructModel.getHasParent());
		newModel.setHasChild(companyStructModel.getHasChild());
		newModel.setParentCode(companyStructModel.getParentCode());
		/*
		 * if (employeeStructModel.getHasParent() == false) {
		 * newModel.setParentCode(null); delmitParent(employeeStructModel.getCode(),
		 * todayDate); } else {
		 * newModel.setParentCode(employeeStructModel.getParentCode()); }
		 */
		processTheIncommingModel(newModel);
	}

	@Override
	@Transactional
	public List<CompanyStructModel> getAllCompanyStructure() {
		List<CompanyStructModel> listOfCompanyStructModels = new ArrayList<CompanyStructModel>();
		List<CompanyCommonID> companyStructs = companyDAO.getAllCompanyStructure();
		if (companyStructs != null) {
			for (Integer i = 0; i < companyStructs.size(); i++) {
				// create a model
				CompanyStructModel model = new CompanyStructModel();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				String startDate = dateFormat.format(companyStructs.get(i).getStartDate());
				String endDate = dateFormat.format(companyStructs.get(i).getEndDate());

				if(companyDAO.isParent(companyStructs.get(i).getCode())){
					model.setHasChild(true);
					model.setHasParent(false);
				}else if(companyDAO.isSubParent(companyStructs.get(i).getCode())) {
					model.setHasChild(true);
					model.setHasParent(true);
				}else {
					model.setHasChild(false);
					model.setHasParent(true);
				}
				model.setParentCode(null);
				model.setStartDate(startDate);
				model.setEndDate(endDate);
				model.setCode(companyStructs.get(i).getCode());
				model.setName(companyStructs.get(i).getName());
				listOfCompanyStructModels.add(model);

			}
		}
		return listOfCompanyStructModels;
	}

	@Override
	@Transactional
	public void assignPaytypeToCompanyStruct(String code, List<String> payTypeCodes) {
	CompanyCommonID companyStruct = companyDAO.getCompanyStruct(code);
		for(int i=0;i<payTypeCodes.size();i++) {
			PayType paytype = paytypeDAO.getPayType(payTypeCodes.get(i));
			PayTypeCommId payTypeCommId = paytype.getCommID();
			if(!companyStruct.getPaytypes().contains(payTypeCommId)) {
				companyStruct.addPaytype(payTypeCommId);
			}
		}
	}

	@Override
	@Transactional
	public List<String> getAllPaytypesAssignedToCompanyStruct(String companyCode) {
		//returns list of payTypeCodes assigned to that employee structure
		CompanyCommonID companyStruct = companyDAO.getCompanyStruct(companyCode);
		List<PayTypeCommId> paytypes = companyStruct.getPaytypes();
		List<String> paytypeCodes=new ArrayList<String>();
		for(int i =0;i<paytypes.size();i++) {
			paytypeCodes.add(paytypes.get(i).getCode());
		}
		return paytypeCodes;
	}

	@Override
	@Transactional
	public void removePaytypeFromCompanyStuct(String code, List<String> payTypeCodes) {
		CompanyCommonID companyStruct = companyDAO.getCompanyStruct(code);
		List<PayTypeCommId> paytypes = companyStruct.getPaytypes();
		for(int i=0;i<payTypeCodes.size();i++) {
			for(int j=0;j<paytypes.size();j++) {
				if(payTypeCodes.get(i).equals(paytypes.get(j).getCode())) {
					paytypes.remove(paytypes.get(j));
				}
			}
		}
		
	}

}
