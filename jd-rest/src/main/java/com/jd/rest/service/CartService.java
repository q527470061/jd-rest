package com.jd.rest.service;

import java.util.List;

import com.jd.pojo.CartItem;
import com.jd.pojo.JDResult;

public interface CartService {

	/*//添加临时购物车功能
	JDResult addTmpCart(String tmp_user_key,long itemId, int num);
	
	//删除临时购物车功能
	JDResult deleteTmpCartItem(String tmp_user_key,long itemId);
	
	//展示临时购物车功能
	List<CartItem> getTmpCartList(String tmp_user_key);*/
	
	//添加用户购物车功能
	JDResult addCart(Long userId,String tmp_user_key,long itemId, int num);
	
	//删除用户购物车功能
	JDResult deleteCartItem(Long userId,String tmp_user_key,long itemId);
	
	//展示用户购物车功能
	List<CartItem> getCartList(Long userId,String tmp_user_key);
	
	//更新合并购物车功能
	JDResult mergeCart(Long userId,String tmp_user_key);
	
	//清空购物车
	JDResult clearCart(Long userId,String tmp_user_key);
}
