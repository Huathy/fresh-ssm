package com.hx.fresh.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.hx.utils.ParamsUtil;

/**
 * 创建该项目上传的图片的
 * @author Huathy
 * @date 2020年3月31日
 */
@WebListener
public class InitApplicationListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String path = "../freshPics";
		
		String temp = sce.getServletContext().getInitParameter("uploadPath");
		
		if(!ParamsUtil.checkNull(temp)){
			path = temp;
		}
		
		String basePath = sce.getServletContext().getRealPath("/");
		File fl = new File(basePath + path);
		System.out.println(basePath + path);
		if(!fl.exists()){
			fl.mkdirs();
		}
	}
	
}
