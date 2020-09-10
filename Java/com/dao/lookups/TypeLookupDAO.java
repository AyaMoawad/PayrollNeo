package com.dao.lookups;

import java.util.List;

import com.entities.lookups.Type;

public interface TypeLookupDAO {
	public List<Type> getListOfTypes();
	public Type getTypeByName(String name);
	public Type getTypeByCode(String code);
	
}
