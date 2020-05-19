package com.hx.fresh.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hx.fresh.biz.IAdminInfoBiz;
import com.hx.fresh.entity.AdminInfo;
import com.hx.fresh.mapper.IAdminInfoMapper;

/**
 * 管理员信息业务接口实现
 * @author Huathy
 * @date 2020年4月5日
 */
@Service
public class AdminInfoBizImpl implements IAdminInfoBiz{
	@Autowired
	private IAdminInfoMapper mapper;
	
	@Override
	public AdminInfo login(AdminInfo admin) {
		return mapper.login(admin);
	}

	@Override
	public List<AdminInfo> findAll() {
		return mapper.findAll();
	}

	@Override
	public int add(AdminInfo af) {
		return mapper.add(af);
	}

	@Override
	public int updatePwd(Integer aid) {
		return mapper.updatePwd(aid);
	}

	@Override
	public int delByAid(Integer aid) {
		if(aid==null || aid<=0){
			return -1;
		}
		return mapper.delByAid(aid);
	}
	
}
