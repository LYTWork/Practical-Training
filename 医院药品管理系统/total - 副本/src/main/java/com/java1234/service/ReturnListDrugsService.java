package com.java1234.service;

import java.util.List;

import com.java1234.entity.PurchaseListDrugs;
import com.java1234.entity.ReturnListDrugs;
import org.springframework.data.domain.Sort;

/*
 * 退货单药品Service接口
 * @author java1234_AT
 *
 */
public interface ReturnListDrugsService {

	/*
	 * 根据退货单id查询所有退货单药品
	 */
    List<ReturnListDrugs> listByReturnListId(Integer returnListId);
	
	/*
	 * 根据条件查询退货单所有药品
	 */
    List<ReturnListDrugs> list(ReturnListDrugs returnListDrugs);


}
