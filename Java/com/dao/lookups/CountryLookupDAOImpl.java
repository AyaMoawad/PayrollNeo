package com.dao.lookups;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.lookups.Country;

@Repository
public class CountryLookupDAOImpl implements CountryLookupDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Country> getListOfCountrys() {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Country> theQuery = session.createQuery("from Country",Country.class);
		List<Country> Countrys = theQuery.list();
					
		return Countrys;
	}

	@Override
	public Country getCountryByName(String name) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Country> theQuery = session.createQuery("from Country where name =:name",Country.class);
		theQuery.setParameter("name", name);
		Country theCountry = (Country)theQuery.getSingleResult();
				
		return theCountry;
		}

	@Override
	public Country getCountryByCode(String code) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Country> theQuery = session.createQuery("from Country where code =:code",Country.class);
		theQuery.setParameter("code", code);
		Country theCountry = (Country)theQuery.getSingleResult();
				
		return theCountry;
		}

}
