package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.java1234.entity.PurchaseListDrugs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.entity.CustomerReturnListDrugs;
import com.java1234.repository.CustomerReturnListDrugsRepository;
import com.java1234.service.CustomerReturnListDrugsService;
import com.java1234.util.StringUtil;
/*
 * 客户退货单药品Service实现类
 * @author java1234_AT
 *
 */

@Service("customerReturnListDrugsService")
public class CustomerReturnListDrugsServiceImpl implements CustomerReturnListDrugsService{

	@Resource
	private CustomerReturnListDrugsRepository customerReturnListDrugsRepository;

	@Override
	public List<CustomerReturnListDrugs> listByCustomerReturnListId(Integer customerReturnListId) {
		return customerReturnListDrugsRepository.listByCustomerReturnListId(customerReturnListId);
	}

	@Override
	public Integer getTotalByDrugsId(Integer drugsId) {
		return customerReturnListDrugsRepository.getTotalByDrugsId(drugsId)==null?0:customerReturnListDrugsRepository.getTotalByDrugsId(drugsId);
	}

	@Override
	public List<CustomerReturnListDrugs> list(CustomerReturnListDrugs customerReturnListDrugs) {
		return customerReturnListDrugsRepository.findAll(new Specification<CustomerReturnListDrugs>() {
					
					@Override
					public Predicate toPredicate(Root<CustomerReturnListDrugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						Predicate predicate=cb.conjunction();
						if(customerReturnListDrugs!=null){
							if(customerReturnListDrugs.getType()!=null && customerReturnListDrugs.getType().getId()!=null && customerReturnListDrugs.getType().getId()!=1){
								predicate.getExpressions().add(cb.equal(root.get("type").get("id"), customerReturnListDrugs.getType().getId()));
							}
							if(StringUtil.isNotEmpty(customerReturnListDrugs.getCodeOrName())){
								predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+customerReturnListDrugs.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+customerReturnListDrugs.getCodeOrName()+"%")));
							}
							if(customerReturnListDrugs.getCustomerReturnList()!=null && StringUtil.isNotEmpty(customerReturnListDrugs.getCustomerReturnList().getCustomerReturnNumber())){
								predicate.getExpressions().add(cb.like(root.get("customerReturnList").get("customerReturnNumber"), "%"+customerReturnListDrugs.getCustomerReturnList().getCustomerReturnNumber()+"%"));
							}
						}
						return predicate;
					}
				});
	}




}
