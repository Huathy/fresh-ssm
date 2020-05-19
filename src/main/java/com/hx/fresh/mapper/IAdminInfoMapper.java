package com.hx.fresh.mapper;

import java.util.List;

import com.hx.fresh.entity.AdminInfo;

/**
 * 管理员信息映射接口
 * @author Huathy
 * @date 2020年4月1日
 */
public interface IAdminInfoMapper {
	
	/**
	 * 根据用户的账号密码查询	用于登录
	 * @param admin
	 * @return AdminInfo
	 */
	public AdminInfo login(AdminInfo admin);
	
	/**
	 * 查询所有管理员信息	aid aname tel
	 * @return
	 */
	public List<AdminInfo> findAll();
	
	/**
	 * 管理员添加
	 * @param af
	 * @return
	 */
	public int add(AdminInfo af);
	
	/**
	 * 重置管理员密码
	 * @param aid
	 * @return
	 */
	public int updatePwd(Integer aid);
	
	/**
	 * 根据aid删除管理员
	 * @param aid
	 * @return
	 */
	public int delByAid(Integer aid);

}
