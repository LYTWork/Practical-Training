package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.entity.DrugsType;
import com.java1234.repository.DrugsTypeRepository;
import com.java1234.service.DrugsTypeService;

/*
 * 药品类别Service实现类
 * @author java1234 AT
 *
 */
@Service("drugsTypeService")
public class DrugsTypeServiceImpl implements DrugsTypeService{

	@Resource
	private DrugsTypeRepository drugsTypeRepository;
	
	@Override
	public void save(DrugsType drugsType) {
		drugsTypeRepository.save(drugsType);
	}

	@Override
	public void delete(Integer id) {
		drugsTypeRepository.delete(id);
	}

	@Override
	public List<DrugsType> findByParentId(int parentId) {
		return drugsTypeRepository.findByParentId(parentId);
	}

	@Override
	public DrugsType findById(Integer id) {
		return drugsTypeRepository.findOne(id);
	}

}
