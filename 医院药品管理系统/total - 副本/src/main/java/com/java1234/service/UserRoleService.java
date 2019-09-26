package com.java1234.service;


import com.java1234.entity.UserRole;

/*
 * 用户角色关联Service接口
 * @author java1234 AT
 *
 */
public interface UserRoleService {

	/*
	 * 添加或者修改用户角色关联
	 */
    void save(UserRole userRole);
	
	/*
	 * 删除用户角色关联实体
	 */
    void delete(UserRole userRole);
	

	
	/*
	 * 根据ID查询用户角色关联实体
	 */
    UserRole findById(Integer id);
	
	/*
	 * 根据用户id删除所有关联信息
	 */
    void deleteByUserId(Integer userId);
	
	/*
	 * 根据角色id删除所有关联信息
	 */
    void deleteByRoleId(Integer userId);
}
