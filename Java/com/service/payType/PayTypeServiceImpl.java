package com.service.payType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.lookups.InputValueLookupDAO;
import com.dao.lookups.IntervalLookupDAO;
import com.dao.lookups.TaxesLookupDAO;
import com.dao.lookups.TypeLookupDAO;
import com.dao.payType.PayTypeDAO;
import com.entities.lookups.InputValue;
import com.entities.lookups.Interval;
import com.entities.lookups.TaxesLookUp;
import com.entities.lookups.Type;
import com.entities.payType.PayType;
import com.entities.payType.PayTypeCommId;
import com.models.payType.PayTypeModel;
import com.rest.errorhandling.NotFoundException;
import com.rest.errorhandling.UniqunessException;

@Service
public class PayTypeServiceImpl implements PayTypeService{
	@Autowired
	private PayTypeDAO payTypeDAO;
	
	@Autowired
	private InputValueLookupDAO inputValueDAO;
	
	@Autowired 
	private IntervalLookupDAO intervalDAO;
	
	@Autowired
	private TypeLookupDAO typeDAO;
	
	@Autowired
	private TaxesLookupDAO taxesDAO;
	
	
	@Override
	@Transactional
	public void addPayType(PayTypeModel payTypeModel) throws ParseException {
		
		Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(payTypeModel.getStartDate());
		Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(payTypeModel.getEndDate());
		
		PayTypeCommId commId=new PayTypeCommId(startDate,endDate,payTypeModel.getCode(),payTypeModel.getName());
		commId.setDeleted(0);
		PayType payTypeObj = new PayType();
				
		InputValue inputValue = inputValueDAO.getInputValueByName(payTypeModel.getInputValue());
		Type type = typeDAO.getTypeByName(payTypeModel.getType());
		Interval interval =intervalDAO.getIntervalByName(payTypeModel.getInterval());
		TaxesLookUp taxes =taxesDAO.getTaxesLookUpByName(payTypeModel.getTaxes());
		
		if(payTypeModel.getInputValue().equals("calculated")||payTypeModel.getInputValue().equals("value")) {
			payTypeObj.setUnit(null);
		}else {payTypeObj.setUnit(payTypeModel.getUnit());}
		payTypeObj.setCommID(commId);
		payTypeObj.setCostCenter(null);
		payTypeObj.setGlAssignemnt(null);
		payTypeObj.setTaxes(taxes.getCode());
		payTypeObj.setInputvalue(inputValue.getCode());
		payTypeObj.setInterval(interval.getCode());
		payTypeObj.setType(type.getCode());
		
		
		try {
			payTypeDAO.addPayType(payTypeObj);
		}catch(Exception e) {throw new UniqunessException("payType with code:"+payTypeModel.getCode()+ " is already saved!");}
	}
	
	
	@Override
	@Transactional
	public PayTypeModel getPayType(String code) {
		try {
			PayTypeModel payTypeModel= new PayTypeModel();
			PayType payType = payTypeDAO.getPayType(code);
			DateFormat dateFormat = new SimpleDateFormat();

			InputValue inputValue = inputValueDAO.getInputValueByCode(payType.getInputvalue());
			Type type = typeDAO.getTypeByCode(payType.getType());
			Interval interval =intervalDAO.getIntervalByCode(payType.getInterval());
			TaxesLookUp taxes = taxesDAO.getTaxesLookUpByCode(payType.getTaxes());
			
			String startDate = dateFormat.format(payType.getCommID().getStartDate());
			String endDate = dateFormat.format(payType.getCommID().getEndDate());
			
			payTypeModel.setCode(code);
			payTypeModel.setEndDate(endDate.substring(0, 6));
			payTypeModel.setStartDate(startDate.substring(0, 6));
			payTypeModel.setName(payType.getCommID().getName());
			payTypeModel.setInputValue(inputValue.getName());
			payTypeModel.setInterval(interval.getName());
			payTypeModel.setType(type.getName());
			payTypeModel.setUnit(payType.getUnit());
			payTypeModel.setTaxes(taxes.getName());
			
			return payTypeModel;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException("There is no payType with code: "+code+" exsits!");
		}
	}
	
