package com.java1234.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.DrugsType;

/*
 * 药品类别Repository接口
 * @author java1234 AT
 */
public interface DrugsTypeRepository extends JpaRepository<DrugsType, Integer>,JpaSpecificationExecutor<DrugsType>{

	/*
	 * 根据父节点查找药品类别
	 */
	@Query(value="select * from t_drugstype where p_id=?1",nativeQuery=true)
    List<DrugsType> findByParentId(int parentId);
}
