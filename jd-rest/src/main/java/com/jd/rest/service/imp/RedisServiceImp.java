package com.jd.rest.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jd.pojo.JDResult;
import com.jd.rest.dao.JedisClient;
import com.jd.rest.service.RedisService;
import com.jd.util.ExceptionUtil;

@Service
public class RedisServiceImp implements RedisService{
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${TB_REDIS_CONTENT}")
	private String TB_REDIS_CONTENT;

	/*
	 * 缓存同步
	 * (non-Javadoc)
	 * @see com.jd.rest.service.RedisService#syncContent(long)
	 */
	
	@Override
	public JDResult syncContent(long contentCid) {
		try {
			jedisClient.hdel(TB_REDIS_CONTENT, contentCid + "");
		} catch (Exception e) {
			e.printStackTrace();
			return JDResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return JDResult.ok();

	}

}
