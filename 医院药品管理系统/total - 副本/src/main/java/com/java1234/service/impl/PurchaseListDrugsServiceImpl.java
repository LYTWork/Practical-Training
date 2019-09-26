package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.java1234.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.entity.PurchaseListDrugs;
import com.java1234.repository.PurchaseListDrugsRepository;
import com.java1234.service.PurchaseListDrugsService;
import com.java1234.util.StringUtil;

/*
 * 进货单药品Service实现类
 * @author java1234_AT
 *
 */
@Service("purchaseListDrugsService")
public class PurchaseListDrugsServiceImpl implements PurchaseListDrugsService{

	@Resource
	private PurchaseListDrugsRepository purchaseListDrugsRepository;

	@Override
	public List<PurchaseListDrugs> listByPurchaseListId(Integer purchaseListId) {
		return purchaseListDrugsRepository.listByPurchaseListId(purchaseListId);
	}

	@Override
	public List<PurchaseListDrugs> list(PurchaseListDrugs purchaseListDrugs) {
		return purchaseListDrugsRepository.findAll(new Specification<PurchaseListDrugs>() {
			
			@Override
			public Predicate toPredicate(Root<PurchaseListDrugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(purchaseListDrugs!=null){
					if(purchaseListDrugs.getType()!=null && purchaseListDrugs.getType().getId()!=null && purchaseListDrugs.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), purchaseListDrugs.getType().getId()));
					}
					if(StringUtil.isNotEmpty(purchaseListDrugs.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+purchaseListDrugs.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+purchaseListDrugs.getCodeOrName()+"%")));
					}
					if(purchaseListDrugs.getPurchaseList()!=null && StringUtil.isNotEmpty(purchaseListDrugs.getPurchaseList().getPurchaseNumber())){
						predicate.getExpressions().add(cb.like(root.get("purchaseList").get("purchaseNumber"), "%"+purchaseListDrugs.getPurchaseList().getPurchaseNumber()+"%"));
					}
				}
				return predicate;
			}
		});
	}




}
