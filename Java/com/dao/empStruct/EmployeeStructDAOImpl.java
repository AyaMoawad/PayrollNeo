package com.dao.empStruct;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.empStruct.CommonID;
import com.entities.empStruct.EmpStructChild;
import com.entities.empStruct.EmpStructParent;
import com.entities.empStruct.EmpStructSubparent;

@Repository
public class EmployeeStructDAOImpl implements EmployeeStructDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addParent(EmpStructParent parent) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();			
		session.saveOrUpdate(parent);		
	}

	@Override
	public void addSubParentToParent(EmpStructSubparent subParent, String parentCode) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
			
		//get the parent using parent code
		Query<EmpStructParent> query = session.createQuery("from EmpStructParent where commID=(select id from CommonID where code =:code)", EmpStructParent.class);
		query.setParameter("code", parentCode);
		EmpStructParent parent = (EmpStructParent) query.getSingleResult();
				
		//Add the subParent to this parent 
		parent.addSubParent(subParent);
		session.saveOrUpdate(subParent);		
	}
	public void addSubParentToSubParent(EmpStructSubparent subParent) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
			
		session.saveOrUpdate(subParent);		
	}
	@Override
	public void addChildToParent(EmpStructChild child, String parentCode) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
			
		//get the parent using parent code
		Query<EmpStructParent> query = session.createQuery("from EmpStructParent where commID=(select id from CommonID where code =:code)", EmpStructParent.class);
		query.setParameter("code", parentCode);
		EmpStructParent parent = (EmpStructParent) query.getSingleResult();
				
		//Add the subParent to this parent 
		parent.addChild(child);
		session.saveOrUpdate(child);
	}
	public void addChildToSubParent(EmpStructChild child, String parentCode) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
			
		//get the parent using subParet code
		Query<EmpStructSubparent> query = session.createQuery("from EmpStructSubparent where commID=(select id from CommonID where code =:code)", EmpStructSubparent.class);
		query.setParameter("code", parentCode);
		EmpStructSubparent subParent = (EmpStructSubparent) query.getSingleResult();
						
		//Add the subParent to this parent 
		subParent.addChild(child);
		session.saveOrUpdate(child);		
	}

	@Override
	public EmpStructParent getParent(String code) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		EmpStructParent parent;
		
		// get all subParents with that parent code
		Query<EmpStructParent> theQuery = session.createQuery("from EmpStructParent where commID=(select id from CommonID where code =:code)", EmpStructParent.class);
		theQuery.setParameter("code",code);
			
		parent = (EmpStructParent) theQuery.getSingleResult();
				
		return parent;
	}
	public EmpStructSubparent getSubParent(String code) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		EmpStructSubparent subParent ;
		// get all subParents with that parent code
		Query<EmpStructSubparent> theQuery = session.createQuery("from EmpStructSubparent where commID=(select id from CommonID where code =:code)", EmpStructSubparent.class);
		theQuery.setParameter("code",code);
					
		subParent = (EmpStructSubparent) theQuery.getSingleResult();
								
		return subParent;
	}
	public EmpStructChild getChild(String code) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		EmpStructChild child;
		
		Query<EmpStructChild> theQuery = session.createQuery("from EmpStructChild where commID=(select id from CommonID where code =:code)", EmpStructChild.class);
		theQuery.setParameter("code",code);
			
		child = (EmpStructChild) theQuery.getSingleResult();
								
		return child;
	}

	@Override
	public EmpStructParent getParentById(int id) {
		Session session = sessionFactory.getCurrentSession();
		EmpStructParent parent;
		parent = session.get(EmpStructParent.class,id);
		
		return parent;
	}
	@Override
	public EmpStructChild getChildById(int id) {
		Session session = sessionFactory.getCurrentSession();
		EmpStructChild child;
		child = session.get(EmpStructChild.class, id);
		
		return child;	
	}
	@Override
	public EmpStructSubparent getSubById(int id) {
		Session session = sessionFactory.getCurrentSession();
		EmpStructSubparent sub;
		sub = session.get(EmpStructSubparent.class, id);
		
		return sub;
	}
		
	@Override
	public List<EmpStructSubparent> getSubParentsOfSubParents(String parentCode) {
		List<EmpStructSubparent> empStructSubParents = new ArrayList<EmpStructSubparent>();
		// get the session
		Session session = sessionFactory.getCurrentSession();
		try {
			// get all subParents with that parent code
			Query<EmpStructSubparent> theQuery = session.createQuery("from EmpStructSubparent s where s.parentCode =:parentCode", EmpStructSubparent.class);
			theQuery.setParameter("parentCode", parentCode);
			
			empStructSubParents = theQuery.getResultList();
					
		}catch(Exception e) {return null;}
		return empStructSubParents;
	}
	@Override
	public List<EmpStructSubparent> getSubParentsOfParent(String parentCode) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		
		// get all subParents with that parent code
		Query<EmpStructParent> theQuery = session.createQuery("from EmpStructParent where commID=(select id from CommonID where code =:code)", EmpStructParent.class);
		theQuery.setParameter("code",parentCode);
				
		EmpStructParent parent = (EmpStructParent) theQuery.getSingleResult();
								
		// get all subParents with that parent code
		Query<EmpStructSubparent> query = session.createQuery("from EmpStructSubparent s where s.parent =:parentId", EmpStructSubparent.class);
		theQuery.setParameter("parentId", parent.getId());
				
		List<EmpStructSubparent> empStructSubParents = query.getResultList();
						
		return empStructSubParents;
	}
	@Override
	public List<CommonID> getAllEmployeeStructure() {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		
		// get all 
		Query<CommonID> theQuery = session.createQuery("from CommonID", CommonID.class);
		List<CommonID> allEmpStruct=theQuery.getResultList();
						
		return allEmpStruct;
	}
	@Override
	public Boolean isParent(String parentCode) {
		Boolean isParent;
		
		// get the session 
		Session session = sessionFactory.getCurrentSession();
								
		try {
			//get the parent using parent code
			Query<EmpStructParent> query = session.createQuery("from EmpStructParent where commID=(select id from CommonID where code =:code)", EmpStructParent.class);
			query.setParameter("code", parentCode);
			EmpStructParent parent = (EmpStructParent) query.getSingleResult();
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
			Query<EmpStructSubparent> query = session.createQuery("from EmpStructSubparent where commID=(select id from CommonID where code =:code)", EmpStructSubparent.class);
			query.setParameter("code", parentCode);
			EmpStructSubparent subParent = (EmpStructSubparent) query.getSingleResult();
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
	public void deleteParent(String code) {
		Session session = sessionFactory.getCurrentSession();
		
		EmpStructParent parent=getParent(code);
		session.delete(parent);
		
	}

	@Override
	public void deleteSubParent(String code) {
		Session session = sessionFactory.getCurrentSession();
		
		EmpStructSubparent sub = getSubParent(code);
		session.delete(sub);
		
	}

	@Override
	public void deleteChild(String code) {
		Session session = sessionFactory.getCurrentSession();
		
		EmpStructChild child = getChild(code);
		session.delete(child);
		
	}

	@Override
	public CommonID getEmpStruct(String empStructCode) {
		// get the session
		Session session = sessionFactory.getCurrentSession();

		// get all empStruct by code
		Query<CommonID> theQuery = session.createQuery(
				"from CommonID where code =:code", CommonID.class);
		theQuery.setParameter("code", empStructCode);

		CommonID empStruct = (CommonID) theQuery.getSingleResult();

		return empStruct;
	}
	
}
