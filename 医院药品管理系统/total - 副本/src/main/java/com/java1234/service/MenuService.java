package com.java1234.service;

import java.util.List;

import com.java1234.entity.Menu;

/*
 * 权限菜单Service实现类
 * @author java1234 AT
 *
 */
public interface MenuService {

	/*
	 * 根据id查询实体
	 */
    Menu findById(Integer id);
	
	/*
	 * 根据id获取权限菜单集合
	 */
    List<Menu> findByRoleId(int roleId);
	
	/*
	 * 根据父节点以及角色id集合查询子节点
	 */
    List<Menu> findByParentIdAndRoleId(int parentId, int roleId);
	
	/*
	 * 根据父节点查找所有子节点
	 */
    List<Menu> findByParentId(int parentId);
}
