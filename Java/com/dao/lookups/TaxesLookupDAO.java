package com.dao.lookups;

import java.util.List;

import com.entities.lookups.TaxesLookUp;

public interface TaxesLookupDAO {
	public List<TaxesLookUp> getListOfTaxesLookUp();
	public TaxesLookUp getTaxesLookUpByName(String name);
	public TaxesLookUp getTaxesLookUpByCode(String code);
}
