package com.java1234.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.DrugsUnit;
import com.java1234.entity.Log;
import com.java1234.service.DrugsUnitService;
import com.java1234.service.LogService;

/*
 * 后台管理药品单位Controller
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/drugsUnit")
public class DrugsUnitAdminController {

	@Resource
	private DrugsUnitService drugsUnitService;
	
	@Resource
	private LogService logService;

	protected static final Logger logger = LoggerFactory.getLogger(DrugsUnitAdminController.class);
	
	@RequestMapping("/comboList")
	@RequiresPermissions(value = { "药品管理" })
	public List<DrugsUnit> comboList() {
		return drugsUnitService.listAll();
	}
	
	/*
	 * 查询所有药品单位
	 */
	@RequestMapping("/listAll")
	@RequiresPermissions(value = { "药品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> listAll() {
		List<DrugsUnit> drugsUnitList=drugsUnitService.listAll();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", drugsUnitList);
		logger.info("查询药品单位信息");
		logService.save(new Log(Log.SEARCH_ACTION,"查询药品单位信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 添加药品单位
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "药品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> save(DrugsUnit drugsUnit) {
		Map<String, Object> resultMap = new HashMap<>();
		logger.info("添加药品单位信息"+drugsUnit);
		logService.save(new Log(Log.ADD_ACTION,"添加药品单位信息"+drugsUnit));
		drugsUnitService.save(drugsUnit);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/*
	 * 删除药品单位信息
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "药品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> delete(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		logger.info("删除药品单位信息"+drugsUnitService.findById(id));
		logService.save(new Log(Log.DELETE_ACTION,"删除药品单位信息"+drugsUnitService.findById(id)));  // 写入日志
		drugsUnitService.delete(id);
		resultMap.put("success", true);
		return resultMap;
	}
	
}
