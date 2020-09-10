package com.dao.lookups;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entities.lookups.Interval;


@Repository
public class IntervalLookupDAOImpl implements IntervalLookupDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Interval> getListOfIntervals() {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Interval> theQuery = session.createQuery("from Interval",Interval.class);
		List<Interval> Intervals = theQuery.list();
			
		return Intervals;
	}

	@Override
	public Interval getIntervalByName(String name) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Interval> theQuery = session.createQuery("from Interval where name =:name",Interval.class);
		theQuery.setParameter("name", name);
		Interval theInterval = (Interval)theQuery.getSingleResult();
		
		return theInterval;
	}
	
	@Override
	public Interval getIntervalByCode(String code) {
		// get the session 
		Session session = sessionFactory.getCurrentSession();
		Query<Interval> theQuery = session.createQuery("from Interval where code =:code",Interval.class);
		theQuery.setParameter("code", code);
		Interval theInterval = (Interval)theQuery.getSingleResult();
		
		return theInterval;
	}


}
