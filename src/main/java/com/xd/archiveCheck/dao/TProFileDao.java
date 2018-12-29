package com.xd.archiveCheck.dao;


import java.util.List;
import java.util.Map;

import com.xd.archiveCheck.domain.TProFileDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */
@Mapper
public interface TProFileDao {

	TProFileDO get(Long proCode);
	
	List<TProFileDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TProFileDO tProFile);
	
	int update(TProFileDO tProFile);
	
	int remove(Long pro_code);
	
	int batchRemove(Long[] proCodes);
}
