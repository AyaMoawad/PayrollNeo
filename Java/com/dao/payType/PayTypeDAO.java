package com.dao.payType;

import java.util.List;

import com.entities.payType.PayType;

public interface PayTypeDAO {

	public void addPayType(PayType payType);
	public PayType getPayType(String code);
	public PayType getPayTypeById(int id);
	public void deletePayType(String code);
	public List<PayType> getAllPayTypes();
	public PayType getPayTypeByName(String name);
}
