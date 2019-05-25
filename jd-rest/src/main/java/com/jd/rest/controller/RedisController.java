package com.jd.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jd.pojo.JDResult;
import com.jd.rest.service.RedisService;

@RequestMapping("/redis/sync")
@Controller
public class RedisController {

	@Autowired
	private RedisService redisService;

	// 内容缓存同步
	@RequestMapping("/content/{contentCid}")
	public JDResult contentCacheSync(@PathVariable long contentCid) {
		return redisService.syncContent(contentCid);
	}
}
