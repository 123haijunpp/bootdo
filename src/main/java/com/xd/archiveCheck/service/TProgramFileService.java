package com.xd.archiveCheck.service;

import com.xd.archiveCheck.domain.TProgramFileDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */
public interface TProgramFileService {
	
	TProgramFileDO get(Long id);
	
	List<TProgramFileDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TProgramFileDO tProgramFile);
	
	int update(TProgramFileDO tProgramFile);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
