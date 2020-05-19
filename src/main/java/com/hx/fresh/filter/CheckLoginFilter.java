package com.hx.fresh.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查过滤器
 * @author Huathy
 * @date 2020年4月5日
 */
@WebFilter(filterName="CheckLoginFilter",value="/back/manager/*",initParams=@WebInitParam(name="errorPage",value="back/index.html"))
public class CheckLoginFilter implements Filter {
	private String path = "index.html";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		//chain.doFilter(req, resp);
		if(session.getAttribute("currentLoginAdmin")!=null){	//没有用户登录没有登录
			chain.doFilter(req, resp);		// 不为空，则说明已经登录，则交给下一个过滤器过滤
		}else{
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			String basePath = req.getScheme()+"://"+req.getServerName()+":"+ req.getServerPort() + req.getContextPath() +"/";
		//getScheme()：返回当前链接使用的协议；比如，一般应用返回http;SSL返回https;。getServerName()：返回服务名称。getServletContext()：返回请求的上下文。
			out.print("<script>alert('请先登录...');top.location.href='" + basePath + path + "';</script>");
			out.flush();
			out.close();
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String temp = config.getInitParameter("errorPage");
		if(temp != null){
			path = temp;
		}
	}

}
