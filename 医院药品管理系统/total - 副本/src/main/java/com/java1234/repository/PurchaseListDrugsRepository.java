package com.java1234.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.PurchaseListDrugs;

/*
 * 进货单药品Repository接口
 * @author java1234 AT
 *
 */
public interface PurchaseListDrugsRepository extends JpaRepository<PurchaseListDrugs, Integer>,JpaSpecificationExecutor<PurchaseListDrugs>{

	/*
	 * 根据进货单id查询所有进货单药品
	 */
	@Query(value="select * from t_purchase_list_drugs where purchase_list_id=?1",nativeQuery=true)
    List<PurchaseListDrugs> listByPurchaseListId(Integer purchaseListId);
	
	/*
	 * 删除指定进货单的所有药品
	 */
	@Query(value="delete from t_purchase_list_drugs where purchase_list_id=?1",nativeQuery=true)
	@Modifying
    void deleteByPurchaseListId(Integer purchaseListId);
}
