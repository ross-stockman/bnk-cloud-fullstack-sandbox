package org.rstockman.bnk.api.vendor.controller;

import java.util.List;

import org.rstockman.bnk.api.vendor.dto.VendorRequestParams;
import org.rstockman.bnk.api.vendor.dto.VendorResult;
import org.rstockman.bnk.common.dao.SimpleDAO;
import org.rstockman.bnk.common.exceptions.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendors")
public class VendorResource {

	@Autowired
	private SimpleDAO<VendorResult, VendorRequestParams, String> dao;

	@GetMapping("/{id}")
	public VendorResult getVendor(@PathVariable String id) {
		var obj = dao.get(id);
		if (obj.isEmpty()) {
			throw ExceptionFactory.resourceNotFound();
		}
		return obj.get();
	}

	@GetMapping
	public List<VendorResult> getAll(VendorRequestParams params) {
		return dao.getAll(params);
	}

	@PostMapping
	public String create(@RequestBody VendorResult obj) {
		return dao.create(obj);
	}

	@PutMapping("/{id}")
	public void put(@PathVariable String key, @RequestBody VendorResult obj) {
		dao.put(key, obj);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String key) {
		dao.delete(key);
	}
}