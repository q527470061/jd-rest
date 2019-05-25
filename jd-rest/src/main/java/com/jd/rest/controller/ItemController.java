package com.jd.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jd.pojo.JDResult;
import com.jd.rest.pojo.SearchResult;
import com.jd.rest.service.ItemService;

@RequestMapping("/rest/item")
@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;

	@CrossOrigin(origins = "*")
	@RequestMapping("/base/{itemId}")
	public JDResult getItem(@PathVariable long itemId) {
		return itemService.getItem(itemId);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping("/desc/{itemId}")
	public JDResult getItemDesc(@PathVariable long itemId) {
		return itemService.getItemDesc(itemId);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping("/param/{itemId}")
	public String getItemParamItem(@PathVariable long itemId) {
		return itemService.getItemParamItem(itemId);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping("/list")
	public JDResult getItemsByCateId(@RequestParam("q") long cateId, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "5") Integer rows) {

		return itemService.getItemsByCateId(cateId, page, rows);
	}

}
