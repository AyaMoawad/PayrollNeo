package com.dao.lookups;

import java.util.List;

import com.entities.lookups.Currency;

public interface CurrencyLookupDAO {
	public List<Currency> getListOfCurrencys();
	public Currency getCurrencyByName(String name);
	public Currency getCurrencyByCode(String code);
}
