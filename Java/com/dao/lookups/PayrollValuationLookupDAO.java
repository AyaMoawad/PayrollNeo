package com.dao.lookups;

import java.util.List;

import com.entities.lookups.PayrollValuation;

public interface PayrollValuationLookupDAO {
	public List<PayrollValuation> getListOfPayrollValuations();
	public PayrollValuation getPayrollValuationByName(String name);
	public PayrollValuation getPayrollValuationByCode(String code);
	
}
