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
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.Drugs;
import com.java1234.entity.Log;
import com.java1234.service.CustomerReturnListDrugsService;
import com.java1234.service.DrugsService;
import com.java1234.service.LogService;
import com.java1234.util.StringUtil;

/*
 * 后台管理药品Controller
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/drugs")
public class DrugsAdminContrller {

	@Resource
	private DrugsService drugsService;
	
	@Resource
	private CustomerReturnListDrugsService customerReturnListDrugsService;
	
	@Resource
	private LogService logService;

	protected static final Logger logger = LoggerFactory.getLogger(DrugsAdminContrller.class);
	
	/*
	 * 根据条件分页查询药品信息
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "药品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> list(Drugs drugs,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Drugs> drugsList=drugsService.list(drugs, page, rows, Direction.ASC, "id");
		Long total=drugsService.getCount(drugs);
		resultMap.put("rows", drugsList);
		resultMap.put("total", total);
		logger.info("查询药品信息");
		logService.save(new Log(Log.SEARCH_ACTION,"查询药品信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 根据条件分页查询药品库存信息
	 */
	@RequestMapping("/listInventory")
	@RequiresPermissions(value = { "当前库存查询" })
	public Map<String,Object> listInventory(Drugs drugs,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Drugs> drugsList=drugsService.list(drugs, page, rows, Direction.ASC, "id");
		Long total=drugsService.getCount(drugs);
		resultMap.put("rows", drugsList);
		resultMap.put("total", total);
		logger.info("查询药品库存信息");
		logService.save(new Log(Log.SEARCH_ACTION,"查询药品库存信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 查询库存报警药品
	 */
	@RequestMapping("/listAlarm")
	@RequiresPermissions(value = { "库存报警" })
	public Map<String,Object> listAlart() {
		Map<String, Object> resultMap = new HashMap<>();
		List<Drugs> alarmDrugsList=drugsService.listAlarm();
		resultMap.put("rows", alarmDrugsList);
		logger.info("查询库存报警药品");
		return resultMap;
	}
	
	/*
	 * 根据条件分页查询没有库存的药品信息
	 */
	@RequestMapping("/listNoInventoryQuantity")
	@RequiresPermissions(value = { "期初库存" })
	public Map<String,Object> listNoInventoryQuantity(@RequestParam(value="codeOrName",required=false)String codeOrName,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Drugs> drugsList=drugsService.listNoInventoryQuantityByCodeOrName(codeOrName, page, rows, Direction.ASC, "id");
		Long total=drugsService.getCountNoInventoryQuantityByCodeOrName(codeOrName);
		resultMap.put("rows", drugsList);
		resultMap.put("total", total);
		logger.info("查询药品信息（无库存）");
		logService.save(new Log(Log.SEARCH_ACTION,"查询药品信息（无库存）")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 分页查询有库存的药品信息
	 */
	@RequestMapping("/listHasInventoryQuantity")
	@RequiresPermissions(value = { "期初库存" })
	public Map<String,Object> listHasInventoryQuantity(@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Drugs> drugsList=drugsService.listHasInventoryQuantity(page, rows, Direction.ASC, "id");
		Long total=drugsService.getCountHasInventoryQuantity();
		resultMap.put("rows", drugsList);
		resultMap.put("total", total);
		logger.info("查询药品信息（有库存）");
		logService.save(new Log(Log.SEARCH_ACTION,"查询药品信息（有库存）")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 删除库存 把药品的库存设置成0
	 */
	@RequestMapping("/deleteStock")
	@RequiresPermissions(value = { "期初库存" })
	public Map<String,Object> deleteStock(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		Drugs drugs=drugsService.findById(id);
		if(drugs.getState()==2){ // 2表示有进货或者销售单据 不能删除
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该药品已经发生单据，不能删除！");
			logger.error("该药品已经发生单据，不能删除！");
		}else{
			drugs.setInventoryQuantity(0);
			drugsService.save(drugs);
			resultMap.put("success", true);
			logger.info("删除库存");
		}
		return resultMap;			
	}
	
	/*
	 * 生成药品编码
	 */
	@RequestMapping("/genDrugsCode")
	@RequiresPermissions(value = { "药品管理" })
	public String genDrugsCode() {
		String maxDrugsCode=drugsService.getMaxDrugsCode();
		if(StringUtil.isNotEmpty(maxDrugsCode)){
			Integer code = Integer.valueOf(maxDrugsCode)+1;
			String codes = code.toString();
			int length = codes.length();
			for (int i = 4; i > length; i--) {
				codes = "0"+codes;
			}
			return codes;
		}else{
			return "0001";
		}
	}
	
	/*
	 * 添加药品
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "药品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> save(Drugs drugs) {
		if(drugs.getId()!=null){ // 写入日志
			logger.info("更新药品信息"+drugs);
			logService.save(new Log(Log.UPDATE_ACTION,"更新药品信息"+drugs));
		}else{
			logger.info("添加药品信息"+drugs);
			logService.save(new Log(Log.ADD_ACTION,"添加药品信息"+drugs));
			drugs.setLastPurchasingPrice(drugs.getPurchasingPrice()); // 设置上次进价为当前价格
		}
		Map<String, Object> resultMap = new HashMap<>();
		drugsService.save(drugs);
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 添加药品到仓库 修改库存信息
	 */
	@RequestMapping("/saveStore")
	@RequiresPermissions(value = { "期初库存" })
	public Map<String,Object> saveStore(Integer id,Integer num,Float price) {
		Map<String, Object> resultMap = new HashMap<>();
		Drugs drugs=drugsService.findById(id);
		drugs.setInventoryQuantity(num);
		drugs.setPurchasingPrice(price);
		drugsService.save(drugs);
		logger.info("修改药品"+drugs+"，价格="+price+",库存="+num);
		logService.save(new Log(Log.UPDATE_ACTION,"修改药品"+drugs+"，价格="+price+",库存="+num)); // 写入日志
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 删除药品信息
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "药品管理" })
	public Map<String,Object> delete(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		Drugs drugs=drugsService.findById(id);
		if(drugs.getState()==1){
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该药品已经期初入库，不能删除！");
			logger.error("该药品已经期初入库，不能删除！");
		}else if(drugs.getState()==2){
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该药品已经发生单据，不能删除！");
			logger.error("该药品已经发生单据，不能删除！");
		}else{
			logger.info("删除药品信息"+drugsService.findById(id));
			logService.save(new Log(Log.DELETE_ACTION,"删除药品信息"+drugsService.findById(id)));  // 写入日志
			drugsService.delete(id);
			resultMap.put("success", true);			
		}
		return resultMap;
	}
}
