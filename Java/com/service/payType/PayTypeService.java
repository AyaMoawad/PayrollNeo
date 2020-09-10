package com.service.payType;

import java.text.ParseException;
import java.util.List;

import com.models.payType.PayTypeModel;

public interface PayTypeService {
	public void addPayType(PayTypeModel payTypeModel) throws ParseException;
	public PayTypeModel getPayType(String code);
	public void deletePayType(String code);
	public void delimitPayType(String code, String endDate) throws ParseException;
	public void updatePayTypeData(PayTypeModel payTypeModel) throws ParseException;
	public List<PayTypeModel> getAllPayTypes();
	public PayTypeModel getPayTypeByName(String name);
}
