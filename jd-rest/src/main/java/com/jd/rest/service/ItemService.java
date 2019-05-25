package com.jd.rest.service;

import com.jd.pojo.JDResult;


public interface ItemService {
	
	//获取商品基本信息
	JDResult getItem(long itemId);
	//获取商品描述
	JDResult getItemDesc(long itemId);
	//获取商品参数
	String getItemParamItem(long itemId);
	//按分类获取商品
	JDResult getItemsByCateId(long cateId, Integer page, Integer rows);
}


   