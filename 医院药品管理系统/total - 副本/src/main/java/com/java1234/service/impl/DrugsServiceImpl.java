package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.entity.Drugs;
import com.java1234.repository.DrugsRepository;
import com.java1234.service.DrugsService;
import com.java1234.util.StringUtil;

/*
 * 药品Service实现类
 * @author java1234 AT
 *
 */
@Service("drugsService")
public class DrugsServiceImpl implements DrugsService {

	@Resource
	private DrugsRepository drugsRepository;
	
	@Override
	public Drugs findById(Integer id) {
		return drugsRepository.findOne(id);
	}

	@Override
	public void save(Drugs drugs) {
		drugsRepository.save(drugs);
	}

	@Override
	public List<Drugs> list(Drugs drugs, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Drugs> pageUser=drugsRepository.findAll(new Specification<Drugs>() {
			
			@Override
			public Predicate toPredicate(Root<Drugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(drugs!=null){
					if(StringUtil.isNotEmpty(drugs.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+drugs.getName().trim()+"%"));
					}	
					if(drugs.getType()!=null && drugs.getType().getId()!=null && drugs.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), drugs.getType().getId()));
					}
					if(StringUtil.isNotEmpty(drugs.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+drugs.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+drugs.getCodeOrName()+"%")));
					}
				}
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCount(Drugs drugs) {
		Long count=drugsRepository.count(new Specification<Drugs>() {

			@Override
			public Predicate toPredicate(Root<Drugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(drugs!=null){
					if(StringUtil.isNotEmpty(drugs.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+drugs.getName().trim()+"%"));
					}	
					if(drugs.getType()!=null && drugs.getType().getId()!=null && drugs.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), drugs.getType().getId()));
					}
					if(StringUtil.isNotEmpty(drugs.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+drugs.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+drugs.getCodeOrName()+"%")));
					}
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		drugsRepository.delete(id);
	}

	@Override
	public String getMaxDrugsCode() {
		return drugsRepository.getMaxDrugsCode();
	}

	@Override
	public List<Drugs> listNoInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize,
			Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Drugs> pageUser=drugsRepository.findAll(new Specification<Drugs>() {
			
			@Override
			public Predicate toPredicate(Root<Drugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtil.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+codeOrName+"%"), cb.like(root.get("name"),"%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0)); // 库存是0
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCountNoInventoryQuantityByCodeOrName(String codeOrName) {
		Long count=drugsRepository.count(new Specification<Drugs>() {

			@Override
			public Predicate toPredicate(Root<Drugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtil.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+codeOrName+"%"), cb.like(root.get("name"),"%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0)); // 库存是0
				return predicate;
			}
		});
		return count;
	}

	@Override
	public List<Drugs> listHasInventoryQuantity(Integer page, Integer pageSize, Direction direction,
			String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Drugs> pageUser=drugsRepository.findAll(new Specification<Drugs>() {
			
			@Override
			public Predicate toPredicate(Root<Drugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0)); // 库存不是0
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCountHasInventoryQuantity() {
		Long count=drugsRepository.count(new Specification<Drugs>() {

			@Override
			public Predicate toPredicate(Root<Drugs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0)); // 库存不是0
				return predicate;
			}
		});
		return count;
	}

	@Override
	public List<Drugs> listAlarm() {
		return drugsRepository.listAlarm();
	}



}
