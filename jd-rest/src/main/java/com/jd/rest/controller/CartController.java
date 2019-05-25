package com.jd.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jd.pojo.JDResult;
import com.jd.rest.service.CartService;

@RequestMapping("/rest/cart")
@RestController
public class CartController {

	@Autowired
	private CartService cartService;

	@RequestMapping("/add/{itemId}")
	public JDResult addCart(@PathVariable long itemId, Long userId, String tmp_user_key,
			@RequestParam(defaultValue = "1") int num) {
		return cartService.addCart(userId, tmp_user_key, itemId, num);
	}

	@RequestMapping("/mergeCart")
	public JDResult mergeCart(Long userId, String tmp_user_key) {
		return cartService.mergeCart(userId, tmp_user_key);
	}

	@RequestMapping("/list")
	public JDResult getCartList(Long userId, String tmp_user_key) {
		return JDResult.ok(cartService.getCartList(userId, tmp_user_key));
	}

	@RequestMapping("/delete/{itemId}")
	public JDResult deleteCartItem(@PathVariable long itemId, Long userId, String tmp_user_key) {
		return cartService.deleteCartItem(userId, tmp_user_key, itemId);
	}

	@RequestMapping("/clear")
	public JDResult clearCart(Long userId, String tmp_user_key) {
		return cartService.clearCart(userId, tmp_user_key);
	}

}
