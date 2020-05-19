package com.hx.fresh.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hx.fresh.biz.IGoodsTypeBiz;
import com.hx.fresh.entity.GoodsType;
import com.hx.fresh.mapper.IGoodsTypeMapper;

/**
 * 商品类别业务接口实现
 * @author Huathy
 * @date 2020年4月2日
 */
@Service
public class GoodsTypeBizImpl implements IGoodsTypeBiz{
	@Autowired
	private IGoodsTypeMapper mapper;
	
	@Override
	public List<GoodsType> findAllByPage(Integer page, Integer pageSize) {
		return mapper.findAllByPage((page-1)*pageSize,pageSize);
	}

	@Override
	public int countAll() {
		return mapper.countAll();
	}

	@Override
	public int delByTno(Integer tno) {
		return mapper.delByTno(tno);
	}

	@Override
	public int add(String savePath, String tname) {
		return mapper.add(savePath,tname);
	}

	@Override
	public List<GoodsType> getTidName() {
		return mapper.getTidName();
	}

	@Override
	public int modify(GoodsType goodsType) {
		return mapper.modify(goodsType);
	}

	@Override
	public List<GoodsType> getAll() {
		return mapper.getAll();
	}

}
