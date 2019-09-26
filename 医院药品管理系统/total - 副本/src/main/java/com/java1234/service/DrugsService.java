package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.Drugs;

/*
 * 药品Service接口
 * @author java1234 AT
 *
 */
public interface DrugsService {

	
	/*
	 * 根据id查询药品实体
	 */
    Drugs findById(Integer id);
	
	/*
	 * 修改或者修改药品信息
	 */
    void save(Drugs drugs);
	
	/*
	 * 根据条件分页查询药品信息
	 */
    List<Drugs> list(Drugs drugs, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 根据药品编码或者名称分页查询没有库存的药品信息
	 */
    List<Drugs> listNoInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 分页查询有库存的药品信息
	 */
    List<Drugs> listHasInventoryQuantity(Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 获取总记录数
	 */
    Long getCount(Drugs drugs);
	
	/*
	 * 根据药品编码或者名称查询没有库存的药品信息信息总记录数
	 */
    Long getCountNoInventoryQuantityByCodeOrName(String codeOrName);
	
	/*
	 * 查询有库存的药品信息信息总记录数
	 */
    Long getCountHasInventoryQuantity();
	
	/*
	 * 根据id删除药品
	 */
    void delete(Integer id);
	
	/*
	 * 获取最大的药品编号
	 */
    String getMaxDrugsCode();
	
	/*
	 * 查询库存报警药品 库存小于库存下限的药品
	 */
    List<Drugs> listAlarm();
	

}
