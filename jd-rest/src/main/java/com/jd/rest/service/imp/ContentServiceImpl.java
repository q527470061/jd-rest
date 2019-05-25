package com.jd.rest.service.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jd.mapper.ContentMapper;
import com.jd.pojo.Content;
import com.jd.pojo.ContentExample;
import com.jd.pojo.ContentExample.Criteria;
import com.jd.pojo.JDResult;
import com.jd.rest.dao.JedisClient;
import com.jd.rest.service.ContentService;
import com.jd.util.ExceptionUtil;
import com.jd.util.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${TB_REDIS_CONTENT}")
	private String TB_REDIS_CONTENT;

	@Override
	public JDResult getContentList(long contentCid) {
		// 从缓存中取内容
		try {
			String result = jedisClient.hget(TB_REDIS_CONTENT, contentCid + "");
			if (!StringUtils.isBlank(result)) {
				// 把字符串转换成list
				List<Content> resultList = JsonUtils.jsonToList(result, Content.class);
				return JDResult.ok(resultList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Content> list = null;
		try {
			// 根据内容分类id查询内容列表
			ContentExample example = new ContentExample();
			Criteria criteria = example.createCriteria();
			criteria.andCategoryIdEqualTo(contentCid);
			// 执行查询
			list = contentMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return JDResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		// 向缓存中添加内容
		try {
			// 把list转换成字符串
			String cacheString = JsonUtils.objectToJson(list);
			jedisClient.hset(TB_REDIS_CONTENT, contentCid + "", cacheString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return JDResult.ok(list);
	}

}
