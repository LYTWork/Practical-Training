package com.java1234.controller.admin;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.java1234.entity.Customer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java1234.entity.Log;
import com.java1234.entity.PurchaseList;
import com.java1234.entity.PurchaseListDrugs;
import com.java1234.service.LogService;
import com.java1234.service.PurchaseListDrugsService;
import com.java1234.service.PurchaseListService;
import com.java1234.service.UserService;
import com.java1234.util.DateUtil;
import com.java1234.util.StringUtil;

/*
 * 进货单Controller类
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/purchaseList")
public class PurchaseListAdminController {

	@Resource
	private PurchaseListService purchaseListService;
	
	@Resource
	private PurchaseListDrugsService purchaseListDrugsService;
	
	@Resource
	private LogService logService;
	
	@Resource
	private UserService userService;
	
	@InitBinder//对表单数据绑定(entity中定义了Date，double等类型，如果没有做任何处理的话，日期以及double都无法绑定)
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}   
	
	/*
	 * 根据条件分页查询进货单信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "进货单据查询" })
	public Map<String,Object> list(PurchaseList purchaseList) {
		Map<String, Object> resultMap = new HashMap<>();
		List<PurchaseList> purchaseListList=purchaseListService.list(purchaseList, Direction.DESC, "purchaseDate");
		resultMap.put("rows", purchaseListList);
		return resultMap;
	}

	/*
	 * 根据进货单id查询所有进货单药品
	 */
	@RequestMapping("/listDrugs")
	@RequiresPermissions(value = { "进货单据查询" })
	public Map<String,Object> listDrugs(Integer purchaseListId) {
		if(purchaseListId==null){
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<PurchaseListDrugs> purchaseListDrugsList=purchaseListDrugsService.listByPurchaseListId(purchaseListId);
		resultMap.put("rows", purchaseListDrugsList);
		return resultMap;
	}

	
	/*
	 * 客户统计 获取进货单的所有药品信息
	 */
	@RequestMapping("/listCount")
	@RequiresPermissions(value = { "客户统计" })
	public Map<String,Object> listCount(PurchaseList purchaseList,PurchaseListDrugs purchaseListDrugs) {
		Map<String, Object> resultMap = new HashMap<>();
		List<PurchaseList> purchaseListList=purchaseListService.list(purchaseList, Direction.DESC, "purchaseDate");
		for(PurchaseList pl:purchaseListList){
			purchaseListDrugs.setPurchaseList(pl);
			List<PurchaseListDrugs> plgList=purchaseListDrugsService.list(purchaseListDrugs);
			for(PurchaseListDrugs plg:plgList){
				plg.setPurchaseList(null);
			}
			pl.setPurchaseListDrugsList(plgList);
		}
		resultMap.put("rows", purchaseListList);
		return resultMap;
	}
	
	/*
	 * 获取进货单号
	 */
	@ResponseBody
	@RequestMapping("/getPurchaseNumber")
	@RequiresPermissions(value = {"进货入库"})
	public String genBillCode(String type)throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("JH");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String purchaseNumber=purchaseListService.getTodayMaxPurchaseNumber(); // 获取当天最大的进货单号
		if(purchaseNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(purchaseNumber));
		}else{
			biilCodeStr.append("0001");
		}
		return biilCodeStr.toString();
	}
	
	/*
	 * 添加进货单 以及所有进货单药品 以及 修改药品的成本均价
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions(value = {"进货入库"})
	public Map<String,Object> save(PurchaseList purchaseList,String drugsJson) {
		Map<String, Object> resultMap = new HashMap<>();
		purchaseList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
		Gson gson = new Gson();
		List<PurchaseListDrugs> plgList=gson.fromJson(drugsJson, new TypeToken<List<PurchaseListDrugs>>(){}.getType());//****转json数据
		purchaseListService.save(purchaseList, plgList);//***********调
		logService.save(new Log(Log.ADD_ACTION,"添加进货单")); 
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 修改进货单的支付状态
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions(value = {"供应商统计"})
	public Map<String,Object> update(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		PurchaseList purchaseList=purchaseListService.findById(id);
		purchaseList.setState(1); // 修改成支付状态
		purchaseListService.update(purchaseList);
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 根据id删除进货单信息 包括进货单里的药品
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "进货单据查询" })
	public Map<String,Object> delete(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		purchaseListService.delete(id);
		logService.save(new Log(Log.DELETE_ACTION,"删除进货单信息"+purchaseListService.findById(id)));  // 写入日志
		resultMap.put("success", true);		
		return resultMap;
	}
	

}
