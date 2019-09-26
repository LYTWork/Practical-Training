package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.ReturnList;
import com.java1234.entity.ReturnListDrugs;

/*
 * 退货单Service接口
 * @author java1234_AT
 *
 */
public interface ReturnListService {

	/*
	 * 根据id查询实体
	 */
    ReturnList findById(Integer id);
	
	/*
	 * 获取当天最大退货单号
	 */
    String getTodayMaxReturnNumber();
	
	/*
	 * 添加退货单 以及所有退货单药品
	 */
    void save(ReturnList returnList, List<ReturnListDrugs> returnListDrugsList);
	
	/*
	 * 根据条件查询退货单信息
	 */
    List<ReturnList> list(ReturnList returnList, Direction direction, String... properties);
	
	/*
	 * 根据id删除退货单信息 包括退货单里的药
	 */
    void delete(Integer id);
	
	/*
	 * 更新退货单
	 */
    void update(ReturnList returnList);
}
