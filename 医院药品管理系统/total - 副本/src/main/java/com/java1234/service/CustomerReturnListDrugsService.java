package com.java1234.service;

import java.util.List;

import com.java1234.entity.CustomerReturnListDrugs;
import com.java1234.entity.PurchaseListDrugs;
import org.springframework.data.domain.Sort;

/*
 * 客户退货单药品Service接口
 * @author java1234_AT
 *
 */
public interface CustomerReturnListDrugsService {

	/*
	 * 根据客户退货单id查询所有客户退货单药品
	 */
    List<CustomerReturnListDrugs> listByCustomerReturnListId(Integer customerReturnListId);

	/*
	 * 统计某个药品的客户退货总量
	 */
    Integer getTotalByDrugsId(Integer drugsId);
	
	/*
	 * 根据条件查询客户退货单药品
	 */
    List<CustomerReturnListDrugs> list(CustomerReturnListDrugs customerReturnListDrugs);


}
