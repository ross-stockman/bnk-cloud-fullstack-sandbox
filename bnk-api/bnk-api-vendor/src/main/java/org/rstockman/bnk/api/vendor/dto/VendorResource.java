package org.rstockman.bnk.api.vendor.dto;

import java.util.Date;

import org.rstockman.bnk.common.dto.StandardResourceDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VendorResource extends StandardResourceDTO {
	private Long id;
	private String name;

	public VendorResource() {
		super();
	}

	public VendorResource(Long id, String name, String key, String version, Date created, Date updated) {
		super();
		this.id = id;
		this.name = name;
		setKey(key);
		setVersion(version);
		setCreated(created);
		setUpdated(updated);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
