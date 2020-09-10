package com.dao.lookups;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.lookups.Currency;

@Repository
public class CurrencyLookupDAOImpl implements CurrencyLookupDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Currency> getListOfCurrencys() {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Currency> theQuery = session.createQuery("from Currency",Currency.class);
		List<Currency> Currencys = theQuery.list();
					
		return Currencys;
	}

	@Override
	public Currency getCurrencyByName(String name) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Currency> theQuery = session.createQuery("from Currency where name =:name",Currency.class);
		theQuery.setParameter("name", name);
		Currency theCurrency = (Currency)theQuery.getSingleResult();
				
		return theCurrency;
		}

	@Override
	public Currency getCurrencyByCode(String code) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Currency> theQuery = session.createQuery("from Currency where code =:code",Currency.class);
		theQuery.setParameter("code", code);
		Currency theCurrency = (Currency)theQuery.getSingleResult();
				
		return theCurrency;
		}

}
