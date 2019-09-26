package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.entity.DrugsUnit;
import com.java1234.repository.DrugsUnitRepository;
import com.java1234.service.DrugsUnitService;

/*
 * 药品单位Service实现类
 * @author java1234 AT
 *
 */
@Service("drugsUnitService")
public class DrugsUnitServiceImpl implements DrugsUnitService{

	@Resource
	private DrugsUnitRepository drugsUnitRepository;
	
	@Override
	public List<DrugsUnit> listAll() {
		return drugsUnitRepository.findAll();
	}

	@Override
	public void save(DrugsUnit drugsUnit) {
		drugsUnitRepository.save(drugsUnit);
	}

	@Override
	public void delete(Integer id) {
		drugsUnitRepository.delete(id);
	}

	@Override
	public DrugsUnit findById(Integer id) {
		return drugsUnitRepository.findOne(id);
	}

}
