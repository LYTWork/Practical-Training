package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.entity.Drugs;
import com.java1234.entity.SaleList;
import com.java1234.entity.SaleListDrugs;
import com.java1234.repository.DrugsRepository;
import com.java1234.repository.DrugsTypeRepository;
import com.java1234.repository.SaleListDrugsRepository;
import com.java1234.repository.SaleListRepository;
import com.java1234.service.SaleListService;
import com.java1234.util.StringUtil;

/*
 * 销售单Service实现类
 * @author java1234_AT
 *
 */
@Service("saleListService")
@Transactional
public class SaleListServiceImpl implements SaleListService{

	@Resource
	private SaleListRepository saleListRepository;
	
	@Resource
	private SaleListDrugsRepository saleListDrugsRepository;
	
	@Resource
	private DrugsRepository drugsRepository;
	
	@Resource
	private DrugsTypeRepository drugsTypeRepository;
	
	@Override
	public String getTodayMaxSaleNumber() {
		return saleListRepository.getTodayMaxSaleNumber();
	}

	@Transactional
	public void save(SaleList saleList, List<SaleListDrugs> saleListDrugsList) {
		// 保存每个销售单药品
		for(SaleListDrugs saleListDrugs:saleListDrugsList){
			saleListDrugs.setType(drugsTypeRepository.findOne(saleListDrugs.getTypeId())); // 设置类别
			saleListDrugs.setSaleList(saleList); // 设置采购单
			saleListDrugsRepository.save(saleListDrugs);
			// 修改药品库存
			Drugs drugs=drugsRepository.findOne(saleListDrugs.getDrugsId());
			drugs.setInventoryQuantity(drugs.getInventoryQuantity()-saleListDrugs.getNum());
			drugs.setState(2);
			drugsRepository.save(drugs);
		}
		saleListRepository.save(saleList); // 保存销售单
	}

	@Override
	public List<SaleList> list(SaleList saleList, Direction direction,
			String... properties) {
		return saleListRepository.findAll(new Specification<SaleList>(){

			@Override
			public Predicate toPredicate(Root<SaleList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(saleList!=null){
					if(saleList.getCustomer()!=null && saleList.getCustomer().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), saleList.getCustomer().getId()));
					}
					if(StringUtil.isNotEmpty(saleList.getSaleNumber())){
						predicate.getExpressions().add(cb.like(root.get("saleNumber"), "%"+saleList.getSaleNumber().trim()+"%"));
					}
					if(saleList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), saleList.getState()));
					}
					if(saleList.getbSaleDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("saleDate"), saleList.getbSaleDate()));
					}
					if(saleList.geteSaleDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("saleDate"), saleList.geteSaleDate()));
					}
				}
				return predicate;
			}
		  },new Sort(direction, properties));
	}

	@Override
	public void delete(Integer id) {
		saleListDrugsRepository.deleteBySaleListId(id);
		saleListRepository.delete(id);
	}

	@Override
	public SaleList findById(Integer id) {
		return saleListRepository.findOne(id);
	}

	@Override
	public void update(SaleList saleList) {
		saleListRepository.save(saleList);
	}

	@Override
	public List<Object> countSaleByDay(String begin, String end) {
		return saleListRepository.countSaleByDay(begin, end);
	}

	@Override
	public List<Object> countSaleByMonth(String begin, String end) {
		return saleListRepository.countSaleByMonth(begin, end);
	}



}
