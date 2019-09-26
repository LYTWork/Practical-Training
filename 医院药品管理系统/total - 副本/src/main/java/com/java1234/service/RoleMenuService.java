package com.java1234.service;

import com.java1234.entity.RoleMenu;

/*
 * 角色权限关联Service接口
 * @author java1234 AT
 *
 */
public interface RoleMenuService {

	/*
	 * 根据角色id删除所有关联信息
	 */
    void deleteByRoleId(Integer roleId);
	
	/*
	 * 保存
	 */
    void save(RoleMenu roleMenu);
}
