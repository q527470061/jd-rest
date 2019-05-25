package com.jd.rest.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.jd.mapper.ItemMapper;
import com.jd.pojo.CartItem;
import com.jd.pojo.Item;
import com.jd.pojo.JDResult;
import com.jd.rest.dao.JedisClient;
import com.jd.rest.service.CartService;
import com.jd.util.JsonUtils;

@Service
public class CartServiceImp implements CartService {

	// reids
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_TMP_USER_CART}")
	private String REDIS_TMP_USER_CART;
	@Value("${REDIS_TMP_USER_EXPIRE}")
	private int REDIS_TMP_USER_EXPIRE;

	@Value("${REDIS_USER_CART}")
	private String REDIS_USER_CART;
	@Value("${REDIS_USER_CART_EXPIRE}")
	private int REDIS_USER_CART_EXPIRE;

	// item
	@Autowired
	private ItemMapper itemMapper;

	@Override
	public JDResult addCart(Long userId, String tmp_user_key, long itemId, int num) {
		// 取商品信息
		CartItem cartItem = null;
		// 取购物车商品列表
		List<CartItem> itemList = getCartList(userId, tmp_user_key);
		// 判断购物车商品列表中是否存在此商品
		for (CartItem cItem : itemList) {
			// 如果存在此商品
			if (cItem.getId() == itemId) {
				// 增加商品数量
				cItem.setNum(cItem.getNum() + num);
				cartItem = cItem;
				break;
			}
		}
		if (cartItem == null) {
			cartItem = new CartItem();
			// 根据商品id查询商品基本信息。
			Item item = itemMapper.selectByPrimaryKey(itemId);
			cartItem.setId(item.getId());
			cartItem.setTitle(item.getTitle());
			cartItem.setImage(item.getImage() == null ? "" : item.getImage().split(",")[0]);
			cartItem.setNum(num);
			cartItem.setPrice(item.getPrice());
			// 添加到购物车列表
			itemList.add(cartItem);
		}

		if (userId != null) {
			jedisClient.set(REDIS_USER_CART + ":" + userId, JsonUtils.objectToJson(itemList));
			jedisClient.expire(REDIS_USER_CART + ":" + userId, REDIS_TMP_USER_EXPIRE);
		} else {
			// 把购物车列表写入tmp cart
			jedisClient.set(REDIS_TMP_USER_CART + ":" + tmp_user_key, JsonUtils.objectToJson(itemList));
			jedisClient.expire(REDIS_TMP_USER_CART + ":" + tmp_user_key, REDIS_USER_CART_EXPIRE);
		}
		cartItem.setNum(num);
		return JDResult.ok(cartItem);
	}

	@Override
	public JDResult deleteCartItem(Long userId, String tmp_user_key, long itemId) {
		// 取购物车商品列表
		List<CartItem> itemList = getCartList(userId, tmp_user_key);
		// 从列表中找到此商品
		for (CartItem cartItem : itemList) {
			if (cartItem.getId() == itemId) {
				itemList.remove(cartItem);
				break;
			}
		}
		// 写入reids
		if (userId != null) {
			jedisClient.set(REDIS_USER_CART + ":" + userId, JsonUtils.objectToJson(itemList));
			jedisClient.expire(REDIS_USER_CART + ":" + userId, REDIS_TMP_USER_EXPIRE);
		} else {
			// 把购物车列表写入tmp cart
			jedisClient.set(REDIS_TMP_USER_CART + ":" + tmp_user_key, JsonUtils.objectToJson(itemList));
			jedisClient.expire(REDIS_TMP_USER_CART + ":" + tmp_user_key, REDIS_USER_CART_EXPIRE);
		}
		return JDResult.ok(itemList);
	}

	@Override
	public List<CartItem> getCartList(Long userId, String tmp_user_key) {
		// 登录，则从user cart取
		String json = "";
		if (userId != null) {
			json = jedisClient.get(REDIS_USER_CART + ":" + userId);
			// 未登录从tmp cart 取出
		} else {
			json = jedisClient.get(REDIS_TMP_USER_CART + ":" + tmp_user_key);
		}

		if (StringUtil.isEmpty(json)) {
			return new ArrayList<>();
		}
		// 把json转换成商品列表
		try {
			List<CartItem> list = JsonUtils.jsonToList(json, CartItem.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public JDResult mergeCart(Long userId, String tmp_user_key) {
		List<CartItem> tmpcart = null;
		List<CartItem> cart = null;

		try {
			// 分别取出临时购车，用户购物车中的数据
			// 临时购物车
			String tmpJson = jedisClient.get(REDIS_TMP_USER_CART + ":" + tmp_user_key);
			if (tmpJson != null) {
				tmpcart = JsonUtils.jsonToList(tmpJson, CartItem.class);
			}
			String userJson = jedisClient.get(REDIS_USER_CART + ":" + userId);
			if (userJson != null) {
				cart = JsonUtils.jsonToList(userJson, CartItem.class);
			}
			if (tmpcart != null) {
				for (CartItem tmpCartItem : tmpcart) {
					CartItem _cartItem = null;
					if (cart != null) {
						for (CartItem cartItem : cart) {
							// 如果存在此商品
							if (cartItem.getId() == tmpCartItem.getId()) {
								// 增加商品数量
								cartItem.setNum(cartItem.getNum() + tmpCartItem.getNum());
								_cartItem = cartItem;
								break;
							}
						}
						// 用户购物车不存在，添加整个item
						if (_cartItem == null) {
							cart.add(tmpCartItem);
						}
					} else {
						cart = tmpcart;
					}

				}
			}
			jedisClient.del(REDIS_TMP_USER_CART + ":" + tmp_user_key);
			jedisClient.set(REDIS_USER_CART + ":" + userId, JsonUtils.objectToJson(cart));
		} catch (Exception e) {
			e.printStackTrace();
			JDResult.build(500, e.getMessage());
		}

		return JDResult.ok();
	}

	@JmsListener(destination = "mergeCart")
	public void receiveMergeCart(Map<String, Object> message) {
		Long userId = (Long) message.get("userId");
		String tmp_user_key = (String) message.get("tmp_user_key");
		mergeCart(userId, tmp_user_key);
	}

	@Override
	public JDResult clearCart(Long userId, String tmp_user_key) {
		if (userId != null) {
			jedisClient.del(REDIS_USER_CART + ":" + userId);
			// 未登录从tmp cart 取出
		} else {
			jedisClient.del(REDIS_TMP_USER_CART + ":" + tmp_user_key);
		}
		return JDResult.ok();
	}

}
