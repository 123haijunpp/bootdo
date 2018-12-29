package com.xd.archiveCheck.dao;

import java.util.List;
import java.util.Map;

import com.xd.archiveCheck.domain.TProgramFileDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */
@Mapper
public interface TProgramFileDao {

	TProgramFileDO get(Long id);
	
	List<TProgramFileDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TProgramFileDO tProgramFile);
	
	int update(TProgramFileDO tProgramFile);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
