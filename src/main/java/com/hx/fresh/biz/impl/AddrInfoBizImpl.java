package com.hx.fresh.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hx.fresh.biz.IAddrInfoBiz;
import com.hx.fresh.entity.AddrInfo;
import com.hx.fresh.mapper.IAddrInfoMapper;

/**
 * 地址信息业务实现
 * @author Huathy
 * @date 2020年4月21日
 */
@Service
public class AddrInfoBizImpl implements IAddrInfoBiz {
	
	@Autowired
	private IAddrInfoMapper mapper;
	
	@Override
	public int add(AddrInfo addr) {
		int result = mapper.add(addr);
		if(result <= 0){
			return -1;
		}
		int ano = addr.getAno();
		return ano;
	}

	@Override
	public List<AddrInfo> getAddrByMno(Integer mno) {
		return mapper.getAddrByMno(mno);
	}
	
	@Override
	public int changeDefault(Integer mno, Integer ano) {
		return mapper.changeDefault(mno,ano);
	}

	@Override
	public int modifyAddrByAno(AddrInfo addr) {
		return mapper.modifyAddrByAno(addr);
	}

	@Override
	public int delByAno(Integer ano) {
		return mapper.delByAno(ano);
	}

}
