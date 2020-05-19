package com.hx.fresh.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.hx.fresh.biz.IOrderInfoBiz;
import com.hx.fresh.entity.OrderInfo;
import com.hx.utils.ParamsUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阿里支付控制层
 * @author Huathy
 * @date 2020年4月21日
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping("/alipay")
public class AliPayController {
	
	@Autowired
	private IOrderInfoBiz orderBiz;
	
	@RequestMapping("/pay")		//踩坑记录->这里不可直接return result; 必须要response.getWriter().print(result);或者以map键值对方法返回
	public String toPay(OrderInfo order,HttpServletRequest request,HttpServletResponse resp,HttpSession session) throws AlipayApiException, IOException {
		if(ParamsUtil.checkNull(order.getOno(),order.getPrice()) ){
			return "<script>alert('支付请求失败！')</script>";
		}
		
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = String.valueOf(order.getOno());
		session.setAttribute("ono", out_trade_no);
		//付款金额，必填
		String total_amount = String.valueOf(order.getPrice());
		//订单名称，必填
		String subject = "天天生鲜系列商品";
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		resp.getWriter().print(result);
		return null;
	}
	
	@RequestMapping("callbackNotify")
	public void callbackNotify(HttpServletRequest request,HttpServletResponse resp,HttpSession session) throws IOException, AlipayApiException{
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		PrintWriter out = resp.getWriter();
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			
			int result = orderBiz.modifyStatusByOno(Integer.parseInt(out_trade_no), 2);
			
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				orderBiz.modifyStatusByOno(Integer.parseInt(out_trade_no), 2);
				out.print("<script>location.href='order.html'</script>");
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			}else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				orderBiz.modifyStatusByOno(Integer.parseInt(out_trade_no), 2);
				out.print("<script>location.href='order.html'</script>");
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
			}
			
		}else {//验证失败
			Integer ono = (Integer) session.getAttribute("ono");
			orderBiz.modifyStatusByOno(ono, 1);
			//调试用，写文本函数记录程序运行情况是否正常
			//String sWord = AlipaySignature.getSignCheckContentV1(params);
			//AlipayConfig.logResult(sWord);
		}
	}
	
	@RequestMapping("callbackReturn")
	public void callbackReturn(HttpServletRequest request,HttpServletResponse resp) throws AlipayApiException, IOException{
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		PrintWriter out = resp.getWriter();
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			int result = orderBiz.modifyStatusByOno(Integer.parseInt(out_trade_no), 2);
			out.print("<script>location.href='../order.html'</script>");
		}else {
			out.println("验签失败");
		}
	}

}
