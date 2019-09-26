package com.java1234.service;

import java.util.List;

import com.java1234.entity.Customer;
import com.java1234.entity.PurchaseListDrugs;
import org.springframework.data.domain.Sort;

/*
 * 进货单药品Service接口
 * @author java1234_AT
 *
 */
public interface PurchaseListDrugsService {

	/*
	 * 根据进货单id查询所有进货单药品
	 */
    List<PurchaseListDrugs> listByPurchaseListId(Integer purchaseListId);
	
	/*
	 * 根据条件查询进货单药品
	 */
    List<PurchaseListDrugs> list(PurchaseListDrugs purchaseListDrugs);


}
