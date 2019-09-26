package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.SaleList;
import com.java1234.entity.SaleListDrugs;

/*
 * 销售单Service接口
 * @author java1234_AT
 *
 */
public interface SaleListService {

	/*
	 * 根据id查询实体
	 */
    SaleList findById(Integer id);
	
	/*
	 * 获取当天最大销售单号
	 */
    String getTodayMaxSaleNumber();
	
	/*
	 * 添加销售单 以及所有销售单药品 以及 修改药品的成本均价
	 */
    void save(SaleList saleList, List<SaleListDrugs> saleListDrugsList);
	
	/*
	 * 根据条件查询销售单信息
	 */
    List<SaleList> list(SaleList saleList, Direction direction, String... properties);
	
	/*
	 * 根据id删除销售单信息 包括销售单里的药品
	 */
    void delete(Integer id);
	
	/*
	 * 更新销售单
	 */
    void update(SaleList saleList);
	
	/*
	 * 按天统计某个日期范围内的销售信息
	 */
    List<Object> countSaleByDay(String begin, String end);
	
	/*
	 * 按月统计某个日期范围内的销售信息
	 */
    List<Object> countSaleByMonth(String begin, String end);

}
