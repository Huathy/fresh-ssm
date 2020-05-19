package com.hx.fresh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hx.fresh.entity.AddrInfo;

/**
 * 地址信息数据映射接口
 * @author Huathy
 * @date 2020年4月21日
 */
public interface IAddrInfoMapper {
	
	/**
	 * 添加收货地址
	 * @param addr
	 * @return	返回受影响行数，并返回ano到实体类中
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
	public int changeDefault(@Param("mno")Integer mno, @Param("ano")Integer ano);
	
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
