package com.dao.payrollstruct;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.payrollstruct.*;

@Repository
public class PayrollStructDAOImpl implements PayrollStructDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void addPayrollStruct(PayrollStruct PayrollStruct) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();			
		session.saveOrUpdate(PayrollStruct);
	}

	@Override
	public PayrollStruct getPayrollStruct(String code) {
		// get the session
		Session session = sessionFactory.getCurrentSession();
		PayrollStruct PayrollStruct ;
		Query<PayrollStruct> theQuery = session.createQuery("From PayrollStruct where commID=(select id from PayrollStructCommId where code =:code)", PayrollStruct.class); 
		theQuery.setParameter("code",code);
		
		PayrollStruct = theQuery.getSingleResult();
		
		return PayrollStruct;		
	}

	
	@Override
	public PayrollStruct getPayrollStructById(int id) {
		Session session = sessionFactory.getCurrentSession();
		PayrollStruct PayrollStruct = session.get(PayrollStruct.class, id);
		return PayrollStruct;		
	}
	
	@Override
	public void deletePayrollStruct(String code) {
		Session session = sessionFactory.getCurrentSession();
		PayrollStruct PayrollStruct=getPayrollStruct(code);
		session.delete(PayrollStruct);
	}
	
	
	
	@Override
	public List<PayrollStruct> getAllPayrollStructs(){
		// get the session
		Session session = sessionFactory.getCurrentSession();
		List<PayrollStruct> PayrollStructs = new ArrayList<PayrollStruct>();
		Query<PayrollStruct> theQuery =session.createQuery("From PayrollStruct",PayrollStruct.class);
		
		PayrollStructs = theQuery.getResultList();
		return PayrollStructs;		
	}
	
	
	
	
	
	
	
	
	
	

}
