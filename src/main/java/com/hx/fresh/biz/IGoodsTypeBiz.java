package com.hx.fresh.biz;

import java.util.List;

import com.hx.fresh.entity.GoodsType;

/**
 * 商品类别业务接口
 * @author Huathy
 * @date 2020年4月2日
 */
public interface IGoodsTypeBiz {
	
	/**
	 * 类别分页查询
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<GoodsType> findAllByPage(Integer page, Integer pageSize);

	/**
	 * 获取总记录数
	 * @return
	 */
	public int countAll();
	
	/**
	 * 根据类型编号删除
	 * @param tno
	 * @return
	 */
	public int delByTno(Integer tno);
	
	/**
	 * 添加商品类型
	 * @param savePath
	 * @param tname
	 * @return
	 */
	public int add(String savePath, String tname);
	
	/**
	 * 查询类型id，名称
	 * @return
	 */
	public List<GoodsType> getTidName();
	
	/**
	 * 根据tno修改类型信息
	 * @param goodsType
	 * @return
	 */
	public int modify(GoodsType goodsType);
	
	/**
	 * 查询所有状态值为1的类型名称，编号，图片
	 * @return
	 */
	public List<GoodsType> getAll();

}
