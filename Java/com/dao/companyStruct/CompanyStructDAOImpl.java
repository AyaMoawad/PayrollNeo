package com.dao.companyStruct;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.companyStruct.CompanyCommonID;
import com.entities.companyStruct.CompanyStructChild;
import com.entities.companyStruct.CompanyStructParent;
import com.entities.companyStruct.CompanyStructSubparent;

@Repository
public class CompanyStructDAOImpl implements CompanyStructDAO{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addParent(CompanyStructParent parent) {
		
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		//then save the parent
		session.saveOrUpdate(parent);
		}
	@Override
	public void addSubParentToParent(CompanyStructSubparent subParent, String parentCode) {
	
		// get the session 
		Session session = sessionFactory.getCurrentSession();
					
		//get the parent using parent code
		Query<CompanyStructParent> query = session.createQuery("from CompanyStructParent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructParent.class);
		query.setParameter("code", parentCode);
		CompanyStructParent parent = (CompanyStructParent) query.getSingleResult();
				
		//Add the subParent to this parent 
		parent.addSubParent(subParent);
		session.saveOrUpdate(subParent);
		
		}
	public void addSubParentToSubParent(CompanyStructSubparent subParent) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(subParent);
		
	}

	@Override
	public void addChildToParent(CompanyStructChild child, String parentCode) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
			
		//get the parent using parent code
		Query<CompanyStructParent> query = session.createQuery("from CompanyStructParent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructParent.class);
		query.setParameter("code", parentCode);
		CompanyStructParent parent = (CompanyStructParent) query.getSingleResult();
				
		//Add the subParent to this parent 
		parent.addChild(child);
		session.saveOrUpdate(child);
		
	}
	public void addChildToSubParent(CompanyStructChild child, String parentCode) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		//get the parent using subParet code
		Query<CompanyStructSubparent> query = session.createQuery("from CompanyStructSubparent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructSubparent.class);
		query.setParameter("code", parentCode);
		CompanyStructSubparent subParent = (CompanyStructSubparent) query.getSingleResult();
		//Add the subParent to this parent 
		subParent.addChild(child);
		session.saveOrUpdate(child);
		
	}

	@Override
	public CompanyStructParent getParent(String code) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		CompanyStructParent parent;
		// get all subParents with that parent code
		Query<CompanyStructParent> theQuery = session.createQuery("from CompanyStructParent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructParent.class);
		theQuery.setParameter("code",code);
			
		parent = (CompanyStructParent) theQuery.getSingleResult();
		
		return parent;
	}
	public CompanyStructSubparent getSubParent(String code) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		CompanyStructSubparent subParent ;
		// get all subParents with that parent code
		Query<CompanyStructSubparent> theQuery = session.createQuery("from CompanyStructSubparent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructSubparent.class);
		theQuery.setParameter("code",code);
					
		subParent = (CompanyStructSubparent) theQuery.getSingleResult();
		
		return subParent;
	}
	public CompanyStructChild getChild(String code) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		CompanyStructChild child;
		// get all subParents with that parent code
		Query<CompanyStructChild> theQuery = session.createQuery("from CompanyStructChild where commID=(select id from CompanyCommonID where code =:code)", CompanyStructChild.class);
		theQuery.setParameter("code",code);
			
		child = (CompanyStructChild) theQuery.getSingleResult();
					
