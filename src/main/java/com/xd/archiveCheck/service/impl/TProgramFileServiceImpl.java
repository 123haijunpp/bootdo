package com.xd.archiveCheck.service.impl;

import com.xd.archiveCheck.dao.TProgramFileDao;
import com.xd.archiveCheck.domain.TProgramFileDO;
import com.xd.archiveCheck.service.TProgramFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class TProgramFileServiceImpl implements TProgramFileService {
	@Autowired
	private TProgramFileDao tProgramFileDao;
	
	@Override
	public TProgramFileDO get(Long id){
		return tProgramFileDao.get(id);
	}
	
	@Override
	public List<TProgramFileDO> list(Map<String, Object> map){
		return tProgramFileDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return tProgramFileDao.count(map);
	}
	
	@Override
	public int save(TProgramFileDO tProgramFile){
		return tProgramFileDao.save(tProgramFile);
	}
	
	@Override
	public int update(TProgramFileDO tProgramFile){
		return tProgramFileDao.update(tProgramFile);
	}
	
	@Override
	public int remove(Long id){
		return tProgramFileDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return tProgramFileDao.batchRemove(ids);
	}
	
}
