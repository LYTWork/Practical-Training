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

import com.java1234.entity.ReturnListDrugs;
import com.java1234.repository.ReturnListDrugsRepository;
import com.java1234.service.ReturnListDrugsService;
import com.java1234.util.StringUtil;

/*
 * 退货单药品Service实现类
 * @author java1234_AT
 *
 */
@Service("returnListDrugsService")
public class ReturnListDrugsServiceImpl implements ReturnListDrugsService{

	@Resource
	private ReturnListDrugsRepository returnListDrugsRepository;

	@Override
	public List<ReturnListDrugs> listByReturnListId(Integer returnListId) {
		return returnListDrugsRepository.listByReturnListId(returnListId);
	}

	@Override
	public List<ReturnListDrugs> list(ReturnListDrugs returnListDrugs) {
		return returnListDrugsRepository.findAll(new Specification<ReturnListDrugs>() {
			
			@Override
			public Predicate toPredicate(Root<ReturnListDrugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(returnListDrugs!=null){
					if(returnListDrugs.getType()!=null && returnListDrugs.getType().getId()!=null && returnListDrugs.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), returnListDrugs.getType().getId()));
					}
					if(StringUtil.isNotEmpty(returnListDrugs.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+returnListDrugs.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+returnListDrugs.getCodeOrName()+"%")));
					}
					if(returnListDrugs.getReturnList()!=null && StringUtil.isNotEmpty(returnListDrugs.getReturnList().getReturnNumber())){
						predicate.getExpressions().add(cb.like(root.get("returnList").get("returnNumber"), "%"+returnListDrugs.getReturnList().getReturnNumber()+"%"));
					}
				}
				return predicate;
			}
		});
	}



}
