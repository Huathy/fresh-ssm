package com.hx.fresh.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hx.fresh.biz.ICartInfoBiz;
import com.hx.fresh.entity.CartInfo;
import com.hx.fresh.entity.GoodsInfo;
import com.hx.utils.ParamsUtil;

/**
 * 购物车信息控制层
 * @author Huathy
 * @date 2020年4月21日
 */
@RestController
@RequestMapping("cart")
public class CartInfoController {
	
	@Autowired
	private ICartInfoBiz cartBiz;
	
	@RequestMapping("countByMno")
	public int countByMno(Integer mno){
		return cartBiz.countByMno(mno);
	}
	
	@RequestMapping("delByMnoGno")
	public int delByMnoGno(Integer mno,Integer gno){
		try {
			if(ParamsUtil.checkNull(mno,gno) || ParamsUtil.checkGreater("<=", 0, mno,gno)){
				return -1;		//参数错误！
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;		//参数错误！
		}
		return cartBiz.delByMnoGno(mno,gno);
	}
	
	@RequestMapping("updateByMnoGno")
	public int updateByMnoGno(Integer mno,Integer gno,Integer num){
		try {
			if(ParamsUtil.checkNull(mno,gno,num) || ParamsUtil.checkGreater("<=", 0, mno,gno,num)){
				return -1;		//参数错误！
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;		//参数错误！
		}
		return cartBiz.updateByMnoGno(new CartInfo(mno,gno,num));
	}
	
	@RequestMapping("getAllByMno/{mno}")
	public List<GoodsInfo> getAllByMno(@PathVariable Integer mno,Integer page,Integer limit){
		if(ParamsUtil.checkNull(mno) || mno<=0 ){
			return Collections.emptyList();
		}
		List<GoodsInfo> list = cartBiz.getCartGoodsByMno(mno);
		return  list;
	}
	
	@RequestMapping("add")
	public int add(Integer mno,Integer gno,Integer num){
		try {		//对参数进行判断，是否合法
			if( ParamsUtil.checkNull(mno,gno,num) || ParamsUtil.checkGreater("<=", 0, mno,gno,num)){
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		CartInfo cart = new CartInfo(mno,gno,num);
		int result = cartBiz.add(cart);
		if(result > 0){
			return cartBiz.countByMno(mno);
		}
		return result;
	}
	
}
