package com.xd.archiveCheck.service;


import com.xd.archiveCheck.domain.TProFileDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */
public interface TProFileService {
	
	TProFileDO get(Long proCode);
	
	List<TProFileDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TProFileDO tProFile);
	
	int update(TProFileDO tProFile);
	
	int remove(Long proCode);
	
	int batchRemove(Long[] proCodes);
}
