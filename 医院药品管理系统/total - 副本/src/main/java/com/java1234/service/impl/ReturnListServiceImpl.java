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
import com.java1234.entity.ReturnList;
import com.java1234.entity.ReturnListDrugs;
import com.java1234.repository.DrugsRepository;
import com.java1234.repository.DrugsTypeRepository;
import com.java1234.repository.ReturnListDrugsRepository;
import com.java1234.repository.ReturnListRepository;
import com.java1234.service.ReturnListService;
import com.java1234.util.StringUtil;

/*
 * 退货单Service实现类
 * @author java1234_AT
 *
 */
@Service("returnListService")
@Transactional
public class ReturnListServiceImpl implements ReturnListService{

	@Resource
	private ReturnListRepository returnListRepository;
	
	@Resource
	private ReturnListDrugsRepository returnListDrugsRepository;
	
	@Resource
	private DrugsRepository drugsRepository;
	
	@Resource
	private DrugsTypeRepository drugsTypeRepository;
	
	@Override
	public String getTodayMaxReturnNumber() {
		return returnListRepository.getTodayMaxReturnNumber();
	}

	@Transactional
	public void save(ReturnList returnList, List<ReturnListDrugs> returnListDrugsList) {
		// 保存每个退货单药品
		for(ReturnListDrugs returnListDrugs:returnListDrugsList){
			returnListDrugs.setType(drugsTypeRepository.findOne(returnListDrugs.getTypeId())); // 设置类别
			returnListDrugs.setReturnList(returnList); // 设置退货单
			returnListDrugsRepository.save(returnListDrugs);
			// 修改药品库存
			Drugs drugs=drugsRepository.findOne(returnListDrugs.getDrugsId());
			drugs.setInventoryQuantity(drugs.getInventoryQuantity()-returnListDrugs.getNum());
			drugs.setState(2);
			drugsRepository.save(drugs);
		}
		returnListRepository.save(returnList); // 保存退货单
	}

	@Override
	public ReturnList findById(Integer id) {
		return returnListRepository.findOne(id);
		
	}

	@Override
	public List<ReturnList> list(ReturnList returnList, Direction direction, String... properties) {
		return returnListRepository.findAll(new Specification<ReturnList>(){

			@Override
			public Predicate toPredicate(Root<ReturnList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(returnList!=null){
					if(returnList.getSupplier()!=null && returnList.getSupplier().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("supplier").get("id"), returnList.getSupplier().getId()));
					}
					if(StringUtil.isNotEmpty(returnList.getReturnNumber())){
						predicate.getExpressions().add(cb.like(root.get("returnNumber"), "%"+returnList.getReturnNumber().trim()+"%"));
					}
					if(returnList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), returnList.getState()));
					}
					if(returnList.getbReturnDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("returnDate"), returnList.getbReturnDate()));
					}
					if(returnList.geteReturnDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("returnDate"), returnList.geteReturnDate()));
					}
				}
				return predicate;
			}
		  },new Sort(direction, properties));
	}

	@Override
	public void delete(Integer id) {
		returnListDrugsRepository.deleteByReturnListId(id);
		returnListRepository.delete(id);
	}

	@Override
	public void update(ReturnList returnList) {
		returnListRepository.save(returnList);
	}

}
