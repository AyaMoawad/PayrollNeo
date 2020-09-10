package com.services.empStruct;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.empStruct.EmployeeStructDAO;
import com.dao.payType.PayTypeDAO;
import com.entities.empStruct.CommonID;
import com.entities.empStruct.EmpStructChild;
import com.entities.empStruct.EmpStructParent;
import com.entities.empStruct.EmpStructSubparent;
import com.entities.payType.PayType;
import com.entities.payType.PayTypeCommId;
import com.models.empStuct.EmployeeStructModel;
import com.rest.errorhandling.NotFoundException;
import com.rest.errorhandling.UniqunessException;

@Service
public class EmployeeStructServiceImpl implements EmployeeStructService {

	@Autowired
	private EmployeeStructDAO employeeDAO;
	
	@Autowired
	private PayTypeDAO paytypeDAO;

	@Override
	@Transactional
	public void processTheIncommingModel(EmployeeStructModel employee) throws Exception {
		if (employee.getName() != null && employee.getCode() != null && employee.getEndDate() != null
				&& employee.getStartDate() != null) {

			// conversion of dates
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(employee.getStartDate());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(employee.getEndDate());

			// commonID data
			CommonID commId = new CommonID(startDate, endDate, employee.getCode(), employee.getName());
			commId.setDeleted(0);

			String parentCode = employee.getParentCode();

			// check if this model has parent and type of his parent
			Boolean hisParentIsParent = employeeDAO.isParent(parentCode);
			Boolean hisParentIsSubParent = employeeDAO.isSubParent(parentCode);

			// Process the model to know if it is a parent/SubParent/Child
			if (!employee.getHasParent() && employee.getHasChild()) {
				// the model has no parent but has a child ==> save as parent
				EmpStructParent parent = new EmpStructParent(commId);
				try {
					employeeDAO.addParent(parent);
				}catch(Exception e) {
					throw new UniqunessException("the parent code : "+parent.getCommID().getCode()+" is already used!");
				}
			} else if (employee.getHasParent() && employee.getHasChild() && hisParentIsParent) {
				// the model has parent also has child and his parent is parent ==> save as
				// subParent
				EmpStructSubparent subParent = new EmpStructSubparent(0, null, commId);
				try {
					employeeDAO.addSubParentToParent(subParent, parentCode);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+subParent.getCommID().getCode()+" is already used!");
				}
			} else if (employee.getHasParent() && employee.getHasChild() && hisParentIsSubParent) {
				// the model has parent also has child and his parent is subParent ==> save as
				// subParent
				EmpStructSubparent subParent = new EmpStructSubparent(1, parentCode, commId);
				try {
					employeeDAO.addSubParentToSubParent(subParent);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+subParent.getCommID().getCode()+" is already used!");
				}
			} else if (employee.getHasParent() && !employee.getHasChild() && hisParentIsParent) {

				// the model has parent and has no child and his parent is parent ==>save as
				// child to parent
				EmpStructChild child = new EmpStructChild(commId);
				
				try {
					employeeDAO.addChildToParent(child, parentCode);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+child.getCommID().getCode()+" is already used!");
				}

			} else if (employee.getHasParent() && !employee.getHasChild() && hisParentIsSubParent) {

				// the model has parent and has no child and his parent is subParent ==>save as
				// child to subParent
				EmpStructChild child = new EmpStructChild(commId);
				try {
					employeeDAO.addChildToSubParent(child, parentCode);
				}catch(Exception e) {
					throw new UniqunessException("the subParent code : "+child.getCommID().getCode()+" is already used!");
				}
			}
		} else if(employee.getName() == null) {
			throw new UniqunessException("the employee name is missing!");
		}else if(employee.getCode() == null) {
			throw new UniqunessException("the employee code is missing!");
		}else if(employee.getStartDate() == null) {
			throw new UniqunessException("the employee start date is missing!");
		}else if(employee.getEndDate() == null) {
			throw new UniqunessException("the employee end date is missing!");
		}else {
			throw new UniqunessException("ERROR! can't save/update the employee structure");
		}
	}

	@Override
	@Transactional
	public List<EmployeeStructModel> getTheSubParentsOfSubParent(String parentCode) {
		List<EmployeeStructModel> listOfSubParents = new ArrayList<EmployeeStructModel>();
		List<EmpStructSubparent> subParents = employeeDAO.getSubParentsOfSubParents(parentCode);
		if (subParents != null) {
			for (Integer i = 0; i < subParents.size(); i++) {
				// create a model
				EmployeeStructModel model = new EmployeeStructModel();
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

				if (employeeDAO.getSubParentsOfSubParents(subParents.get(i).getCommID().getCode()) != null) {
					listOfSubParents.addAll(getTheSubParentsOfSubParent(subParents.get(i).getCommID().getCode()));
				}
			}
		}
		return listOfSubParents;
	}

	@Override
	@Transactional
	public List<EmployeeStructModel> getAllEmployeeStructure() {
		List<EmployeeStructModel> listOfEmpStructModels = new ArrayList<EmployeeStructModel>();
		List<CommonID> empStructs = employeeDAO.getAllEmployeeStructure();
		if (empStructs != null) {
			for (Integer i = 0; i < empStructs.size(); i++) {
				// create a model
				EmployeeStructModel model = new EmployeeStructModel();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				String startDate = dateFormat.format(empStructs.get(i).getStartDate());
				String endDate = dateFormat.format(empStructs.get(i).getEndDate());

				if(employeeDAO.isParent(empStructs.get(i).getCode())){
					model.setHasChild(true);
					model.setHasParent(false);
				}else if(employeeDAO.isSubParent(empStructs.get(i).getCode())) {
					model.setHasChild(true);
					model.setHasParent(true);
				}else {
					model.setHasChild(false);
					model.setHasParent(true);
				}
				model.setParentCode(null);
				model.setStartDate(startDate);
				model.setEndDate(endDate);
				model.setCode(empStructs.get(i).getCode());
				model.setName(empStructs.get(i).getName());
				listOfEmpStructModels.add(model);

			}
		}
		return listOfEmpStructModels;
	}

	
	
	
	
	@Override
	@Transactional
	public EmployeeStructModel getTheParent(String code) {
		try {
			EmpStructParent parent = employeeDAO.getParent(code);
			EmployeeStructModel model = new EmployeeStructModel();
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
	public EmployeeStructModel getTheSubParent(String code) {
		try {
			EmpStructSubparent subParent = employeeDAO.getSubParent(code);
			EmployeeStructModel model = new EmployeeStructModel();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			String startDate = dateFormat.format(subParent.getCommID().getStartDate());
			String endDate = dateFormat.format(subParent.getCommID().getEndDate());
			EmpStructParent parent = subParent.getParent();
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
	public EmployeeStructModel getTheChild(String code) {
		try {
			EmpStructChild child = employeeDAO.getChild(code);
			EmployeeStructModel model = new EmployeeStructModel();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			String startDate = dateFormat.format(child.getCommID().getStartDate());
			String endDate = dateFormat.format(child.getCommID().getEndDate());

			EmpStructParent hisParentIsParent = child.getParent();
			EmpStructSubparent hisParentIsSub = child.getSubParent();
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
	public List<EmployeeStructModel> getTheSubParentsOfParent(String code) {
		EmpStructParent parent ;
		try {parent = employeeDAO.getParent(code);}catch(Exception e) {throw new NotFoundException("Cannot find parent with code:"+code);}
		List<EmpStructSubparent> subParentsOfParent = parent.getSubParents();
		List<EmployeeStructModel> listOfSubParents = new ArrayList<EmployeeStructModel>();
		if (subParentsOfParent != null) {
			for (Integer i = 0; i < subParentsOfParent.size(); i++) {
				EmployeeStructModel model = new EmployeeStructModel();
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
	public List<EmployeeStructModel> getTheChildrenOfSubParent(String subCode) {
		EmpStructSubparent subParent ;
		try {subParent= employeeDAO.getSubParent(subCode);}catch(Exception e) {throw new NotFoundException("Cannot find subParent with code:"+subCode);}
		List<EmpStructChild> childrenOfSub = subParent.getChildren();
		List<EmployeeStructModel> listOfChildren = new ArrayList<EmployeeStructModel>();
		if (childrenOfSub != null) {
			for (Integer i = 0; i < childrenOfSub.size(); i++) {
				EmployeeStructModel model = new EmployeeStructModel();
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
	public List<EmployeeStructModel> getTheChildrenOfParent(String parentCode) {
		EmpStructParent parent ;
		try {parent = employeeDAO.getParent(parentCode);}catch(Exception e) {throw new NotFoundException("Cannot find parent with code:"+parentCode);}
		
		List<EmpStructChild> childrenOfParent = parent.getChildren();
		List<EmployeeStructModel> listOfChildren = new ArrayList<EmployeeStructModel>();
		if (childrenOfParent != null) {
			for (Integer i = 0; i < childrenOfParent.size(); i++) {
				EmployeeStructModel model = new EmployeeStructModel();
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
	public EmployeeStructModel getParentOfSub(EmpStructSubparent sub) {
		EmployeeStructModel parentOfSub;
		if (sub.getHasParent() == 0) {
			EmpStructParent parent = sub.getParent();
			parentOfSub = (getTheParent(parent.getCommID().getCode()));
		} else {
			EmpStructSubparent subParent ;
			try {subParent = employeeDAO.getSubParent(sub.getParentCode());}catch(Exception e) {
				subParent=null;
			}
			parentOfSub = (getTheSubParent(subParent.getCommID().getCode()));
		}
		return parentOfSub;
	}

	@Override
	@Transactional
	public EmployeeStructModel getParentOfChild(EmpStructChild child) {
		EmployeeStructModel parentOfChild = null;
		if (child.getParent() != null) {
			EmployeeStructModel parent = getTheParent(child.getParent().getCommID().getCode());
			parentOfChild = parent;
		} else if (child.getSubParent() != null) {
			EmployeeStructModel sub = getTheSubParent(child.getSubParent().getCommID().getCode());
			parentOfChild = sub;
		}

		return parentOfChild;
	}

	@Override
	@Transactional
	public EmpStructParent getParent(String code) {
		try {
			EmpStructParent parent = employeeDAO.getParent(code);
			return parent;
		}catch(Exception e) {
			throw new NotFoundException("Cannot get parent with code:"+code+" is not saved");
		}
	}

	@Override
	@Transactional
	public EmpStructSubparent getSubParent(String code) {
		try {
			EmpStructSubparent subParent = employeeDAO.getSubParent(code);
			return subParent;
		}catch(Exception e) {
			throw new NotFoundException("Cannot get parent with code:"+code+" is not saved");
		}
	}
	@Override
	@Transactional
	public Boolean isSubParent(String parentCode) {
		Boolean isSub = employeeDAO.isSubParent(parentCode);
		return isSub;
	}
	@Override
	@Transactional
	public Boolean isParent(String parentCode) {
		Boolean isParent = employeeDAO.isParent(parentCode);
		return isParent;
	}

	/*
	 * Getting The Chain
	 */
	@Override
	@Transactional
	public List<EmployeeStructModel> getParentChain(String code) {
		List<EmployeeStructModel> parentMap = new ArrayList<EmployeeStructModel>();
		// getting parent
		EmployeeStructModel parent = getTheParent(code);
		EmpStructParent parentObject = getParent(code);
		List<EmployeeStructModel> subOfParent = getTheSubParentsOfParent(parentObject.getCommID().getCode());
		List<EmployeeStructModel> childrenOfParent = getTheChildrenOfParent(parentObject.getCommID().getCode());
		List<EmpStructSubparent> subOfParentObject = parentObject.getSubParents();

		for (Integer i = 0; i < subOfParentObject.size(); i++) {
			List<EmployeeStructModel> subOfSub = getTheSubParentsOfSubParent(
					subOfParentObject.get(i).getCommID().getCode());
			List<EmployeeStructModel> childrenOfSubMap = getTheChildrenOfSubParent(
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
	public List<EmployeeStructModel> getSubParentChain(String code) {
		List<EmployeeStructModel> subparentChainMap = new ArrayList<EmployeeStructModel>();
		EmployeeStructModel subParent = getTheSubParent(code);
		EmpStructSubparent subObject;
		try {subObject = employeeDAO.getSubParent(code);}catch(Exception e) {throw new NotFoundException("Cannot find subParent with code:" +code);}
		subparentChainMap.add(subParent);

		subparentChainMap.addAll(getTheChildrenOfSubParent(code));

		List<EmployeeStructModel> subOfSub = getTheSubParentsOfSubParent(subObject.getCommID().getCode());
		subparentChainMap.addAll(subOfSub);

		while (subObject.getHasParent() != 0) {
			subparentChainMap.add(getParentOfSub(subObject));
			subObject = employeeDAO.getSubParent(subObject.getParentCode());
		}
		subparentChainMap.add(getParentOfSub(subObject));

		return subparentChainMap;
	}

	@Override
	@Transactional
	public List<EmployeeStructModel> getChildChain(String code) {
		List<EmployeeStructModel> childChainMap = new ArrayList<EmployeeStructModel>();

		// getting child
		EmployeeStructModel child = getTheChild(code);
		childChainMap.add(child);
		EmpStructChild childObject ;
		try {childObject = employeeDAO.getChild(code);}catch(Exception e) {throw new NotFoundException("Cannot find child with code: "+code);}
		EmpStructParent hisParent = childObject.getParent();
		EmpStructSubparent hisSubParent = childObject.getSubParent();
		if (hisParent != null) {
			childChainMap.add(getTheParent(childObject.getParent().getCommID().getCode()));
		} else if (hisSubParent != null) {
			childChainMap.add(getTheSubParent(hisSubParent.getCommID().getCode()));
			while (hisSubParent.getHasParent() != 0) {
				childChainMap.add(getParentOfSub(hisSubParent));
				hisSubParent = employeeDAO.getSubParent(hisSubParent.getParentCode());
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
	public void updateEmployeeStructure(EmployeeStructModel employee) throws ParseException {
		// process to check the model(parent/sub/child)
		if (employee.getName() != null && employee.getCode() != null && employee.getEndDate() != null
				&& employee.getStartDate() != null && employee.getHasParent() != null
				&& employee.getHasChild() != null) {
			// conversion of dates
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(employee.getStartDate());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(employee.getEndDate());
			String parentCode = employee.getParentCode();
			// check if this model has parent and type of his parent
			Boolean hisParentIsParent = employeeDAO.isParent(parentCode);
			Boolean hisParentIsSubParent = employeeDAO.isSubParent(parentCode);

			if (!employee.getHasParent() && employee.getHasChild()) {
				// the model has no parent but has a child ==> update parent
				EmpStructParent parent = employeeDAO.getParent(employee.getCode());
				parent.getCommID().setName(employee.getName());
				parent.getCommID().setStartDate(startDate);
				parent.getCommID().setEndDate(endDate);
				employeeDAO.addParent(parent);
			} else if (employee.getHasParent() && employee.getHasChild() && hisParentIsParent) {
				// the model has parent also has child and his parent is parent ==> save as
				// subParent
				EmpStructSubparent subParent = employeeDAO.getSubParent(employee.getCode());
				subParent.getCommID().setName(employee.getName());
				subParent.getCommID().setStartDate(startDate);
				subParent.getCommID().setEndDate(endDate);
				employeeDAO.addSubParentToParent(subParent, parentCode);
			} else if (employee.getHasParent() && employee.getHasChild() && hisParentIsSubParent) {
				EmpStructSubparent subParent = employeeDAO.getSubParent(employee.getCode());
				subParent.getCommID().setName(employee.getName());
				subParent.getCommID().setStartDate(startDate);
				subParent.getCommID().setEndDate(endDate);
				employeeDAO.addSubParentToSubParent(subParent);
			} else if (employee.getHasParent() && !employee.getHasChild() && hisParentIsParent) {
				// the model has parent and has no child and his parent is parent ==>save as
				// child to parent
				EmpStructChild child = employeeDAO.getChild(employee.getCode());
				child.getCommID().setName(employee.getName());
				child.getCommID().setStartDate(startDate);
				child.getCommID().setEndDate(endDate);
				employeeDAO.addChildToParent(child, parentCode);
			} else if (employee.getHasParent() && !employee.getHasChild() && hisParentIsSubParent) {
				// the model has parent and has no child and his parent is subParent ==>save as
				// child to subParent
				EmpStructChild child = employeeDAO.getChild(employee.getCode());

				child.getCommID().setName(employee.getName());
				child.getCommID().setStartDate(startDate);
				child.getCommID().setEndDate(endDate);
				employeeDAO.addChildToSubParent(child, parentCode);
			}
		}
	}

	@Override
	@Transactional
	public List<EmpStructSubparent> getSubOfSub(String code) {
		try {
			EmpStructSubparent subParent = employeeDAO.getSubParent(code);
			List<EmpStructSubparent> listOfSubParents = new ArrayList<EmpStructSubparent>();
			List<EmpStructSubparent> subParents = employeeDAO.getSubParentsOfSubParents(subParent.getCommID().getCode());
			if (subParents != null) {
				for (Integer i = 0; i < subParents.size(); i++) {
					listOfSubParents.add(subParents.get(i));

					if (employeeDAO.getSubParentsOfSubParents(subParents.get(i).getCommID().getCode()) != null) {
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
			EmpStructParent parent = getParent(code);
			List<EmpStructSubparent> subs = parent.getSubParents();
			List<EmpStructSubparent> subOfsub = new ArrayList<EmpStructSubparent>();
			for (int i = 0; i < subs.size(); i++) {
				subOfsub.addAll(getSubOfSub(subs.get(i).getCommID().getCode()));
			}
			for (int i = 0; i < subOfsub.size(); i++) {
				employeeDAO.deleteSubParent(subOfsub.get(i).getCommID().getCode());
			}
			employeeDAO.deleteParent(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot Delete! -the parent code :"+code+" is not saved");

		}
	}

	@Override
	@Transactional
	public void deleteSubParent(String code) {
		try {
			EmpStructSubparent sub = employeeDAO.getSubParent(code);
			EmpStructParent parent = sub.getParent();
			if (parent != null) {
				List<EmpStructSubparent> subList = parent.getSubParents();
				subList.remove(sub);
			}
			List<EmpStructSubparent> subParents = getSubOfSub(code);
			employeeDAO.deleteSubParent(sub.getCommID().getCode());
			for (int i = 0; i < subParents.size(); i++) {
				employeeDAO.deleteSubParent(subParents.get(i).getCommID().getCode());
			}
		}catch(Exception e) {
			throw new NotFoundException("Cannot Delete! -the subParent code :"+code+" is not saved");
		}
	}

	@Override
	@Transactional
	public void deleteChild(String code) {
		try {
			EmpStructChild child = employeeDAO.getChild(code);
			EmpStructSubparent sub = child.getSubParent();
			EmpStructParent parent = child.getParent();
			List<EmpStructChild> childrenList = new ArrayList<EmpStructChild>();
			if (parent != null) {
				childrenList = parent.getChildren();
			} else {
				childrenList = sub.getChildren();
			}
			childrenList.remove(child);
			employeeDAO.deleteChild(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot Delete! -the child code :"+code+" is not saved");
		}
		
	}

	@Override
	@Transactional
	public void delmitParent(String code, String endDate) throws ParseException {
		Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		EmpStructParent parent;
		try {
			parent = employeeDAO.getParent(code);
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
		EmpStructSubparent sub;
		try {
			sub = employeeDAO.getSubParent(code);
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
		EmpStructChild child;
		try {child = employeeDAO.getChild(code);}catch(Exception e) {
			throw new NotFoundException("Cannot delimit child with code:"+code+" not found");
		}
		child.getCommID().setEndDate(enddate);
		child.getCommID().setDeleted(1);
	}

	@Override
	@Transactional
	public void copyEmployeeStructure(EmployeeStructModel employeeStructModel, String todayDate) throws Exception {
		EmployeeStructModel newModel = new EmployeeStructModel();
		newModel.setCode(employeeStructModel.getCode());
		newModel.setEndDate(employeeStructModel.getEndDate());
		newModel.setStartDate(employeeStructModel.getStartDate());
		newModel.setName(employeeStructModel.getName());
		newModel.setHasParent(employeeStructModel.getHasParent());
		newModel.setHasChild(employeeStructModel.getHasChild());
		newModel.setParentCode(employeeStructModel.getParentCode());
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
	public void assignPaytypeToEmployeeStruct(String empStructCode, List<String> paytypeCodes) {
		CommonID empStruct = employeeDAO.getEmpStruct(empStructCode);
		for(int i=0;i<paytypeCodes.size();i++) {
			PayType paytype = paytypeDAO.getPayType(paytypeCodes.get(i));
			PayTypeCommId payTypeCommId = paytype.getCommID();
			if(!empStruct.getPaytypes().contains(payTypeCommId)) {
				empStruct.addPaytype(payTypeCommId);
			}
			
		}
	}

	@Override
	@Transactional
	public List<String> getAllPaytypesAssignedToEmpStruct(String empStructCode) {
		//returns list of payTypeCodes assigned to that employee structure
		CommonID empStruct = employeeDAO.getEmpStruct(empStructCode);
		List<PayTypeCommId> paytypes = empStruct.getPaytypes();
		List<String> paytypeCodes=new ArrayList<String>();
		for(int i =0;i<paytypes.size();i++) {
			paytypeCodes.add(paytypes.get(i).getCode());
		}
		return paytypeCodes;
	}

	@Override
	@Transactional
	public void removePaytypeFromEmpStuct(String empStructCode,List<String> unassignedPaytypes) {
		CommonID empStruct = employeeDAO.getEmpStruct(empStructCode);
		List<PayTypeCommId> paytypes = empStruct.getPaytypes();
		for(int i=0;i<unassignedPaytypes.size();i++) {
			for(int j=0;j<paytypes.size();j++) {
				if(unassignedPaytypes.get(i).equals(paytypes.get(j).getCode())) {
					paytypes.remove(paytypes.get(j));
				}
			}
		}
	}

}
