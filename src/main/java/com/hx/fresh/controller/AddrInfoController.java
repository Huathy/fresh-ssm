package com.hx.fresh.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hx.fresh.biz.IAddrInfoBiz;
import com.hx.fresh.entity.AddrInfo;
import com.hx.utils.ParamsUtil;

/**
 * 地址信息控制器
 * @author Huathy
 * @date 2020年4月21日
 */
@RestController
@RequestMapping("addr")
public class AddrInfoController {
	@Autowired
	private IAddrInfoBiz addrBiz;
	
	@RequestMapping("delByAno")
	public int delByAno(Integer ano){
		if(ano==null || ano<=0){
			return -1;		//参数错误
		}
		return addrBiz.delByAno(ano);
	}
	
	@RequestMapping("modifyAddr")
	public int modifyAddrByAno(AddrInfo addr){
		if(ParamsUtil.checkNull(addr.getAno(),addr.getName(),addr.getTel(),addr.getProvince(),addr.getCity(),addr.getArea(),addr.getAddr())){
			return -1;
		}
		return addrBiz.modifyAddrByAno(addr);
	}
	
	@RequestMapping("changeDefault")
	public int changeDefault(Integer mno,Integer ano){
		try {
			if(ParamsUtil.checkNull(mno,ano) || ParamsUtil.checkGreater("<=", 0, mno,ano)){
				return -1;		//参数错误
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return addrBiz.changeDefault(mno,ano);
	}
	
	@RequestMapping("add")
	public int add(AddrInfo addr){
		if(ParamsUtil.checkNull(addr.getMno(),addr.getProvince(),addr.getCity(),addr.getCity(),addr.getAddr(),addr.getName(),addr.getTel())){
			return -2;
		}
		return addrBiz.add(addr);
	}
	
	@RequestMapping("getAddrByMno")
	public List<AddrInfo> getAddrByMno(Integer mno){
		if(mno==null || mno<=0){
			return Collections.emptyList();
		}
		return addrBiz.getAddrByMno(mno);
	}
	
}
