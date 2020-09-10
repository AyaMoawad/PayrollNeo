package com.dao.payrollstruct;

import java.util.List;

import com.entities.payrollstruct.*;;

public interface PayrollStructDAO {

	public void addPayrollStruct(PayrollStruct PayrollStruct);
	public PayrollStruct getPayrollStruct(String code);
	public PayrollStruct getPayrollStructById(int id);
	public void deletePayrollStruct(String code);
	public List<PayrollStruct> getAllPayrollStructs();
}
