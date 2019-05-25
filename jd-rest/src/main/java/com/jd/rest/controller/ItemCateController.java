package com.jd.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.rest.pojo.CateResult;
import com.jd.rest.service.ItemCateService;

@RestController
@RequestMapping("/rest")
public class ItemCateController {

	@Autowired
	private ItemCateService itemCateService;
	
/*	@SuppressWarnings("deprecation")
	@RequestMapping("/itemcate/list")
	public Object getItemCatList(String callback) {
		CateResult cateResult=itemCateService.getItemCatList();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(cateResult);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}*/
	
	@CrossOrigin(origins = "*")
	@RequestMapping("/itemcate/list")
	public CateResult getItemCatList() {
		return itemCateService.getItemCatList();
	}
}
