package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.User;

/*
 * 用户Service接口
 * @author java1234 AT
 *
 */
public interface UserService {

	/*
	 * 根据用户名查找用户实体
	 */
    User findByUserName(String userName);
	
	/*
	 * 根据id查询用户实体
	 */
    User findById(Integer id);
	
	/*
	 * 修改或者修改用户信息
	 */
    void save(User user);
	
	/*
	 * 根据条件分页查询用户信息
	 */
    List<User> list(User user, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 获取总记录数
	 */
    Long getCount(User user);
	
	/*
	 * 根据id删除用户
	 */
    void delete(Integer id);
}
