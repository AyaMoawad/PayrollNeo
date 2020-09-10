package com.service.lookups;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.lookups.CountryLookupDAO;
import com.dao.lookups.CurrencyLookupDAO;
import com.dao.lookups.InputValueLookupDAO;
import com.dao.lookups.IntervalLookupDAO;
import com.dao.lookups.PayrollValuationLookupDAO;
import com.dao.lookups.TaxesLookupDAO;
import com.dao.lookups.TypeLookupDAO;
import com.entities.lookups.Country;
import com.entities.lookups.Currency;
import com.entities.lookups.InputValue;
import com.entities.lookups.Interval;
import com.entities.lookups.PayrollValuation;
import com.entities.lookups.TaxesLookUp;
import com.entities.lookups.Type;

@Service
public class LookUpsService {
	@Autowired
	private InputValueLookupDAO inputValDAO;
	
	@Autowired
	private IntervalLookupDAO intervalDAO;
	
	@Autowired
	private TypeLookupDAO typeDAO;
	
	@Autowired
	private PayrollValuationLookupDAO payrollValDAO;
	
	@Autowired
	private CountryLookupDAO countryDAO;
	
	@Autowired
	private CurrencyLookupDAO currencyDAO;
	
	@Autowired
	private TaxesLookupDAO taxesDAO;
	
	@Transactional
	public List<InputValue> getAllInputValues() {
		return inputValDAO.getListOfInputValues();
	}
	@Transactional
	public List<Interval> getAllIntervals() {
		return intervalDAO.getListOfIntervals();
	}
	@Transactional
	public List<Type> getAllTypes() {
		return typeDAO.getListOfTypes();
	}
	@Transactional
	public List<PayrollValuation> getAllPayrollValuations() {
		return payrollValDAO.getListOfPayrollValuations();
	}

	@Transactional
	public List<Country> getAllCountries() {
		return countryDAO.getListOfCountrys();
	}
	
	@Transactional
	public List<Currency> getAllCurrencies() {
		return currencyDAO.getListOfCurrencys();
		
	}
	
	@Transactional
	public List<TaxesLookUp> getAllTaxesLookUps() {
		return taxesDAO.getListOfTaxesLookUp();
	}

	
}
