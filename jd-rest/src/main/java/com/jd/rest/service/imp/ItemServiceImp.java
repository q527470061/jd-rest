package com.jd.rest.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jd.mapper.ItemDescMapper;
import com.jd.mapper.ItemMapper;
import com.jd.mapper.ItemParamItemMapper;
import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;
import com.jd.pojo.ItemExample;
import com.jd.pojo.ItemParamItem;
import com.jd.pojo.ItemParamItemExample;
import com.jd.pojo.ItemParamItemExample.Criteria;
import com.jd.pojo.JDResult;
import com.jd.rest.pojo.SearchResult;
import com.jd.rest.service.ItemService;

@Service
public class ItemServiceImp implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private ItemParamItemMapper itemParamItemMapper;

	@Override
	public JDResult getItem(long itemId) {
		// 根据商品id查询商品信息
		Item item = itemMapper.selectByPrimaryKey(itemId);

		return JDResult.ok(item);
	}

	@Override
	public JDResult getItemDesc(long itemId) {
		ItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		return JDResult.ok(itemDesc.getItemDesc());
	}

	@Override
	public String getItemParamItem(long itemId) {
		ItemParamItemExample example = new ItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<ItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (!list.isEmpty()) {
			ItemParamItem itemParamItem = list.get(0);
			return itemParamItem.getParamData();
		}
		return null;
	}

	@Override
	public JDResult getItemsByCateId(long cateId, Integer page, Integer rows) {
		ItemExample example = new ItemExample();
		com.jd.pojo.ItemExample.Criteria criteria = example.createCriteria();
		criteria.andCidEqualTo(cateId);
		// 分页处理
		PageHelper.startPage(page, rows);
		List<Item> list = itemMapper.selectByExample(example);
		// 创建一个返回值对象
		SearchResult result = new SearchResult();
		result.setItemList(list);
		// 取记录总条数
		PageInfo<Item> pageInfo = new PageInfo<>(list);
		result.setRecordCount(pageInfo.getTotal());
		//总页数
		result.setPageCount(pageInfo.getPages());
		return JDResult.ok(result);
	}

}
