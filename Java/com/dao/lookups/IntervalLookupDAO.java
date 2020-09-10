package com.dao.lookups;

import java.util.List;

import com.entities.lookups.Interval;

public interface IntervalLookupDAO {
	
	public List<Interval> getListOfIntervals();
	public Interval getIntervalByName(String name);
	public Interval getIntervalByCode(String code);	
	
}
