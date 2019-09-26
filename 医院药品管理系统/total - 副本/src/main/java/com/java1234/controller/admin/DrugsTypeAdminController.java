package com.java1234.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.java1234.entity.DrugsType;
import com.java1234.entity.Log;
import com.java1234.service.DrugsTypeService;
import com.java1234.service.LogService;

/*
 * 后台管理药品类别Controller
 * @author java1234 AT
 * @author java1234 lyw
 */
@RestController
@RequestMapping("/admin/drugsType")
public class DrugsTypeAdminController {

	@Resource
	private DrugsTypeService drugsTypeService;
	
	@Resource
	private LogService logService;

	protected static final Logger logger = LoggerFactory.getLogger(DrugsTypeAdminController.class);
	
	/*
	 * 加载药品类别树菜单
	 */
    @PostMapping("/loadTreeInfo")
	@RequiresPermissions(value = { "药品管理","进货入库","当前库存查询"},logical=Logical.OR)
	public String loadTreeInfo() {
    	logger.info("查询药品类别信息");
    	logService.save(new Log(Log.SEARCH_ACTION,"查询药品类别信息")); // 写入日志
		return getAllByParentId(-1).toString();
	}
	
	/*
	 * 添加药品类别
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "药品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> save(String name,Integer parentId) {
		Map<String, Object> resultMap = new HashMap<>();
		DrugsType drugsType=new DrugsType();
		drugsType.setName(name);
		drugsType.setpId(parentId);
		drugsType.setIcon("icon-folder");
		drugsType.setState(0);
		logger.info("添加药品类别信息"+drugsType);
		logService.save(new Log(Log.ADD_ACTION,"添加药品类别信息"+drugsType));
		drugsTypeService.save(drugsType); // 保存药品类别
		DrugsType parentDrugsType=drugsTypeService.findById(parentId); // 查找父节点
		parentDrugsType.setState(1); // 修改state 1 根节点
		drugsTypeService.save(parentDrugsType); // 保存父节点药品类别
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 药品类别删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "药品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> delete(Integer id) {
		Map<String, Object> resultMap = new HashMap<>();
		DrugsType drugsType=drugsTypeService.findById(id);
		if(drugsTypeService.findByParentId(drugsType.getpId()).size()==1){ // 假如父节点下只有当前这个子节点，修改下 父节点的state状态
			DrugsType parentDrugsType=drugsTypeService.findById(drugsType.getpId());
			parentDrugsType.setState(0); // 修改state 0  叶子节点
			drugsTypeService.save(parentDrugsType); // 保存父节点药品类别
		}
		logger.info("删除药品类别信息"+drugsType);
		logService.save(new Log(Log.DELETE_ACTION,"删除药品类别信息"+drugsType));  // 写入日志
		drugsTypeService.delete(id); // 删除
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/*
	 * 根据父节点递归获取所有药品类别信息
	 */
	public JsonArray getAllByParentId(Integer parentId){
		JsonArray jsonArray=this.getByParentId(parentId);
		for(int i=0;i<jsonArray.size();i++){
			JsonObject jsonObject=(JsonObject) jsonArray.get(i);
    		if("open".equals(jsonObject.get("state").getAsString())){
    			continue;
    		}else{
    			jsonObject.add("children", getAllByParentId(jsonObject.get("id").getAsInt()));
    		}
		}
		return jsonArray;
	}
	
	/*
	 * 根据父节点查询子节点
	 */
	private JsonArray getByParentId(Integer parentId){
		JsonArray jsonArray=new JsonArray();
		List<DrugsType> drugsTypeList=drugsTypeService.findByParentId(parentId);
		for(DrugsType drugsType:drugsTypeList){
			JsonObject jsonObject=new JsonObject();
			jsonObject.addProperty("id", drugsType.getId()); // 节点id
			jsonObject.addProperty("text", drugsType.getName()); // 节点名称
			if(drugsType.getState()==1){
    			jsonObject.addProperty("state", "closed"); // 根节点
    		}else{
    			jsonObject.addProperty("state", "open"); // 叶子节点
    		}
			jsonObject.addProperty("iconCls", drugsType.getIcon());
			JsonObject attributeObject=new JsonObject(); // 扩展属性
    		attributeObject.addProperty("state",drugsType.getState()); // 节点状态
			jsonObject.add("attributes", attributeObject);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
}
