package com.java1234.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.Supplier;

/*
 * 供应商Repository接口
 * @author java1234 AT
 *
 */
public interface SupplierRepository extends JpaRepository<Supplier, Integer>,JpaSpecificationExecutor<Supplier>{

	/*
	 * 根据名称模糊查询供应商信息
	 */
	@Query(value="select * from t_supplier where name like ?1",nativeQuery=true)
    List<Supplier> findByName(String name);
	
}
