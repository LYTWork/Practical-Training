package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.Log;

/*
 * 系统日志Service接口
 * @author java1234 AT
 *
 */
public interface LogService {


	
	/*
	 * 修改或者修改日志信息
	 */
    void save(Log log);
	
	/*
	 * 根据条件分页查询日志信息
	 */
    List<Log> list(Log log, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/*
	 * 获取总记录数
	 */
    Long getCount(Log log);

}