		return child;
	}

	@Override
	public CompanyStructParent getParentById(int id) {
		Session session = sessionFactory.getCurrentSession();
		CompanyStructParent parent;
		parent = session.get(CompanyStructParent.class,id);
		
		return parent;
	}
	@Override
	public CompanyStructChild getChildById(int id) {
		Session session = sessionFactory.getCurrentSession();
		CompanyStructChild child;
		child = session.get(CompanyStructChild.class, id);

		return child;	
	}
	@Override
	public CompanyStructSubparent getSubById(int id) {
		Session session = sessionFactory.getCurrentSession();
		CompanyStructSubparent sub;
		sub = session.get(CompanyStructSubparent.class, id);
		
		return sub;
	}
		
	@Override
	public List<CompanyStructSubparent> getSubParentsOfSubParents(String parentCode) {
		List<CompanyStructSubparent> companyStructSubParents = new ArrayList<CompanyStructSubparent>();
		// get the session
		Session session = sessionFactory.getCurrentSession();
		// get all subParents with that parent code
		Query<CompanyStructSubparent> theQuery = session.createQuery("from CompanyStructSubparent s where s.parentCode =:parentCode", CompanyStructSubparent.class);
		theQuery.setParameter("parentCode", parentCode);
			
		companyStructSubParents = theQuery.getResultList();
		
		return companyStructSubParents;
	}
	public List<CompanyStructSubparent> getSubParentsOfParent(String parentCode) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		
		// get all subParents with that parent code
		Query<CompanyStructParent> theQuery = session.createQuery("from CompanyStructParent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructParent.class);
		theQuery.setParameter("code",parentCode);
				
		CompanyStructParent parent = (CompanyStructParent) theQuery.getSingleResult();
								
		// get all subParents with that parent code
		Query<CompanyStructSubparent> query = session.createQuery("from CompanyStructSubparent s where s.parent =:parentId", CompanyStructSubparent.class);
		theQuery.setParameter("parentId", parent.getId());
				
		List<CompanyStructSubparent> companyStructSubParents = query.getResultList();
						
		return companyStructSubParents;
	}

	@Override
	public Boolean isParent(String parentCode) {
		Boolean isParent;
		
		// get the session 
		Session session = sessionFactory.getCurrentSession();
								
		try {
			//get the parent using parent code
			Query<CompanyStructParent> query = session.createQuery("from CompanyStructParent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructParent.class);
			query.setParameter("code", parentCode);
			CompanyStructParent parent = (CompanyStructParent) query.getSingleResult();
			if(parent!=null) {
				isParent= true;
			}else{
				isParent= false;
			}
		}catch(Exception e) {
			//clear session and return false
			session.clear(); 
			return false;
		}
		return isParent;
	}
	public Boolean isSubParent(String parentCode) {
		Boolean isSubParent;
		// get the session 
		Session session = sessionFactory.getCurrentSession();
								
		try {
			//get the parent using parent code
			Query<CompanyStructSubparent> query = session.createQuery("from CompanyStructSubparent where commID=(select id from CompanyCommonID where code =:code)", CompanyStructSubparent.class);
			query.setParameter("code", parentCode);
			CompanyStructSubparent subParent = (CompanyStructSubparent) query.getSingleResult();
			if(subParent!=null) {
				isSubParent= true;
			}else{
				isSubParent= false;
			}
		}catch(Exception e) {
			//clear session and return false
			session.clear(); 
			return false;
		}
		return isSubParent;
	}

	@Override
	public String deleteParent(String code) {
		Session session = sessionFactory.getCurrentSession();
		Boolean isDeleted =false;
		CompanyStructParent parent=getParent(code);
		session.delete(parent);
		isDeleted=true;
		
		return isDeleted.toString();
	}

	@Override
	public String deleteSubParent(String code) {
		Session session = sessionFactory.getCurrentSession();
		Boolean isDeleted =false;
		CompanyStructSubparent sub = getSubParent(code);
		session.delete(sub);
		isDeleted=true;
		
		return isDeleted.toString();
	}

	@Override
	public String deleteChild(String code) {
		Session session = sessionFactory.getCurrentSession();
		Boolean isDeleted =false;
		CompanyStructChild child = getChild(code);
		session.delete(child);
		isDeleted=true;
		
		return isDeleted.toString();
	}
	
	@Override
	public List<CompanyCommonID> getAllCompanyStructure() {
		// get the session
		Session session = sessionFactory.getCurrentSession();
				
				
		Query<CompanyCommonID> theQuery = session.createQuery("from CompanyCommonID", CompanyCommonID.class);
	 	List<CompanyCommonID> allCompanyStruct=theQuery.getResultList();
								
		return allCompanyStruct;
	}
	
	@Override
	public CompanyCommonID getCompanyStruct(String companyStructCode) {
		// get the session
		Session session = sessionFactory.getCurrentSession();

		// get all empStruct by code
		Query<CompanyCommonID> theQuery = session.createQuery(
				"from CompanyCommonID where code =:code", CompanyCommonID.class);
		theQuery.setParameter("code", companyStructCode);

		CompanyCommonID companyStruct = (CompanyCommonID) theQuery.getSingleResult();

		return companyStruct;
	}
	
}
