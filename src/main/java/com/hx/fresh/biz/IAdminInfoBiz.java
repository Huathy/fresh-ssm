package com.hx.fresh.biz;

import java.util.List;

import com.hx.fresh.entity.AdminInfo;

/**
 * 管理员业务接口
 * @author Huathy
 * @date 2020年4月21日
 */
public interface IAdminInfoBiz {
	
	/**
	 * 管理员登录
	 * @param admin
	 * @return
	 */
	public AdminInfo login(AdminInfo admin);
	
	/**
	 * 查询所有的管理员信息
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
