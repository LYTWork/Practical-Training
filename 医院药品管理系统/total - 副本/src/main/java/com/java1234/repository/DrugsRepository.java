package com.java1234.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.Drugs;


/*
 * 药品Repository接口
 * @author java1234 AT
 *
 */
public interface DrugsRepository extends JpaRepository<Drugs, Integer>,JpaSpecificationExecutor<Drugs>{

	/*
	 * 获取最大的药品编号
	 */
	@Query(value="select max(code) from t_drugs",nativeQuery=true)
    String getMaxDrugsCode();
	
	/*
	 * 查询库存报警药品 库存小于库存下限的药品
	 */
	@Query(value="SELECT * FROM t_drugs WHERE inventory_quantity<min_num",nativeQuery=true)
    List<Drugs> listAlarm();
	
}
