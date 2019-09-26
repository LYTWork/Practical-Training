package com.java1234.service;

import java.util.List;

import com.java1234.entity.DrugsUnit;

/*
 * 药品单位Service接口
 * @author java1234 AT
 *
 */
public interface DrugsUnitService {

	/*
	 * 根据id查询实体
	 */
    DrugsUnit findById(Integer id);
	
	/*
	 * 查询所有药品单位信息
	 */
    List<DrugsUnit> listAll();
	
	/*
	 * 修改或者修改药品单位信息
	 */
    void save(DrugsUnit drugsUnit);
	
	/*
	 * 根据id删除药品单位
	 */
    void delete(Integer id);
}
