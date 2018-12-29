package com.xd.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.xd.common.dao.TbFileDao;
import com.xd.common.domain.TbFileDO;
import com.xd.common.service.TbFileService;



@Service
public class TbFileServiceImpl implements TbFileService {
	@Autowired
	private TbFileDao tbFileDao;
	
	@Override
	public TbFileDO get(Integer id){
		return tbFileDao.get(id);
	}
	
	@Override
	public List<TbFileDO> list(Map<String, Object> map){
		return tbFileDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return tbFileDao.count(map);
	}
	
	@Override
	public int save(TbFileDO tbFile){
		return tbFileDao.save(tbFile);
	}
	
	@Override
	public int update(TbFileDO tbFile){
		return tbFileDao.update(tbFile);
	}
	
	@Override
	public int remove(Integer id){
		return tbFileDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return tbFileDao.batchRemove(ids);
	}
	
}
