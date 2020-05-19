package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
public class AlipayConfig {
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016102400752498";

	// 商户私钥，您的PKCS8格式RSA2私钥
	public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYPw/d1klRQbcR7Q206oP/CB1WcJJ44BroIQfbAY8sjMk6P9gCCCGcHdnJjtPFQ4R0RDLdK6qmzQCW7zwQ+On8Jii3GdQfVjItHO+Oa9eoiKqgDvVozkvQb71oMpw1WsmDgk/gw4U2573RrpI0GvkeSafAxAP56AccJhhMfRrrScODkTCpPuf+iQXye5YevYy1o/4wHV5AUdDpNJfmc0rmd0ygT25eMrfX9x6U3TELHP6gQSQVX6KF+aCV5YgrXnNuS4E+D5bCnHPtNv1rLjyzkTgRx+zK6kmi4Nm5KapRa9ZeFAAKi0BMddTZTfRRxPQ3DOE0ambO9g3sLPqZU4mFAgMBAAECggEAdEKD3UWMC28BW8EIXIVRculBbgbc800/fs/qbcWNDdocuEyUqUiIUe1BqHdXJCZg+WAy704vLN0TfC8IUXWjSJfjM5+Xqq5p9qVIEhwsPVjP4xZJNVWD85HAs9ciDmRbyf8y/FKSYdEp1PtYTk3iusxIPB3zY11aQJaeq5pFejJM6v8Bqw/JY3FladjOGd7ZjbCUbjd0iCpME10/wpEMATbbkNghQPyVRczglsJC7ebfeRQMgY/4G2uI8n4iGV15FgkLErxVn0swQokKxfnES32tKtQ4UNzTwlaq3oUH5IGZx0FrGHOARTepJig+/K1tPKfGXGeBZIrNrUSy+FAeaQKBgQD964TB+fX6YP+7EOJBhrwpo+6qHWmo0jj9xThdeM+UifUOMJ/LhBBCFxvuHSEkoHVOr2IkyEIWC6OZOxTx+ZGGVhMoxoviMtHpHJiaJ2e/UHPvDAYYr3RIxocjJ8ncMgsgDR83CigwRFS+P/a0ErvsTSNrinVw7VTaqrPVHuA1bwKBgQCZflRJ4or8GxegVLudCHnDVhQieJlm7CduzPpCGidd6qXsl9UcO3ghLNQ9ouc+XyrGZbTzoU+MHNnL/ItsxgDDIZnTwb2MF2S6zto1pC+GnnOzHHEFgVduYioSx8KzwB+VMgiCCFfjGwaHHa0z14+dXziOrUB2l5cFCJl2rv0+SwKBgQDNkhl++aFdPZ6MklS6/BvmEmQssXdTSC7/tOW3NxHHjhEpldAo8hg/iXoBQvAMOSP/uhnY8ZAfTPj8G0sPxB8/BPyp7GRfkwlCkvYxB2oFB1t00uU0owefy6qomZPhg1pibn0xEDYNLaT2Cc7vZqNh4emlLQl7jYk/JyhEiKS4XQKBgB77LLAtFbiFD2PBNo0fpNQUj8PVa8EmcV8f6HNnLAL81bjHfLBSIJExpzU5qEsiklOxO7DhIlk5iYxu0QWHgafhJFdlh4i7QA2kqs6g8SbO3LOOThG6ZZdWIP/hah817CoFC0qTvImObubeypLoM4ikK5pAxf4p4sWVUsRt1Ze5AoGAetwzMVUX+SC3p7lGjN8Il5RA538snz/dL/+L626/Djh+8AV2o4JZZxMcfLVg5RtubvtQzkcfMHn+iEOjyEgjJCxusLCfwRtL4+cjSe8is0oVA8E2BFRDp50AVNf69GeYtYzgl801I9Vt8zNQX8J0sBU5bouKPourC2914SlkK60=";

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
	// 对应APPID下的支付宝公钥。
	public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkJfy4HLeMfLrrd46srx4UY0IixdSa2UKV1Bibeb5HbkPzs7Aaa1j10y95bi0b2osBH5ASkwrRQCf2XA/3gXwjIf72UgflFnvCm+d3HRJvXZn/7VM7Ur8MxYwA2R0lIq97jmpQN5Jk/1OmdL31qXHFNQ0cXu6PXqElOlH4MAtJeqeduKip6NOLiTvLQ6OZEUcpuzRcBSqiH1N+d8RGco512DvV9nwcobjq1R58Amin9/xoygl3F7vCyv62G94UnISzHiWN8pDAnBk2QcDYhw3Er7StY28w/2B3BosoF8NvXGe5k4xcBi8ZtRn2fB/ibezw9XFDPj/QotYfTawlPaOGQIDAQAB";

	// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://fresh.free.idcfengye.com/fresh/alipay/callbackNotify";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://fresh.free.idcfengye.com/fresh/alipay/callbackReturn";

	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "utf-8";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	// 支付宝网关
	public static String log_path = "C:\\";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
	/**
	 * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	 * 
	 * @param sWord
	 *            要写入日志里的文本内容
	 */
	public static void logResult(String sWord) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
			writer.write(sWord);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
