package com.java1234.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.CustomerReturnListDrugs;

/*
 * 客户退货单药品Repository接口
 * @author java1234 AT
 *
 */
public interface CustomerReturnListDrugsRepository extends JpaRepository<CustomerReturnListDrugs, Integer>,JpaSpecificationExecutor<CustomerReturnListDrugs>{

	/*
	 * 根据客户退货单id查询所有客户退货单药品
	 */
	@Query(value="select * from t_customer_return_list_drugs where customer_return_list_id=?1",nativeQuery=true)
    List<CustomerReturnListDrugs> listByCustomerReturnListId(Integer customerReturnListId);
	
	/*
	 * 删除指定客户退货单的所有药品
	 */
	@Query(value="delete from t_customer_return_list_drugs where customer_return_list_id=?1",nativeQuery=true)
	@Modifying
    void deleteByCustomerReturnListId(Integer customerReturnListId);
	
	/*
	 * 统计某个药品的客户退货总量
	 */
	@Query(value="SELECT SUM(num) AS total FROM t_customer_return_list_drugs WHERE drugs_id=?1",nativeQuery=true)
    Integer getTotalByDrugsId(Integer drugsId);
}
