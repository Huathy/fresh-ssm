package com.hx.fresh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hx.fresh.biz.IGoodsInfoBiz;
import com.hx.fresh.entity.GoodsInfo;
import com.hx.utils.FileUploadUtil;
import com.hx.utils.ParamsUtil;

/**
 * 商品信息控制器
 * @author Huathy
 * @date 2020年4月4日
 */
@RestController
@RequestMapping("goods")
public class GoodsInfoControllor {
	
	@Autowired
	private IGoodsInfoBiz goodsBiz;
	
	@RequestMapping("delByGno")
	public int delByGno(Integer gno){
		return goodsBiz.delByGno(gno);
	}
	
	@RequestMapping("getNewGoods")
	public List<GoodsInfo> getNewGoods(){
		return goodsBiz.getNewGoods();
	}
	
	@RequestMapping("getGoodsByGid")
	public GoodsInfo getGoodsByGid(Integer gno){
		if( gno==null || gno<=0){
			return null;
		}
		return goodsBiz.getGoodsByGid(gno);
	}
	
	@RequestMapping("searchGoodsByPage")
	public Map<String,Object> searchGoodsByPage(String kw,Integer page,Integer pageSize){
		if(kw==null || "".equals(kw) || page==null || page<=0 || pageSize==null || pageSize<=0){
			return Collections.emptyMap();
		}
		List<String> kws = ParamsUtil.analyzerCnStr(kw);
		Map<String,Object> map = new HashMap<>();
		int count = goodsBiz.countSearch(kws);
		List<GoodsInfo> list = goodsBiz.searchGoodsByPage(kws,page,pageSize);
		map.put("count", count);
		map.put("goods", list);
		map.put("kws", kws);
		return map;
	}
	
	@RequestMapping("getGoodsByTidPage")
	public Map<String,Object> getGoodsByTidPage(Integer tno,Integer page,Integer pageSize) throws Exception{
		if(ParamsUtil.checkNull(tno,page,pageSize) || ParamsUtil.checkGreater("<=", 0, tno,page,pageSize)){
			return Collections.emptyMap();
		}
		Map<String,Object> map = new HashMap<>();
		int count = goodsBiz.count(tno);
		map.put("count", count);
		List<GoodsInfo> list = goodsBiz.getGoodsByTidPage(tno,page,pageSize);
		map.put("goods", list);
		return map;
	}
	
	/**
	 * 按照类型分组获取前4个商品的编号、名称、图片信息
	 * @return
	 */
	@RequestMapping("getTidGoods")
	public List<Map<String,Object>> getTidGoods(){
		List<Map<String,Object>> list = goodsBiz.getTidGoods();
		return list;
	}
	
	@RequestMapping("findAllByPage")
	public String findAllByPage(Integer page,@RequestParam("limit")Integer pageSize) throws JsonProcessingException{
		int count = goodsBiz.count(null);
		List<GoodsInfo> list = goodsBiz.findAllByPage(page,pageSize);
		String data = ParamsUtil.objectToJsonStr(list);
		return "{\"code\":0 , \"count\":"+count+" , \"data\":"+data+"}";
	}
	
	@RequestMapping("modifyGoods")
	public String modifyGoods(@RequestParam("pic")MultipartFile[] phos,GoodsInfo gf,HttpServletRequest req){
		if(ParamsUtil.checkNull(gf.getTno(),gf.getGname(),gf.getPrice(),gf.getBalance(),gf.getUnit(),gf.getQperied(),gf.getIntro(),gf.getDescr())){
			return "{ \"code\":-1 , \"msg\":\"商品信息不可为空！\" }";		//商品信息为空！
		}
		
		List<String> savePaths = new ArrayList<String>();
		if(phos.length > 0){
			try {
				for(int i=0;i<phos.length;i++){
					if(phos[i].isEmpty()){
						continue;
					}
					String picPath = FileUploadUtil.uploadPicWatermark(phos[i], req);
					savePaths.add(picPath);
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return "{ \"code\":-2 , \"msg\":\"" + e + "\" }";
			} 
		}
		gf.setPics( gf.getPics() + "|" + String.join("|", savePaths) );		//将原有的图片与现有图片拼接
		if(gf.getPics().endsWith("|")){
			gf.setPics(gf.getPics().substring(0, gf.getPics().length()-1));;
		}else if(gf.getPics().startsWith("|")){
			gf.setPics(gf.getPics().substring(1));
		}
		int result = goodsBiz.modify(gf);
		return "{ \"code\":" + result + " , \"msg\":\" "+gf.getPics()+" \" }";
	}
	
	@RequestMapping("/addGoods")
	public int addGoods(@RequestParam("pic")MultipartFile[] pics,GoodsInfo gf,HttpServletRequest req){
		if( pics.length <= 0 ){
			return -1;		//图片为空
		}
		if(ParamsUtil.checkNull(gf.getGno(),gf.getTno(),gf.getGname(),gf.getPrice(),gf.getBalance(),gf.getUnit(),gf.getQperied(),gf.getIntro(),gf.getDescr())){
			return -2;		//商品信息为空！
		}
		
		
		List<String> savePaths = new ArrayList<String>();
		if(pics.length > 0){
			try {
				for(int i=0;i<pics.length;i++){
					if(pics[i].isEmpty()){
						continue;
					}
					String picPath = FileUploadUtil.uploadPicWatermark(pics[i], req);
					savePaths.add(picPath);
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return -1;
			} 
		}
		gf.setPics( String.join("|", savePaths) );		//将原有的图片与现有图片拼接
		if(gf.getPics().endsWith("|")){
			gf.setPics(gf.getPics().substring(0, gf.getPics().length()-1));;
		}else if(gf.getPics().startsWith("|")){
			gf.setPics(gf.getPics().substring(1));
		}
		
		return goodsBiz.add(gf);
	}
	
	@RequestMapping("upload")
	public Map<String, Object> upload(@RequestParam("upload")MultipartFile pic,HttpServletRequest req){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String savePath = FileUploadUtil.uploadPicWatermark(pic, req);
			map.put("uploaded", 1);
			map.put("fileName", pic.getOriginalFilename());
			map.put("url", "../../"+savePath);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			map.put("uploaded", 0);
			map.put("error", "{ \"message\":"+e+" }");
		}
		return map;
	}
}
