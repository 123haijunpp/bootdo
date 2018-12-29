package com.xd.archiveCheck.service;


import com.xd.archiveCheck.domain.TProjectDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */
public interface TProjectService {
	
	TProjectDO get(Long id);
	
	List<TProjectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TProjectDO tProject);
	
	int update(TProjectDO tProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
