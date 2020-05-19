package com.hx.fresh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hx.fresh.entity.GoodsType;

/**
 * 商品类别映射接口
 * @author Huathy
 * @date 2020年4月2日
 */
public interface IGoodsTypeMapper {
	
	/**
	 * 商品类别分页查询
	 * @param page		页号
	 * @param pageSize	条数
	 * @return
	 */
	public List<GoodsType> findAllByPage(@Param("page")Integer page, @Param("pageSize")Integer pageSize);
	
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
	 * @param savePath	存储路径
	 * @param tname		类型名称
	 * @return
	 */
	public int add(@Param("path")String savePath, @Param("tname")String tname);
	
	/**
	 * 查询类型名称，id
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
