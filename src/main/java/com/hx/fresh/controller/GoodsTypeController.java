package com.hx.fresh.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hx.fresh.biz.IGoodsTypeBiz;
import com.hx.fresh.entity.GoodsType;
import com.hx.utils.FileUploadUtil;
import com.hx.utils.ParamsUtil;

/**
 * 商品类型控制器
 * @author Huathy
 * @date 2020年4月5日
 */
@RestController
@RequestMapping("type")
public class GoodsTypeController {
	@Autowired
	private IGoodsTypeBiz typeBiz;
	
	@RequestMapping("getAll")
	public List<GoodsType> getAll(){
		return typeBiz.getAll();
	}
	
	@RequestMapping("modifyByTno")
	public String modify(MultipartFile pic,Integer tno,String tname,Integer status,HttpServletRequest req){
		int result = -1;
		if(ParamsUtil.checkNull(tno,tname,status)){
			return "{\"code\":"+result+",\"msg\":\"参数为空！\"}";	//若修改失败，则返回错误信息
		}
		String savePath = null;
		if(!pic.isEmpty()){
			try {
				savePath = FileUploadUtil.uploadPic(pic, req);
				result = typeBiz.modify(new GoodsType(tno,tname,savePath,status));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}else{
			result = typeBiz.modify(new GoodsType(tno,tname,null,status));
		}
		return "{\"code\":"+result+",\"pic\":\""+savePath+"\"}";	//若修改成功，连pic地址一起返回前台已做修改
	}
	
	@RequestMapping("getTidName")
	public List<GoodsType> getTidName(){
		return typeBiz.getTidName();
	}
	
	@RequestMapping("add")
	public int add(@RequestParam("pic")MultipartFile pic,String tname,HttpServletRequest req){
		if(pic.isEmpty()){
			return -1;	
		}
		if(ParamsUtil.checkNull(tname)){
			return -1;	//参数为空
		}
		
		try {
			String savePath = FileUploadUtil.uploadPic(pic,req);
			return typeBiz.add(savePath,tname);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return -1;
	}
	
	@RequestMapping("delByTno")
	public int delByTno(Integer tno){
		if(ParamsUtil.checkNull(tno)){
			return -1;
		}
		return typeBiz.delByTno(tno);
	}
	
	@RequestMapping("findAllByPage")
	public String findAllByPage(Integer page,@RequestParam("limit")Integer pageSize,HttpServletResponse resp) throws JsonProcessingException{
		int count = typeBiz.countAll();
		List<GoodsType> list = typeBiz.findAllByPage(page,pageSize);
		
		String data = ParamsUtil.objectToJsonStr(list);		// TODO 将数据转换为？格式	
		return  "{\"code\":0, \"msg\":\"\", \"count\":"+count+", \"data\":"+data+"}";	//转为layui需要的json格式
	}
	
}

