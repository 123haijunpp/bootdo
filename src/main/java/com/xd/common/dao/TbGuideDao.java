package com.xd.common.dao;

import com.xd.common.domain.TbGuideDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 办证指引
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-08-02 14:50:55
 */
@Mapper
public interface TbGuideDao {

	TbGuideDO get(Integer id);
	
	List<TbGuideDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TbGuideDO tbGuide);
	
	int update(TbGuideDO tbGuide);
	
	int remove(Integer Id);
	
	int batchRemove(Integer[] ids);
}
