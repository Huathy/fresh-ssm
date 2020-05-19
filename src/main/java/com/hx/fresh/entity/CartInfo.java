package com.hx.fresh.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * 购物车实体
 * @author Huathy
 * @date 2020年3月31日
 */
@JsonInclude(JsonInclude.Include.NON_NULL)	//要求返回给前端的数据，去掉null值，过滤密码及状态等不可见或不必要的数据
public class CartInfo implements Serializable{
	private static final long serialVersionUID = 1572586262558788285L;
	
	private Integer cno;	//购物车编号
	private Integer mno;	//用户编号
	private Integer gno;	//商品编号
	private Integer num;	//数量
	
	@Override
	public String toString() {
		return "CartInfo [cno=" + cno + ", mno=" + mno + ", gno=" + gno + ", num=" + num + "]";
	}
	
	public Integer getCno() {
		return cno;
	}
	public void setCno(Integer cno) {
		this.cno = cno;
	}
	public Integer getMno() {
		return mno;
	}
	public void setMno(Integer mno) {
		this.mno = mno;
	}
	public Integer getGno() {
		return gno;
	}
	public void setGno(Integer gno) {
		this.gno = gno;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	public CartInfo(Integer cno, Integer mno, Integer gno, Integer num) {
		super();
		this.cno = cno;
		this.mno = mno;
		this.gno = gno;
		this.num = num;
	}
	public CartInfo() {
		super();
	}

	public CartInfo(Integer mno, Integer gno, Integer num) {
		super();
		this.mno = mno;
		this.gno = gno;
		this.num = num;
	}
	
}
