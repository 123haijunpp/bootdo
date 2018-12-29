package com.xd.common.service;

import com.xd.common.domain.TbGuideDO;

import java.util.List;
import java.util.Map;

/**
 * 办证指引
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-08-02 14:50:55
 */
public interface TbGuideService {
	
	TbGuideDO get(Integer id);
	
	List<TbGuideDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TbGuideDO tbGuide);
	
	int update(TbGuideDO tbGuide);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
