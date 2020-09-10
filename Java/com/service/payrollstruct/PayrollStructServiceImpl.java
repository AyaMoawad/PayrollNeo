package com.service.payrollstruct;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.lookups.CountryLookupDAO;
import com.dao.lookups.CurrencyLookupDAO;
import com.dao.lookups.IntervalLookupDAO;
import com.dao.lookups.PayrollValuationLookupDAO;
import com.dao.payrollstruct.PayrollStructDAO;
import com.entities.lookups.Country;
import com.entities.lookups.Currency;
import com.entities.lookups.Interval;
import com.entities.lookups.PayrollValuation;
import com.entities.payrollstruct.PayrollStruct;
import com.entities.payrollstruct.PayrollStructCommId;
import com.models.payrollstruct.PayrollStructModel;
import com.rest.errorhandling.NotFoundException;
import com.rest.errorhandling.UniqunessException;

@Service
public class PayrollStructServiceImpl implements PayrollStructService{
	
	@Autowired
	private PayrollStructDAO payrollStructDAO;
	
	@Autowired 
	private IntervalLookupDAO intervalDAO;
	
	@Autowired 
	private CountryLookupDAO countryDAO;
	
	@Autowired 
	private CurrencyLookupDAO currencyDAO;
	
	@Autowired 
	private PayrollValuationLookupDAO payrollValDAO;
	
	
	@Override
	@Transactional
	public void addPayrollStruct(PayrollStructModel PayrollStructModel) throws ParseException {
		
		Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(PayrollStructModel.getStartDate());
		Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(PayrollStructModel.getEndDate());
		
		PayrollStructCommId commId=new PayrollStructCommId(startDate,endDate,PayrollStructModel.getCode(),PayrollStructModel.getName());
		commId.setDeleted(0);
		PayrollStruct PayrollStructObj = new PayrollStruct();
				
		Interval interval =intervalDAO.getIntervalByName(PayrollStructModel.getInterval());
		Country country = countryDAO.getCountryByName(PayrollStructModel.getCountry());
		Currency currency =currencyDAO.getCurrencyByName(PayrollStructModel.getCurrency());
		PayrollValuation payrollValuation =payrollValDAO.getPayrollValuationByName(PayrollStructModel.getPayrollValuation());
		
		PayrollStructObj.setCommID(commId);
		PayrollStructObj.setTaxSettlement(PayrollStructModel.getTaxSettlement());
		PayrollStructObj.setNoOfFixedDays(Integer.parseInt(PayrollStructModel.getNoOfDays()));
		PayrollStructObj.setCompany(null);	//integration
		PayrollStructObj.setCountry(country.getCode());
		PayrollStructObj.setCurrency(currency.getCode());
		PayrollStructObj.setInterval(interval.getCode());
		PayrollStructObj.setPayrollValuation(payrollValuation.getCode());
		
		try {
			payrollStructDAO.addPayrollStruct(PayrollStructObj);
		}catch(Exception e) {
			e.printStackTrace();
			throw new UniqunessException("PayrollStruct with code:"+PayrollStructModel.getCode()+ " is already saved!");}
	}
	
	
	@Override
	@Transactional
	public PayrollStructModel getPayrollStruct(String code) {
		try {
			PayrollStructModel PayrollStructModel= new PayrollStructModel();
			PayrollStruct PayrollStruct = payrollStructDAO.getPayrollStruct(code);
			DateFormat dateFormat = new SimpleDateFormat();

			Interval interval =intervalDAO.getIntervalByCode(PayrollStruct.getInterval());
			Country country = countryDAO.getCountryByCode(PayrollStruct.getCountry());
			Currency currency =currencyDAO.getCurrencyByCode(PayrollStruct.getCurrency());
			PayrollValuation payrollValuation =payrollValDAO.getPayrollValuationByCode(PayrollStruct.getPayrollValuation());
			
			
			String startDate = dateFormat.format(PayrollStruct.getCommID().getStartDate());
			String endDate = dateFormat.format(PayrollStruct.getCommID().getEndDate());
			
			PayrollStructModel.setCode(code);
			PayrollStructModel.setEndDate(endDate.substring(0, 6));
			PayrollStructModel.setStartDate(startDate.substring(0, 6));
			PayrollStructModel.setName(PayrollStruct.getCommID().getName());
			PayrollStructModel.setCountry(country.getName());
			PayrollStructModel.setCurrency(currency.getName());
			PayrollStructModel.setPayrollValuation(payrollValuation.getName());
			PayrollStructModel.setInterval(interval.getName());
			PayrollStructModel.setTaxSettlement(PayrollStruct.getTaxSettlement());
			PayrollStructModel.setNoOfDays( Integer.toString(PayrollStruct.getNoOfFixedDays()));
		return PayrollStructModel;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException("There is no PayrollStruct with code: "+code+" exsits!");
		}
	}
	
