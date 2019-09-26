package com.java1234.controller.admin;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java1234.entity.Log;
import com.java1234.entity.CustomerReturnList;
import com.java1234.entity.CustomerReturnListDrugs;
import com.java1234.service.LogService;
import com.java1234.service.UserService;
import com.java1234.service.CustomerReturnListDrugsService;
import com.java1234.service.CustomerReturnListService;
import com.java1234.util.DateUtil;
import com.java1234.util.StringUtil;

/*
 * 客户退货单Controller类
 * @author java1234 AT
 * @author java1234 lyw
 *
 */
@RestController
@RequestMapping("/admin/customerReturnList")
public class CustomerReturnListAdminController {

	@Resource
	private CustomerReturnListService customerReturnListService;
	
	@Resource
	private CustomerReturnListDrugsService customerReturnListDrugsService;
	
	@Resource
	private LogService logService;
	
	@Resource
	private UserService userService;

	protected static final Logger logger = LoggerFactory.getLogger(CustomerReturnListAdminController.class);
	
	@InitBinder//对表单数据绑定(bean中定义了Date，double等类型，如果没有做任何处理的话，日期以及double都无法绑定。)
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
	/*
	 * 根据条件分页查询客户退货单信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "客户退货查询" })
	public Map<String,Object> list(CustomerReturnList customerReturnList) {
		Map<String, Object> resultMap = new HashMap<>();
		List<CustomerReturnList> customerReturnListList=customerReturnListService.list(customerReturnList, Direction.DESC, "customerReturnDate");
		resultMap.put("rows", customerReturnListList);
		return resultMap;
	}
	
	/*
	 * 根据客户退货单id查询所有客户退货单药品
	 */
	@RequestMapping("/listDrugs")
	@RequiresPermissions(value = { "客户退货查询" })
	public Map<String,Object> listDrugs(Integer customerReturnListId) {
		if(customerReturnListId==null){
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<CustomerReturnListDrugs> customerReturnListDrugsList=customerReturnListDrugsService.listByCustomerReturnListId(customerReturnListId);
		resultMap.put("rows", customerReturnListDrugsList);
		logger.info("根据客户退货单id查询所有客户退货单药品");
		return resultMap;
	}

	/*
	 * 客户统计 获取客户退货单的所有药品信息
	 */
	@RequestMapping("/listCount")
	@RequiresPermissions(value = { "客户退货查询","客户退货" })
	public Map<String,Object> listCount(CustomerReturnList customerReturnList,CustomerReturnListDrugs customerReturnListDrugs) {
		Map<String, Object> resultMap = new HashMap<>();
		List<CustomerReturnList> customerReturnListList=customerReturnListService.list(customerReturnList, Direction.DESC, "customerReturnDate");
		for(CustomerReturnList crl:customerReturnListList){
			customerReturnListDrugs.setCustomerReturnList(crl);
			
			List<CustomerReturnListDrugs> crlList=customerReturnListDrugsService.list(customerReturnListDrugs);
			for(CustomerReturnListDrugs crlg:crlList){
				crlg.setCustomerReturnList(null);
			}
			crl.setCustomerReturnListDrugsList(crlList);
		}
		resultMap.put("rows", customerReturnListList);
		logger.info("获取客户退货单的所有药品信息");
		return resultMap;
	}

	/*
	 * 获取客户退货单号
	 */
	@ResponseBody
	@RequestMapping("/getCustomerReturnNumber")
	@RequiresPermissions(value = {"客户退货"})
	public String genBillCode(String type)throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("XT");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String customerReturnNumber=customerReturnListService.getTodayMaxCustomerReturnNumber(); // 获取当天最大的客户退货单号
		if(customerReturnNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(customerReturnNumber));
		}else{
			logger.error("获取客户退货单号失败");
			biilCodeStr.append("0001");
		}
		logger.info("获取客户退货单号成功");
		return biilCodeStr.toString();
	}
	
	/*
	 * 添加客户退货单 以及所有客户退货单药品
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions(value = {"客户退货"})
	public Map<String,Object> save(CustomerReturnList customerReturnList,String drugsJson) {
		Map<String, Object> resultMap = new HashMap<>();
		customerReturnList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
		Gson gson = new Gson();
		List<CustomerReturnListDrugs> plgList=gson.fromJson(drugsJson, new TypeToken<List<CustomerReturnListDrugs>>(){}.getType());
		customerReturnListService.save(customerReturnList, plgList);
		logger.info("添加客户退货单");
		logService.save(new Log(Log.ADD_ACTION,"添加客户退货单"));
		resultMap.put("success", true);
		return resultMap;
	}
	
	/*
	 * 修改退货单的支付状态
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions(value = {"客户退货查询","客户退货"})
	public Map<String,Object> update(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		CustomerReturnList customerReturnList=customerReturnListService.findById(id);
		customerReturnList.setState(1); // 修改成支付状态
		customerReturnListService.update(customerReturnList);
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 根据id删除客户退货单信息 包括客户退货单里的药品
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "客户退货查询" })
	public Map<String,Object> delete(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		customerReturnListService.delete(id);
		logger.info("删除客户退货单信息"+customerReturnListService.findById(id));
		logService.save(new Log(Log.DELETE_ACTION,"删除客户退货单信息"+customerReturnListService.findById(id)));  // 写入日志
		resultMap.put("success", true);
		return resultMap;
	}
}
