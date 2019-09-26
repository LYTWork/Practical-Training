package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.Supplier;

/*
 * 供应商Service接口
 * @author java1234 AT
 *
 */
public interface SupplierService {

	/*
	 * 根据名称模糊查询供应商信息
	 */
    List<Supplier> findByName(String name);
	
	/*
	 * 根据id查询实体
	 */
    Supplier findById(Integer id);
	
	/*
	 * 修改或者修改供应商信息
	 */
    void save(Supplier supplier);
	
	/*
	 * 根据条件分页查询供应商信息
	 */
    List<Supplier> list(Supplier supplier, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 获取总记录数
	 */
    Long getCount(Supplier supplier);
	
	/*
	 * 根据id删除供应商
	 */
    void delete(Integer id);
}