	@Override
	@Transactional
	public PayTypeModel getPayTypeByName(String name) {
		try {
			PayTypeModel payTypeModel= new PayTypeModel();
			PayType payType = payTypeDAO.getPayTypeByName(name);
			DateFormat dateFormat = new SimpleDateFormat();

			InputValue inputValue = inputValueDAO.getInputValueByCode(payType.getInputvalue());
			Type type = typeDAO.getTypeByCode(payType.getType());
			Interval interval =intervalDAO.getIntervalByCode(payType.getInterval());
			TaxesLookUp taxes = taxesDAO.getTaxesLookUpByCode(payType.getTaxes());

			String startDate = dateFormat.format(payType.getCommID().getStartDate());
			String endDate = dateFormat.format(payType.getCommID().getEndDate());
			
			payTypeModel.setCode(payType.getCommID().getCode());
			payTypeModel.setEndDate(endDate.substring(0, 6));
			payTypeModel.setStartDate(startDate.substring(0, 6));
			payTypeModel.setName(payType.getCommID().getName());
			payTypeModel.setInputValue(inputValue.getName());
			payTypeModel.setInterval(interval.getName());
			payTypeModel.setType(type.getName());
			payTypeModel.setUnit(payType.getUnit());
			payTypeModel.setTaxes(taxes.getName());

			return payTypeModel;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException("There is no payType with name: "+name+" exsits!");
		}
	}
	
	@Override
	@Transactional
	public void deletePayType(String code) {
		try {
			payTypeDAO.deletePayType(code);	
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException("Cannot Delete! -the PayType code :"+code+" is not saved");
		}
	}
	
	@Override
	@Transactional
	public void delimitPayType(String code, String endDate) throws ParseException {
		Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		PayType payType;
		try {
			payType =payTypeDAO.getPayType(code);
		}catch(Exception e) {
			throw new NotFoundException("Cannot delimit PayType with code:"+code+" not found!");
		}	
		
		payType.getCommID().setEndDate(enddate);
		payType.getCommID().setDeleted(1);
	}
	
	@Override
	@Transactional
	public void updatePayTypeData(PayTypeModel payTypeModel) throws ParseException {
		if(payTypeModel.getCode()!=null	&& payTypeModel.getEndDate() !=null	&& payTypeModel.getStartDate() != null
			&& payTypeModel.getName() !=null && payTypeModel.getInputValue() !=null	&& payTypeModel.getInterval() !=null
			&& payTypeModel.getType() !=null) {
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(payTypeModel.getStartDate());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(payTypeModel.getEndDate());
			String code = payTypeModel.getCode();
			PayType payType = payTypeDAO.getPayType(code);
			String intervalCode =intervalDAO.getIntervalByName(payTypeModel.getInterval()).getCode() ;
			String typeCode = typeDAO.getTypeByName(payTypeModel.getType()).getCode();
			String intputValCode = inputValueDAO.getInputValueByName(payTypeModel.getInputValue()).getCode();
			TaxesLookUp taxes = taxesDAO.getTaxesLookUpByCode(payType.getTaxes());

			payType.getCommID().setName(payTypeModel.getName());
			payType.getCommID().setStartDate(startDate);
			payType.getCommID().setEndDate(endDate);
			payType.setInputvalue(intputValCode);
			payType.setType(typeCode);
			payType.setInterval(intervalCode);
			payType.setUnit(payTypeModel.getUnit());
			payType.setTaxes(taxes.getCode());
			payTypeDAO.addPayType(payType);
		
		}
	}


	@Override
	@Transactional
	public List<PayTypeModel> getAllPayTypes() {
		List<PayType> payTypeObj= new ArrayList<PayType>();
		try {
			payTypeObj = payTypeDAO.getAllPayTypes();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		List<PayTypeModel> payTypeModel = new ArrayList<PayTypeModel>();
		for(int i=0;i<payTypeObj.size();i++) {
			PayTypeModel payType =new PayTypeModel();
			DateFormat dateFormat = new SimpleDateFormat();
			InputValue inputValue = inputValueDAO.getInputValueByCode(payTypeObj.get(i).getInputvalue());
			Type type = typeDAO.getTypeByCode(payTypeObj.get(i).getType());
			Interval interval =intervalDAO.getIntervalByCode(payTypeObj.get(i).getInterval());
			
			
			String startDate = dateFormat.format(payTypeObj.get(i).getCommID().getStartDate());
			String endDate = dateFormat.format(payTypeObj.get(i).getCommID().getEndDate());
			
			payType.setCode(payTypeObj.get(i).getCommID().getCode());
			payType.setEndDate(endDate.substring(0, 6));
			payType.setStartDate(startDate.substring(0, 6));
			payType.setName(payTypeObj.get(i).getCommID().getName());
			payType.setInputValue(inputValue.getName());
			payType.setInterval(interval.getName());
			payType.setType(type.getName());
			payType.setUnit(payTypeObj.get(i).getUnit());
			payType.setTaxes(payTypeObj.get(i).getTaxes());

			payTypeModel.add(payType);
		}
		return payTypeModel;
		
	}
	
	
}