	@Override
	@Transactional
	public void deletePayrollStruct(String code) {
		try {
			payrollStructDAO.deletePayrollStruct(code);	
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException("Cannot Delete! -the PayrollStruct code :"+code+" is not saved");
		}
	}
	
	@Override
	@Transactional
	public void delimitPayrollStruct(String code, String endDate) throws ParseException {
		Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		PayrollStruct PayrollStruct;
		try {
			PayrollStruct =payrollStructDAO.getPayrollStruct(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot delimit PayrollStruct with code:"+code+" not found!");
		}	
		
		PayrollStruct.getCommID().setEndDate(enddate);
		PayrollStruct.getCommID().setDeleted(1);
	}
	
	@Override
	@Transactional
	public void updatePayrollStructData(PayrollStructModel PayrollStructModel) throws ParseException {
		if(PayrollStructModel.getCode()!=null	&& PayrollStructModel.getEndDate() !=null	&& PayrollStructModel.getStartDate() != null
			&& PayrollStructModel.getName() !=null && PayrollStructModel.getInterval() !=null&&
			PayrollStructModel.getCountry()!=null && PayrollStructModel.getCurrency()!=null 
			&&PayrollStructModel.getPayrollValuation()!=null ) {
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(PayrollStructModel.getStartDate());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(PayrollStructModel.getEndDate());
			String code = PayrollStructModel.getCode();
			PayrollStruct PayrollStruct = payrollStructDAO.getPayrollStruct(code);
			String intervalCode =intervalDAO.getIntervalByName(PayrollStructModel.getInterval()).getCode() ;
			String currencyCode = currencyDAO.getCurrencyByName(PayrollStructModel.getCurrency()).getCode();
			String countryCode = countryDAO.getCountryByName(PayrollStructModel.getCountry()).getCode();
			String payrollValCode = payrollValDAO.getPayrollValuationByName(PayrollStructModel.getPayrollValuation()).getCode();
			
			//Update the data of this payrollStruct 
			PayrollStruct.getCommID().setName(PayrollStructModel.getName());
			PayrollStruct.getCommID().setStartDate(startDate);
			PayrollStruct.getCommID().setEndDate(endDate);
			PayrollStruct.setInterval(intervalCode);
			PayrollStruct.setCountry(countryCode);
			PayrollStruct.setCurrency(currencyCode);
			PayrollStruct.setPayrollValuation(payrollValCode);
			PayrollStruct.setTaxSettlement(PayrollStructModel.getTaxSettlement());
			PayrollStruct.setNoOfFixedDays( Integer.parseInt(PayrollStructModel.getNoOfDays()));
	
		}
		
	}


	@Override
	@Transactional
	public List<PayrollStructModel> getAllPayrollStruct() {
		try {
			List<PayrollStructModel>  PayrollStructModel= new ArrayList<PayrollStructModel>();
			List<PayrollStruct> PayrollStruct = payrollStructDAO.getAllPayrollStructs();
			for(int i=0;i<PayrollStruct.size();i++) {
				DateFormat dateFormat = new SimpleDateFormat();
				PayrollStructModel model= new PayrollStructModel();
				Interval interval =intervalDAO.getIntervalByCode(PayrollStruct.get(i).getInterval());
				Country country = countryDAO.getCountryByCode(PayrollStruct.get(i).getCountry());
				Currency currency =currencyDAO.getCurrencyByCode(PayrollStruct.get(i).getCurrency());
				PayrollValuation payrollValuation =payrollValDAO.getPayrollValuationByCode(PayrollStruct.get(i).getPayrollValuation());
				
				
				String startDate = dateFormat.format(PayrollStruct.get(i).getCommID().getStartDate());
				String endDate = dateFormat.format(PayrollStruct.get(i).getCommID().getEndDate());
				
				model.setCode(PayrollStruct.get(i).getCommID().getCode());
				model.setEndDate(endDate.substring(0, 6));
				model.setStartDate(startDate.substring(0, 6));
				model.setName(PayrollStruct.get(i).getCommID().getName());
				model.setCountry(country.getName());
				model.setCurrency(currency.getName());
				model.setPayrollValuation(payrollValuation.getName());
				model.setInterval(interval.getName());
				model.setTaxSettlement(PayrollStruct.get(i).getTaxSettlement());
				model.setNoOfDays( Integer.toString(PayrollStruct.get(i).getNoOfFixedDays()));
				PayrollStructModel.add(model);
			}
			
		return PayrollStructModel;
		}catch(Exception e) {
			e.printStackTrace();return null;
		}
	}
	
}
	
