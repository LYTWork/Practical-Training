package com.java1234.controller.admin;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.java1234.entity.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java1234.service.LogService;
import com.java1234.service.SaleListDrugsService;
import com.java1234.service.SaleListService;
import com.java1234.service.UserService;
import com.java1234.util.DateUtil;
import com.java1234.util.MathUtil;
import com.java1234.util.StringUtil;

/*
 * 销售单Controller类
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/saleList")
public class SaleListAdminController {

	@Resource
	private SaleListService saleListService;
	
	@Resource
	private SaleListDrugsService saleListDrugsService;
	
	@Resource
	private LogService logService;
	
	@Resource
	private UserService userService;
	
	@InitBinder//对表单数据绑定(bean中定义了Date，double等类型，如果没有做任何处理的话，日期以及double都无法绑定。)
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
	/*
	 * 根据条件分页查询销售单信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "销售单据查询" })
	public Map<String,Object> list(SaleList saleList) {
		Map<String, Object> resultMap = new HashMap<>();
		List<SaleList> saleListList=saleListService.list(saleList, Direction.DESC, "saleDate");
		resultMap.put("rows", saleListList);
		return resultMap;
	}
	
	/*
	 * 根据销售单id查询所有销售单药品
	 */
	@RequestMapping("/listDrugs")
	@RequiresPermissions(value = { "销售单据查询" })
	public Map<String,Object> listDrugs(Integer saleListId) {
		if(saleListId==null){
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<SaleListDrugs> saleListDrugsList=saleListDrugsService.listBySaleListId(saleListId);
		resultMap.put("rows", saleListDrugsList);
		return resultMap;
	}

	
	/*
	 * 客户统计 获取销售单的所有药品信息
	 */
	@RequestMapping("/listCount")
	@RequiresPermissions(value = { "客户统计" })
	public Map<String,Object> listCount(SaleList saleList,SaleListDrugs saleListDrugs) {
		Map<String, Object> resultMap = new HashMap<>();
		List<SaleList> saleListList=saleListService.list(saleList, Direction.DESC, "saleDate");
		for(SaleList pl:saleListList){
			saleListDrugs.setSaleList(pl);
			List<SaleListDrugs> plgList=saleListDrugsService.list(saleListDrugs);
			for(SaleListDrugs plg:plgList){
				plg.setSaleList(null);
			}
			pl.setSaleListDrugsList(plgList);
		}
		resultMap.put("rows", saleListList);
		return resultMap;
	}
	
	/*
	 * 获取销售单号
	 */
	@ResponseBody
	@RequestMapping("/getSaleNumber")
	@RequiresPermissions(value = {"销售出库"})
	public String genBillCode(String type)throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("XS");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String saleNumber=saleListService.getTodayMaxSaleNumber(); // 获取当天最大的销售单号
		if(saleNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(saleNumber));//拼接在最大的销售单号基础上生成自增一的四位数
		}else{
			biilCodeStr.append("0001");
		}
		return biilCodeStr.toString();
	}
	
	/*
	 * 修改销售单的支付状态
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions(value = {"客户统计"})
	public Map<String,Object> update(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		SaleList saleList=saleListService.findById(id);
		saleList.setState(1); // 修改成支付状态
		saleListService.update(saleList);
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 添加销售单 以及所有销售单药品
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions(value = {"销售出库"})
	public Map<String,Object> save(SaleList saleList,String drugsJson) {
		Map<String, Object> resultMap = new HashMap<>();
		saleList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
		Gson gson = new Gson();
		List<SaleListDrugs> plgList=gson.fromJson(drugsJson, new TypeToken<List<SaleListDrugs>>(){}.getType());
		saleListService.save(saleList, plgList);
		logService.save(new Log(Log.ADD_ACTION,"添加销售单")); 
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 根据id删除销售单信息 包括销售单里的药品
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "销售单据查询" })
	public Map<String,Object> delete(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		saleListService.delete(id);
		logService.save(new Log(Log.DELETE_ACTION,"删除销售单信息"+saleListService.findById(id)));  // 写入日志
		resultMap.put("success", true);		
		return resultMap;
	}

}
