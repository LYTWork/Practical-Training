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
import com.java1234.entity.ReturnList;
import com.java1234.entity.ReturnListDrugs;
import com.java1234.service.LogService;
import com.java1234.service.ReturnListDrugsService;
import com.java1234.service.ReturnListService;
import com.java1234.service.UserService;
import com.java1234.util.DateUtil;
import com.java1234.util.StringUtil;

/*
 * 退货单Controller类
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/returnList")
public class ReturnListAdminController {

	@Resource
	private ReturnListService returnListService;
	
	@Resource
	private ReturnListDrugsService returnListDrugsService;
	
	@Resource
	private LogService logService;
	
	@Resource
	private UserService userService;

	protected static final Logger logger = LoggerFactory.getLogger(ReturnListAdminController.class);
	
	@InitBinder//对表单数据绑定(bean中定义了Date，double等类型，如果没有做任何处理的话，日期以及double都无法绑定。)
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
	/*
	 * 根据条件分页查询退货单信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "退货单据查询" })
	public Map<String,Object> list(ReturnList returnList) {
		Map<String, Object> resultMap = new HashMap<>();
		List<ReturnList> returnListList=returnListService.list(returnList, Direction.DESC, "returnDate");
		resultMap.put("rows", returnListList);
		return resultMap;
	}
	
	/*
	 * 根据退货单id查询所有退货单药品
	 */
	@RequestMapping("/listDrugs")
	@RequiresPermissions(value = { "退货单据查询" })
	public Map<String,Object> listDrugs(Integer returnListId) {
		if(returnListId==null){
			logger.warn("不存在该id的退货单药品");
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<ReturnListDrugs> returnListDrugsList=returnListDrugsService.listByReturnListId(returnListId);
		resultMap.put("rows", returnListDrugsList);
		logger.info("根据退货单id查询所有退货单药品成");
		return resultMap;
	}

	/*
	 * 客户统计 获取退货单的所有药品信息
	 */
	@RequestMapping("/listCount")
	@RequiresPermissions(value = { "客户统计" })
	public Map<String,Object> listCount(ReturnList returnList,ReturnListDrugs returnListDrugs) {
		Map<String, Object> resultMap = new HashMap<>();
		List<ReturnList> returnListList=returnListService.list(returnList, Direction.DESC, "returnDate");
		for(ReturnList pl:returnListList){
			returnListDrugs.setReturnList(pl);
			List<ReturnListDrugs> rlgList=returnListDrugsService.list(returnListDrugs);
			for(ReturnListDrugs rlg:rlgList){
				rlg.setReturnList(null);
			}
			pl.setReturnListDrugsList(rlgList);
		}
		logger.info("获取退货单的所有药品信息");
		resultMap.put("rows", returnListList);
		return resultMap;
	}
	
	/*
	 * 获取退货单号
	 */
	@ResponseBody
	@RequestMapping("/getReturnNumber")
	@RequiresPermissions(value = {"退货出库"})
	public String genBillCode(String type)throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("TH");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String returnNumber=returnListService.getTodayMaxReturnNumber(); // 获取当天最大的退货单号
		if(returnNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(returnNumber));
			logger.info("获取退货单号");
		}else{
			logger.warn("退货单号为空");
			biilCodeStr.append("0001");
		}
		return biilCodeStr.toString();
	}
	
	/*
	 * 添加退货单 以及所有退货单药品 以及 修改药品的成本均价
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions(value = {"退货出库"})
	public Map<String,Object> save(ReturnList returnList,String drugsJson) {
		Map<String, Object> resultMap = new HashMap<>();
		returnList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
		Gson gson = new Gson();
		List<ReturnListDrugs> plgList=gson.fromJson(drugsJson, new TypeToken<List<ReturnListDrugs>>(){}.getType());
		returnListService.save(returnList, plgList);
		logger.info("添加退货单");
		logService.save(new Log(Log.ADD_ACTION,"添加退货单"));
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 修改退货单的支付状态
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions(value = {"供应商统计"})
	public Map<String,Object> update(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		ReturnList returnList=returnListService.findById(id);
		returnList.setState(1); // 修改成支付状态
		returnListService.update(returnList);
		logger.info("修改退货单的支付状态");
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 根据id删除退货单信息 包括退货单里的药品
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "退货单据查询" })
	public Map<String,Object> delete(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		returnListService.delete(id);
		logger.info("删除退货单信息"+returnListService.findById(id));
		logService.save(new Log(Log.DELETE_ACTION,"删除退货单信息"+returnListService.findById(id)));  // 写入日志
		resultMap.put("success", true);		
		return resultMap;
	}
}
