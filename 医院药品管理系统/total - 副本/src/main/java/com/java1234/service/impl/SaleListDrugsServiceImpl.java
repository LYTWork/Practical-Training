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

import com.java1234.entity.SaleListDrugs;
import com.java1234.repository.SaleListDrugsRepository;
import com.java1234.service.SaleListDrugsService;
import com.java1234.util.StringUtil;

/*
 * 销售单药品Service实现类
 * @author java1234_AT
 *
 */
@Service("saleListDrugsService")
public class SaleListDrugsServiceImpl implements SaleListDrugsService{

	@Resource
	private SaleListDrugsRepository saleListDrugsRepository;

	@Override
	public List<SaleListDrugs> listBySaleListId(Integer saleListId) {
		return saleListDrugsRepository.listBySaleListId(saleListId);
	}

	@Override
	public Integer getTotalByDrugsId(Integer drugsId) {
		return saleListDrugsRepository.getTotalByDrugsId(drugsId)==null?0:saleListDrugsRepository.getTotalByDrugsId(drugsId);
	}

	@Override
	public List<SaleListDrugs> list(SaleListDrugs saleListDrugs) {
		return saleListDrugsRepository.findAll(new Specification<SaleListDrugs>() {
					
					@Override
					public Predicate toPredicate(Root<SaleListDrugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						Predicate predicate=cb.conjunction();
						if(saleListDrugs!=null){
							if(saleListDrugs.getType()!=null && saleListDrugs.getType().getId()!=null && saleListDrugs.getType().getId()!=1){
								predicate.getExpressions().add(cb.equal(root.get("type").get("id"), saleListDrugs.getType().getId()));
							}
							if(StringUtil.isNotEmpty(saleListDrugs.getCodeOrName())){
								predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+saleListDrugs.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+saleListDrugs.getCodeOrName()+"%")));
							}
							if(saleListDrugs.getSaleList()!=null && StringUtil.isNotEmpty(saleListDrugs.getSaleList().getSaleNumber())){
								predicate.getExpressions().add(cb.like(root.get("saleList").get("saleNumber"), "%"+saleListDrugs.getSaleList().getSaleNumber()+"%"));
							}
						}
						return predicate;
					}
				});
	}



}
