package com.jd.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.pojo.JDResult;
import com.jd.rest.service.ContentService;

@RestController
@RequestMapping("/rest/content")
public class ContentController {

	/*
	 * 发布服务。接收查询参数。Restful风格内容分类id应该从url中取。 /rest/content/list/{contentCategoryId}
	 * 从url中取内容分类id，调用Service查询内容列表。返回内容列表。
	 */

	@Autowired
	private ContentService contentService;

	@CrossOrigin(origins = "*")
	@RequestMapping("/list/{contentCategoryId}")
	public JDResult getContentList(@PathVariable long contentCategoryId) {
		return contentService.getContentList(contentCategoryId);
	}
}
