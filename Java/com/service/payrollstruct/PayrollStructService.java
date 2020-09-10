package com.service.payrollstruct;

import java.text.ParseException;
import java.util.List;

import com.models.payrollstruct.PayrollStructModel;

public interface PayrollStructService {
	public void addPayrollStruct(PayrollStructModel PayrollStructModel) throws ParseException;
	public PayrollStructModel getPayrollStruct(String code);
	public void deletePayrollStruct(String code);
	public void delimitPayrollStruct(String code, String endDate) throws ParseException;
	public void updatePayrollStructData(PayrollStructModel PayrollStructModel) throws ParseException;
	public List<PayrollStructModel> getAllPayrollStruct();
}
