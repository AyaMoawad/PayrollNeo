package com.dao.lookups;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.lookups.PayrollValuation;

@Repository
public class PayrollValuationLookupDAOImpl implements PayrollValuationLookupDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<PayrollValuation> getListOfPayrollValuations() {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<PayrollValuation> theQuery = session.createQuery("from PayrollValuation",PayrollValuation.class);
		List<PayrollValuation> PayrollValuations = theQuery.list();
					
		return PayrollValuations;
	}

	@Override
	public PayrollValuation getPayrollValuationByName(String name) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<PayrollValuation> theQuery = session.createQuery("from PayrollValuation where name =:name",PayrollValuation.class);
		theQuery.setParameter("name", name);
		PayrollValuation thePayrollValuation = (PayrollValuation)theQuery.getSingleResult();
				
		return thePayrollValuation;
	}
	
	@Override
	public PayrollValuation getPayrollValuationByCode(String code) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<PayrollValuation> theQuery = session.createQuery("from PayrollValuation where code =:code",PayrollValuation.class);
		theQuery.setParameter("code", code);
		PayrollValuation thePayrollValuation = (PayrollValuation)theQuery.getSingleResult();
				
		return thePayrollValuation;
	}


}
