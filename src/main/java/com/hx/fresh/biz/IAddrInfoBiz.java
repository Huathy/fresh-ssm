package com.hx.fresh.biz;

import java.util.List;

import com.hx.fresh.entity.AddrInfo;

/**
 * 地址信息业务接口
 * @author Huathy
 * @date 2020年4月21日
 */
public interface IAddrInfoBiz {
	
	/**
	 * 添加地址信息
	 * @param addr
	 * @return	返回当前插入的地址编号(ano),若插入失败，则返回-1
	 */
	public int add(AddrInfo addr);
	
	/**
	 * 根据mno查询所有地址
	 * @param mno
	 * @return List<AddrInfo>
	 */
	public List<AddrInfo> getAddrByMno(Integer mno);
	
	/**
	 * 根据mno将其他地址设为非默认,将ano地址设为默认
	 * @param mno
	 * @param ano
	 * @return
	 */
	public int changeDefault(Integer mno, Integer ano);
	
	/**
	 * 修改收货地址信息
	 * @param addr
	 * @return
	 */
	public int modifyAddrByAno(AddrInfo addr);
	
	/**
	 * 根据ano删除地址
	 * @param ano
	 * @return
	 */
	public int delByAno(Integer ano);

}
