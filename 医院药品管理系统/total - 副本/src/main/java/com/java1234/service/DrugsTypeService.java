package com.java1234.service;

import java.util.List;

import com.java1234.entity.DrugsType;

/*
 * 药品类别Service接口
 * @author java1234 AT
 *
 */
public interface DrugsTypeService {

	/*
	 * 根据id查询药品类别实体
	 */
    DrugsType findById(Integer id);
	
	/*
	 * 根据父节点查找药品类别
	 */
    List<DrugsType> findByParentId(int parentId);
	
	/*
	 * 添加或者修改药品类别信息
	 */
    void save(DrugsType drugsType);
	
	/*
	 * 根据id删除药品类别信息
	 */
    void delete(Integer id);
}
