package com.java1234.service;

import java.util.List;

import com.java1234.entity.PurchaseListDrugs;
import com.java1234.entity.SaleListDrugs;
import org.springframework.data.domain.Sort;

/*
 * 销售单药品Service接口
 * @author java1234_AT
 *
 */
public interface SaleListDrugsService {

	/*
	 * 根据销售单id查询所有销售单药品
	 */
    List<SaleListDrugs> listBySaleListId(Integer saleListId);
	
	/*
	 * 统计某个药品的销售总量
	 */
    Integer getTotalByDrugsId(Integer drugsId);
	
	/*
	 * 根据条件查询销售单所有药品
	 */
    List<SaleListDrugs> list(SaleListDrugs saleListDrugs);



}
