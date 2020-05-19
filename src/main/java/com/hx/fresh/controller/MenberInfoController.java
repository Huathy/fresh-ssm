package com.hx.fresh.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hx.fresh.biz.IMenberInfoBiz;
import com.hx.fresh.entity.MenberInfo;
import com.hx.utils.ParamsUtil;

/**
 * 会员信息控制类
 * @author Huathy
 * @date 2020年3月31日
 */
@RestController
@RequestMapping("/menber")
public class MenberInfoController {
	@Autowired
	private IMenberInfoBiz menberBiz;
	
	@RequestMapping("reSetMno")
	public int reSetMno(Integer mno){
		if(mno == null || mno<=0){
			return -1;
		}
		return menberBiz.reSetMno(mno);
	}
	
	@RequestMapping("modifyByMno")
	public int modifyByMno(MenberInfo mf){
		if(ParamsUtil.checkNull(mf.getMno(),mf.getNickName(),mf.getRealName(),mf.getEmail(),mf.getTel())){
			return -1;
		}
		return menberBiz.modifyByMno(mf);
	}
	
	@RequestMapping("getInfoByMno")
	public MenberInfo getInfoByMno(Integer mno){
		if(mno==null || mno<=0){
			return null;
		}
		return menberBiz.getInfoByMno(mno);
	}
	
	@RequestMapping("loginOut")
	public void loginOut(HttpSession session){
		session.removeAttribute("currentLoginUser");
	}

	@RequestMapping("checkLogin")
	public MenberInfo checkLogin(HttpSession session){
		Object obj = session.getAttribute("currentLoginUser");
		if( obj != null ){
			return (MenberInfo) obj;
		}
		return null;	//这里再返回空的时候，前端数据格式要求为text，而不能是json，否则JSON.parse('')会出错！
	}

	@RequestMapping("/login")		//参数必要
	public MenberInfo login(@RequestParam Map<String,String> map,HttpSession session){
		if(map.isEmpty() || ParamsUtil.checkNull(map.get("nickname"),map.get("pwd"),map.get("yzm"))){
			return new MenberInfo(-1,"参数为空");		//参数为空
		}
		Object codeObj = session.getAttribute("loginCode");
		if(codeObj==null || !codeObj.toString().equals(map.get("yzm"))){
			return new MenberInfo(-2,"验证码错误");		//验证码错误
		}
		MenberInfo menber = menberBiz.login(map);
		if(menber != null){
			session.removeAttribute("loginCode");	//清除已使用的验证码
			session.setAttribute("currentLoginUser", menber);
			return menber;
		}
		return new MenberInfo(-3,"没有查到，账号或密码错误");	//没有查到，账号或密码错误
	}

	@RequestMapping("/reg")		//参数必要
	public int reg(@RequestParam Map<String,String> map,HttpSession session){
		if(map.isEmpty() || ParamsUtil.checkNull(map.get("nickname"),map.get("pwd"),map.get("email"),map.get("yzm"),map.get("tel"))){
			return -1;		//参数为空
		}
		Object codeObj = session.getAttribute("regCode");
		if(codeObj == null){
			return -2;		//验证码过期
		}
		if(!codeObj.toString().equals(map.get("yzm"))){
			return -3;		//验证码错误
		}
		session.removeAttribute("regCode");	//清除已使用的验证码

		map.put("regDate", ParamsUtil.getTime());

		return menberBiz.reg(map);
	}

	@RequestMapping("/delByMno")
	public int delByMno(Integer mno){
		if(ParamsUtil.checkNull(mno)){
			return -1;
		}
		return menberBiz.delByMno(mno);
	}

	@RequestMapping("/findAllByPage")
	public String findAllByPage(@RequestParam(required=true)Integer page,@RequestParam(required=true)Integer limit,HttpServletResponse resp) throws JsonProcessingException {
		List<MenberInfo> list = menberBiz.findAllByPage(page,limit);
		int count = menberBiz.countAll();
		String data = ParamsUtil.objectToJsonStr(list);
		String result = "{\"code\":0, \"msg\":\"\", \"count\":"+count+", \"data\":"+data+"}";	//转为layui需要的json格式
		return result;
	}

}
