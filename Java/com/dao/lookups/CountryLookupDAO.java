package com.dao.lookups;

import java.util.List;

import com.entities.lookups.Country;

public interface CountryLookupDAO {
	public List<Country> getListOfCountrys();
	public Country getCountryByName(String name);
	public Country getCountryByCode(String code);
}
