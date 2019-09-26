package com.java1234.controller.admin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.java1234.entity.Log;
import com.java1234.entity.Menu;
import com.java1234.entity.Role;
import com.java1234.entity.RoleMenu;
import com.java1234.service.LogService;
import com.java1234.service.MenuService;
import com.java1234.service.RoleMenuService;
import com.java1234.service.RoleService;
import com.java1234.service.UserRoleService;
import com.java1234.util.StringUtil;

/*
 * 后台管理角色Controller
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/role")
public class RoleAdminController {

	@Resource
	private RoleService roleService;
	
	@Resource
	private UserRoleService userRoleService;
	
	@Resource
	private MenuService menuService;
	
	@Resource
	private RoleMenuService roleMenuService;
	
	@Resource
	private LogService logService;

	protected static final Logger logger = LoggerFactory.getLogger(RoleAdminController.class);
	
	/*
	 * 查询所有角色
	 */
	@RequestMapping("/listAll")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> listAll() {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", roleService.listAll());
		logger.info("查询所有角色信息");
		logService.save(new Log(Log.SEARCH_ACTION,"查询所有角色信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 分页查询角色信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> list(Role role,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows) {
		List<Role> roleList=roleService.list(role, page, rows, Direction.ASC, "id");
		Long total=roleService.getCount(role);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", roleList);
		resultMap.put("total", total);
		logger.info("查询角色信息");
		logService.save(new Log(Log.SEARCH_ACTION,"查询角色信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 添加或者修改角色信息
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> save(Role role) {
		if(role.getId()!=null){ // 写入日志
			logger.info("更新角色信息"+role);
			logService.save(new Log(Log.UPDATE_ACTION,"更新角色信息"+role)); 
		}else{
			logger.info("添加角色信息"+role);
			logService.save(new Log(Log.ADD_ACTION,"添加角色信息"+role)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		roleService.save(role);
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/*
	 * 删除角色信息
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> delete(Integer id) {
		logger.info("删除角色信息"+roleService.findById(id));
		logService.save(new Log(Log.DELETE_ACTION,"删除角色信息"+roleService.findById(id)));  // 写入日志
		Map<String, Object> resultMap = new HashMap<>();
		userRoleService.deleteByRoleId(id); // 删除用户角色关联信息
		roleService.delete(id);				
		resultMap.put("success", true);
		return resultMap;
	}
	
	/*
	 * 根据父节点获取所有复选框权限菜单树
	 */
    @PostMapping("/loadCheckMenuInfo")
	@RequiresPermissions(value = { "角色管理" })
	public String loadCheckMenuInfo(Integer parentId,Integer roleId) {
		List<Menu> menuList=menuService.findByRoleId(roleId); // 根据角色查询所有权限菜单信息
		List<Integer> menuIdList=new LinkedList<Integer>();
		for(Menu menu:menuList){
			menuIdList.add(menu.getId());
		}
		return getAllCheckedMenuByParentId(parentId,menuIdList).toString();
	}
	
	/*
	 * 根据父节点ID和权限菜单ID集合获取复选框菜单节点
	 */
	private JsonArray getAllCheckedMenuByParentId(Integer parentId,List<Integer> menuIdList){
		JsonArray jsonArray=this.getCheckedMenuByParentId(parentId, menuIdList);
		for(int i=0;i<jsonArray.size();i++){
			JsonObject jsonObject=(JsonObject) jsonArray.get(i);
			if("open".equals(jsonObject.get("state").getAsString())){
    			continue;
    		}else{
    			jsonObject.add("children", getAllCheckedMenuByParentId(jsonObject.get("id").getAsInt(),menuIdList));
    		}
		}
		return jsonArray;
	}
	
	/*
	 * 根据父节点ID和权限菜单ID集合获取复选框菜单节点
	 */
	private JsonArray getCheckedMenuByParentId(Integer parentId,List<Integer> menuIdList){
		List<Menu> menuList=menuService.findByParentId(parentId);
		JsonArray jsonArray=new JsonArray();
		for(Menu menu:menuList){
    		JsonObject jsonObject=new JsonObject();
    		int menuId=menu.getId();
    		jsonObject.addProperty("id", menuId); // 节点id
    		jsonObject.addProperty("text", menu.getName()); // 节点名称
    		if(menu.getState()==1){
    			jsonObject.addProperty("state", "closed"); // 根节点
    		}else{
    			jsonObject.addProperty("state", "open"); // 叶子节点
    		}
    		if(menuIdList.contains(menuId)){
    			jsonObject.addProperty("checked", true);
    		}
    		jsonObject.addProperty("iconCls", menu.getIcon());
			jsonArray.add(jsonObject);
    	}
		return jsonArray;
	}
	
	/*
	 * 保存角色权限设置
	 */
	@RequestMapping("/saveMenuSet")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> saveMenuSet(String menuIds,Integer roleId) {
		Map<String, Object> resultMap = new HashMap<>();
		roleMenuService.deleteByRoleId(roleId); // 根据角色id删除所有角色权限关联实体
		if(StringUtil.isNotEmpty(menuIds)){
			String idsStr[]=menuIds.split(",");
			for(int i=0;i<idsStr.length;i++){ // 然后添加所有角色权限关联实体
				RoleMenu roleMenu=new RoleMenu();
				roleMenu.setRole(roleService.findById(roleId));
				roleMenu.setMenu(menuService.findById(Integer.parseInt(idsStr[i])));
				roleMenuService.save(roleMenu);
			}
		}
		resultMap.put("success", true);
		logger.info("保存角色权限设置");
		logService.save(new Log(Log.ADD_ACTION,"保存角色权限设置"));  // 写入日志
		return resultMap;
	}
}
