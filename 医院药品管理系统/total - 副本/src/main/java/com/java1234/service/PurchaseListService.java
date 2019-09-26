package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.PurchaseList;
import com.java1234.entity.PurchaseListDrugs;

/*
 * 进货单Service接口
 * @author java1234_AT
 *
 */
public interface PurchaseListService {

	/*
	 * 根据id查询实体
	 */
    PurchaseList findById(Integer id);
	
	/*
	 * 获取当天最大进货单号
	 */
    String getTodayMaxPurchaseNumber();
	
	/*
	 * 添加进货单 以及所有进货单药品 以及 修改药品的成本均价
	 */
    void save(PurchaseList purchaseList, List<PurchaseListDrugs> purchaseListDrugsList);
	
	/*
	 * 根据条件查询进货单信息
	 */
    List<PurchaseList> list(PurchaseList purchaseList, Direction direction, String... properties);
	
	/*
	 * 根据id删除进货单信息 包括进货单里的药品
	 */
    void delete(Integer id);
	
	/*
	 * 更新进货单
	 */
    void update(PurchaseList purchaseList);

}
