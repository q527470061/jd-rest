package com.jd.rest.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.mapper.ItemCatMapper;
import com.jd.pojo.ItemCat;
import com.jd.pojo.ItemCatExample;
import com.jd.pojo.ItemCatExample.Criteria;
import com.jd.rest.pojo.CateNode;
import com.jd.rest.pojo.CateResult;
import com.jd.rest.service.ItemCateService;

@Service
public class ItemCateServiceImp implements ItemCateService {

	/*
	 * 查询商品所有分类 一级分类，二级分类，三级分类即叶子分类 而关于分类的设计，恰好是一个树状结构， 对于树状结构，本能反应是遍历树，用迭代的方式
	 */

	@Autowired
	private ItemCatMapper itemCatMapper;

	// 查询所有分类
	public CateResult getItemCatList() {
		CateResult catResult = new CateResult();
		// 查询分类列表
		catResult.setData(getCateList(0));
		return catResult;
	}

	// 按父類parendId进行查找其所有子类
	public List<?> getCateList(long parentId) {
		ItemCatExample example = new ItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<ItemCat> list = itemCatMapper.selectByExample(example);
		// 因为要查找所有子类的及其子类的子类所以要遍历其子类，并将存入一个List内
		List resultList = new ArrayList<>();
		for (ItemCat itemCate : list) {
			// 判断其是否为父节点
			if (itemCate.getIsParent()) {
				// 每个节点存入Cate中，因为cate最后作为结果集中的一个对象
				CateNode cateNode = new CateNode();
				/*
				 * 根据前台页面索要显示的内容 n,u https://jiadian.jd.com/ 一级分类二级分类内容不做了，直接跳到京东的
				 */
				cateNode.setId(itemCate.getId());
				cateNode.setName(itemCate.getName());
				cateNode.setUrl("https://jiadian.jd.com/");
				// 子节点
				cateNode.setItem(getCateList(itemCate.getId()));

				resultList.add(cateNode);
			} else {
				/*
				 * 京东的地址格式 https://list.jd.com/list.html?cat=9987,653,655 改成/list/343434.html
				 */
				CateNode cateNode = new CateNode();
				cateNode.setId(itemCate.getId());
				cateNode.setName(itemCate.getName());
				cateNode.setUrl("/list/" + itemCate.getId() + ".html");
				resultList.add(cateNode);
			}
		}
		return resultList;
	}

}
