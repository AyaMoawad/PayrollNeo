package com.dao.lookups;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.lookups.TaxesLookUp;

@Repository
public class TaxesLookupDAOImpl implements TaxesLookupDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<TaxesLookUp> getListOfTaxesLookUp() {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<TaxesLookUp> theQuery = session.createQuery("from TaxesLookUp",TaxesLookUp.class);
		List<TaxesLookUp> TaxesLookUps = theQuery.list();
					
		return TaxesLookUps;
	}

	@Override
	public TaxesLookUp getTaxesLookUpByName(String name) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<TaxesLookUp> theQuery = session.createQuery("from TaxesLookUp where name =:name",TaxesLookUp.class);
		theQuery.setParameter("name", name);
		TaxesLookUp theTaxesLookUp = (TaxesLookUp)theQuery.getSingleResult();
				
		return theTaxesLookUp;
		}

	@Override
	public TaxesLookUp getTaxesLookUpByCode(String code) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<TaxesLookUp> theQuery = session.createQuery("from TaxesLookUp where code =:code",TaxesLookUp.class);
		theQuery.setParameter("code", code);
		TaxesLookUp theTaxesLookUp = (TaxesLookUp)theQuery.getSingleResult();
				
		return theTaxesLookUp;
		}

}
