package com.dao.lookups;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.lookups.Type;

@Repository
public class TypeLookupDAOImpl implements TypeLookupDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Type> getListOfTypes() {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Type> theQuery = session.createQuery("from Type",Type.class);
		List<Type> Types = theQuery.list();
					
		return Types;
	}

	@Override
	public Type getTypeByName(String name) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Type> theQuery = session.createQuery("from Type where name =:name",Type.class);
		theQuery.setParameter("name", name);
		Type theType = (Type)theQuery.getSingleResult();
				
		return theType;
	}
	
	@Override
	public Type getTypeByCode(String code) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Type> theQuery = session.createQuery("from Type where code =:code",Type.class);
		theQuery.setParameter("code", code);
		Type theType = (Type)theQuery.getSingleResult();
				
		return theType;
	}

}
