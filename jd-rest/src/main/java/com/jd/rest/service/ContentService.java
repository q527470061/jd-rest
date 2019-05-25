package com.jd.rest.service;

import com.jd.pojo.JDResult;

public interface ContentService {
	//按内容分类获取内容
	JDResult getContentList(long contentCid);
}
