package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.Customer;

/*
 * 客户Service接口
 * @author java1234 AT
 *
 */
public interface CustomerService {

	/*
	 * 根据名称模糊查询客户信息
	 */
    List<Customer> findByName(String name);
	
	/*
	 * 根据id查询实体
	 */
    Customer findById(Integer id);
	
	/*
	 * 添加或者修改客户信息
	 */
    void save(Customer customer);
	
	/*
	 * 根据条件分页查询客户信息
	 */
    List<Customer> list(Customer customer, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 获取总记录数
	 */
    Long getCount(Customer customer);
	
	/*
	 * 根据id删除客户
	 */
    void delete(Integer id);
}
