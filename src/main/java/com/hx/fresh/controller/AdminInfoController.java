package com.hx.fresh.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hx.fresh.biz.IAdminInfoBiz;
import com.hx.fresh.entity.AdminInfo;
import com.hx.utils.ParamsUtil;

/**
 * 管理员控制层
 * @author Huathy
 * @date 2020年3月31日
 */
@RestController
@RequestMapping("/admin")
public class AdminInfoController {
	@Autowired
	private IAdminInfoBiz adminBiz;
	
	@RequestMapping("/updatePwd")
	public int updatePwd(@RequestParam(required=true)Integer aid){
		if(ParamsUtil.checkNull(aid) || aid<=0){
			return -1;
		}
		return adminBiz.updatePwd(aid);
	}
	
	@RequestMapping("/findAll")
	public List<AdminInfo> findAll(){
		return adminBiz.findAll();
	}
	
	@RequestMapping("delByAid")
	public int delByAid(Integer aid){
		return adminBiz.delByAid(aid);
	}
	
	@RequestMapping("/add")
	public int add(AdminInfo af){
		if(ParamsUtil.checkNull(af.getAname(),af.getPwd(),af.getTel())){
			return -1;		//参数为空
		}
		return adminBiz.add(af);
	}
	
	@RequestMapping("/login")
	public int login(AdminInfo af,HttpSession session){
		if(ParamsUtil.checkNull(af.getAname(),af.getPwd())){
			return -1;
		}
		AdminInfo admin = adminBiz.login(af);
		if(admin != null){
			session.setAttribute("currentLoginAdmin", admin);
			return 1;
		}
		return -2;	//账号密码错误
	}
	
	@RequestMapping("/checkLogin")
	public AdminInfo checkLogin(HttpSession session){
		Object admin = session.getAttribute("currentLoginAdmin");
		if(admin == null){
			return null;
		}
		return (AdminInfo) admin;
	}
	
}
