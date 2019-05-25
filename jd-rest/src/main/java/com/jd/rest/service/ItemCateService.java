package com.jd.rest.service;

import java.util.List;

import com.jd.rest.pojo.CateResult;

public interface ItemCateService {

	CateResult getItemCatList();
	
	List<?> getCateList(long parentId);
}
