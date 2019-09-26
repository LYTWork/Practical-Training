package com.java1234.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.SaleListDrugs;

/*
 * 销售单药品Repository接口
 * @author java1234 AT
 *
 */
public interface SaleListDrugsRepository extends JpaRepository<SaleListDrugs, Integer>,JpaSpecificationExecutor<SaleListDrugs>{

	/*
	 * 根据销售单id查询所有销售单药品
	 */
	@Query(value="select * from t_sale_list_drugs where sale_list_id=?1",nativeQuery=true)
    List<SaleListDrugs> listBySaleListId(Integer saleListId);
	
	/*
	 * 删除指定销售单的所有药品
	 */
	@Query(value="delete from t_sale_list_drugs where sale_list_id=?1",nativeQuery=true)
	@Modifying
    void deleteBySaleListId(Integer saleListId);
	
	/*
	 * 统计某个药品的销售总量
	 */
	@Query(value="SELECT SUM(num) AS total FROM t_sale_list_drugs WHERE drugs_id=?1",nativeQuery=true)
    Integer getTotalByDrugsId(Integer drugsId);
	
}
