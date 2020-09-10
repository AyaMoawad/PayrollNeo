package com.dao.lookups;

import java.util.List;

import com.entities.lookups.InputValue;

public interface InputValueLookupDAO {
	public List<InputValue> getListOfInputValues();
	public InputValue getInputValueByName(String name);
	public InputValue getInputValueByCode(String code);
}
