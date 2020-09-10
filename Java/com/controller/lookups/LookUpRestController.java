package com.controller.lookups;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.entities.lookups.Country;
import com.entities.lookups.Currency;
import com.entities.lookups.InputValue;
import com.entities.lookups.Interval;
import com.entities.lookups.PayrollValuation;
import com.entities.lookups.TaxesLookUp;
import com.entities.lookups.Type;
import com.service.lookups.LookUpsService;


@RestController
@RequestMapping("/lookUps")
public class LookUpRestController {
	@Autowired
	private LookUpsService lookUpsService;
	
	@RequestMapping(value = { "/getInputVals" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<InputValue> getInputVals() {	
		return lookUpsService.getAllInputValues();
	}

	
	@RequestMapping(value = { "/getIntervals" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Interval> getIntervals() {	
		return lookUpsService.getAllIntervals();
	}
	
	
	@RequestMapping(value = { "/getTypes" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Type> getTypes() {	
		return lookUpsService.getAllTypes();
	}
	
	@RequestMapping(value = { "/getPayrollValuations" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<PayrollValuation> getPayrollValuations() {	
		return lookUpsService.getAllPayrollValuations();
	}
	
	@RequestMapping(value = { "/getCountries" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Country> getCountries() {	
		return lookUpsService.getAllCountries();
	}
	
	@RequestMapping(value = { "/getCurrencies" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Currency> getCurrencies() {	
		return lookUpsService.getAllCurrencies();
		
	}
	
	@RequestMapping(value = { "/gettaxes" }, method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TaxesLookUp> gettaxes() {	
		return lookUpsService.getAllTaxesLookUps();
	}
		
}
