package com.entities.lookups;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country {

	@Id
	@Column(name="country_id")
	private String id;
	
	@Column(name ="code")
	private String code;
	
	@Column(name="name")
	private String name;
	
	public Country() {
	}
	
	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	

}
