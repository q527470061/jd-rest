package com.jd.rest.service;

import com.jd.pojo.JDResult;

public interface RedisService {

	 JDResult syncContent(long contentCid);
}
