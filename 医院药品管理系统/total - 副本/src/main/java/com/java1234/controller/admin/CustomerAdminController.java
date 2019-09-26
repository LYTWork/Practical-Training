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

import com.java1234.entity.Customer;
import com.java1234.entity.Log;
import com.java1234.service.CustomerService;
import com.java1234.service.LogService;

/*
 * 后台管理客户Controller
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/customer")
public class CustomerAdminController {
	
	@Resource
	private CustomerService customerService;
	
	@Resource
	private LogService logService;

	protected static final Logger logger = LoggerFactory.getLogger(CustomerAdminController.class);
	
	/*
	 * 分页查询客户信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "客户管理" })
	public Map<String,Object> list(Customer customer,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows) {
		List<Customer> customerList=customerService.list(customer, page, rows, Direction.ASC, "id");
		Long total=customerService.getCount(customer);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		logger.info("查询客户信息");
		logService.save(new Log(Log.SEARCH_ACTION,"查询客户信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 下拉框模糊查询
	 */
	@ResponseBody
	@RequestMapping("/comboList")
	@RequiresPermissions(value = {"销售出库","客户退货","销售单据查询","客户退货查询"},logical=Logical.OR)
	public List<Customer> comboList(String q) {
		if(q==null){
			q="";
		}
		return customerService.findByName("%"+q+"%");
	}
	
	
	/*
	 * 添加或者修改客户信息
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "客户管理" })
	public Map<String,Object> save(Customer customer) {
		if(customer.getId()!=null){ // 写入日志
			logger.info("更新客户信息"+customer);
			logService.save(new Log(Log.UPDATE_ACTION,"更新客户信息"+customer)); 
		}else{
			logger.info("添加客户信息"+customer);
			logService.save(new Log(Log.ADD_ACTION,"添加客户信息"+customer)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		customerService.save(customer);			//********
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/*
	 * 删除客户信息
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "客户管理" })
	public Map<String,Object> delete(String ids) {
		Map<String, Object> resultMap = new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			logger.info("删除客户信息"+customerService.findById(id));
			logService.save(new Log(Log.DELETE_ACTION,"删除客户信息"+customerService.findById(id)));  // 写入日志
			customerService.delete(id);							
		}
		resultMap.put("success", true);
		return resultMap;
	}

}
