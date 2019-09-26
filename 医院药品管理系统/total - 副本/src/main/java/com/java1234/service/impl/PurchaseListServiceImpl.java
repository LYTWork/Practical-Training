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
import com.java1234.entity.PurchaseList;
import com.java1234.entity.PurchaseListDrugs;
import com.java1234.repository.DrugsRepository;
import com.java1234.repository.DrugsTypeRepository;
import com.java1234.repository.PurchaseListDrugsRepository;
import com.java1234.repository.PurchaseListRepository;
import com.java1234.service.PurchaseListService;
import com.java1234.util.MathUtil;
import com.java1234.util.StringUtil;

/*
 * 进货单Service实现类
 * @author java1234_AT
 *
 */
@Service("purchaseListService")
@Transactional
public class PurchaseListServiceImpl implements PurchaseListService{

	@Resource
	private PurchaseListRepository purchaseListRepository;
	
	@Resource
	private PurchaseListDrugsRepository purchaseListDrugsRepository;
	
	@Resource
	private DrugsRepository drugsRepository;
	
	@Resource
	private DrugsTypeRepository drugsTypeRepository;
	
	@Override
	public String getTodayMaxPurchaseNumber() {
		return purchaseListRepository.getTodayMaxPurchaseNumber();
	}

	@Transactional
	public void save(PurchaseList purchaseList, List<PurchaseListDrugs> purchaseListDrugsList) {
		// 保存每个进货单药品
		for(PurchaseListDrugs purchaseListDrugs:purchaseListDrugsList){
			purchaseListDrugs.setType(drugsTypeRepository.findOne(purchaseListDrugs.getTypeId())); // 设置类别
			purchaseListDrugs.setPurchaseList(purchaseList); // 设置采购单
			purchaseListDrugsRepository.save(purchaseListDrugs); //**************
			// 修改药品库存 和 成本均价 以及上次进价
			Drugs drugs=drugsRepository.findOne(purchaseListDrugs.getDrugsId());
			// 计算成本均价
			float avePurchasingPrice=(drugs.getPurchasingPrice()*drugs.getInventoryQuantity()+purchaseListDrugs.getPrice()*purchaseListDrugs.getNum())/(drugs.getInventoryQuantity()+purchaseListDrugs.getNum());
			drugs.setPurchasingPrice(MathUtil.format2Bit(avePurchasingPrice));
			drugs.setInventoryQuantity(drugs.getInventoryQuantity()+purchaseListDrugs.getNum());
			drugs.setLastPurchasingPrice(purchaseListDrugs.getPrice());
			drugs.setState(2);
			drugsRepository.save(drugs);
		}
		purchaseListRepository.save(purchaseList); // 保存进货单 //**************
	}

	@Override
	public List<PurchaseList> list(PurchaseList purchaseList, Direction direction,
			String... properties) {
		return purchaseListRepository.findAll(new Specification<PurchaseList>(){

			@Override
			public Predicate toPredicate(Root<PurchaseList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(purchaseList!=null){
					if(purchaseList.getSupplier()!=null && purchaseList.getSupplier().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("supplier").get("id"), purchaseList.getSupplier().getId()));
					}
					if(StringUtil.isNotEmpty(purchaseList.getPurchaseNumber())){
						predicate.getExpressions().add(cb.like(root.get("purchaseNumber"), "%"+purchaseList.getPurchaseNumber().trim()+"%"));
					}
					if(purchaseList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), purchaseList.getState()));
					}
					if(purchaseList.getbPurchaseDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("purchaseDate"), purchaseList.getbPurchaseDate()));
					}
					if(purchaseList.getePurchaseDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("purchaseDate"), purchaseList.getePurchaseDate()));
					}
				}
				return predicate;
			}
		  },new Sort(direction, properties));
	}

	@Override
	public void delete(Integer id) {
		purchaseListDrugsRepository.deleteByPurchaseListId(id);
		purchaseListRepository.delete(id);
	}

	@Override
	public PurchaseList findById(Integer id) {
		return purchaseListRepository.findOne(id);
	}

	@Override
	public void update(PurchaseList purchaseList) {
		purchaseListRepository.save(purchaseList);
	}



}
