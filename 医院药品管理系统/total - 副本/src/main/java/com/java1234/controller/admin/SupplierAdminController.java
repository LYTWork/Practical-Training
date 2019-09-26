package com.java1234.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.Log;
import com.java1234.entity.Supplier;
import com.java1234.service.LogService;
import com.java1234.service.SupplierService;

/*
 * 后台管理供应商Controller
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/supplier")
public class SupplierAdminController {
	
	@Resource
	private SupplierService supplierService;
	
	@Resource
	private LogService logService;

	protected static final Logger logger = LoggerFactory.getLogger(SupplierAdminController.class);
	
	/*
	 * 分页查询供应商信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "供应商管理" })
	public Map<String,Object> list(Supplier supplier,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows) {
		List<Supplier> supplierList=supplierService.list(supplier, page, rows, Direction.ASC, "id");
		Long total=supplierService.getCount(supplier);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", supplierList);
		resultMap.put("total", total);
		logger.info("查询供应商信息");
		logService.save(new Log(Log.SEARCH_ACTION,"查询供应商信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 下拉框模糊查询
	 */
	@ResponseBody
	@RequestMapping("/comboList")
	@RequiresPermissions(value = {"进货入库","退货出库","进货单据查询","退货单据查询"},logical=Logical.OR)
	public List<Supplier> comboList(String q) {
		if(q==null){
			q="";
		}
		return supplierService.findByName("%"+q+"%");
	}
	
	
	
	/*
	 * 添加或者修改供应商信息
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "供应商管理" })
	public Map<String,Object> save(Supplier supplier) {
		if(supplier.getId()!=null){ // 写入日志
			logger.info("更新供应商信息"+supplier);
			logService.save(new Log(Log.UPDATE_ACTION,"更新供应商信息"+supplier));
		}else{
			logger.info("添加供应商信息"+supplier);
			logService.save(new Log(Log.ADD_ACTION,"添加供应商信息"+supplier)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		supplierService.save(supplier);			
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/*
	 * 删除供应商信息
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "供应商管理" })
	public Map<String,Object> delete(String ids) {
		Map<String, Object> resultMap = new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			logger.info("删除供应商信息"+supplierService.findById(id));
			logService.save(new Log(Log.DELETE_ACTION,"删除供应商信息"+supplierService.findById(id)));  // 写入日志
			supplierService.delete(id);							
		}
		resultMap.put("success", true);
		return resultMap;
	}

}
