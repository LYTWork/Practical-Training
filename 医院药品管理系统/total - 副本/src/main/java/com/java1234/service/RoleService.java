package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.Role;

/*
 * 角色Service实现类
 * @author java1234 AT
 *
 */
public interface RoleService {

	/*
	 * 根据用户id查询角色集合
	 */
    List<Role> findByUserId(Integer id);
	
	/*
	 * 根据id查询实体
	 */
    Role findById(Integer id);
	
	/*
	 * 查询所有角色信息
	 */
    List<Role> listAll();
	
	/*
	 * 根据条件分页查询角色信息
	 */
    List<Role> list(Role role, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 获取总记录数
	 */
    Long getCount(Role role);
	
	/*
	 * 添加或者修改角色信息
	 */
    void save(Role role);
	
	/*
	 * 根据id删除角色
	 */
    void delete(Integer id);
	
	
}
