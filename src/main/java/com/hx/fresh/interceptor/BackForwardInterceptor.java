package com.hx.fresh.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 后台页面的拦截器
 * @author Huathy
 * @date 2020年3月31日
 */
public class BackForwardInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		if( !path.endsWith("/") && path.indexOf(".")==-1 ){
			//错误请求path路径：/back/manager
			response.sendRedirect("/fresh"+path+"/");
			//因为WEB-INF 下的只能内部转发进去，但是转发，只对错误请求的加/，无法处理后面的其他请求，这里使用重定向处理
			//http://localhost:8080/fresh/back/manager	因为自动加上少的/是针对项目级别的，所以这里，对于类似这样的访问做处理。
			return false;
		}
		request.getRequestDispatcher("/WEB-INF/" + path).forward(request, response);
		return false;
	}
	
}
