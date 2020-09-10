package com.entities.payType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="paytype")
public class PayType {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="paytype_id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "paytype_commId_id")
	private PayTypeCommId commID;
	
	@Column(name="`interval`")
	private String interval;
	
	@Column(name="type")
	private String type;;
	
	@Column(name="inputvalue")
	private String inputvalue;
	
	@Column(name="taxes")
	private String taxes;
	
	@Column(name="gl_assignemnt")
	private String glAssignemnt;
	
	@Column(name="cost_center")
	private String costCenter;
	
	@Column(name="unit")
	private String unit;
	
	public PayType() {
		
	}

	public PayType(PayTypeCommId commID, String interval, String type, String inputvalue, String taxes,
			String glAssignemnt, String costCenter) {
		this.commID = commID;
		this.interval = interval;
		this.type = type;
		this.inputvalue = inputvalue;
		this.taxes = taxes;
		this.glAssignemnt = glAssignemnt;
		this.costCenter = costCenter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PayTypeCommId getCommID() {
		return commID;
	}

	public void setCommID(PayTypeCommId commID) {
		this.commID = commID;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInputvalue() {
		return inputvalue;
	}

	public void setInputvalue(String inputvalue) {
		this.inputvalue = inputvalue;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

	public String getGlAssignemnt() {
		return glAssignemnt;
	}

	public void setGlAssignemnt(String glAssignemnt) {
		this.glAssignemnt = glAssignemnt;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	
}
