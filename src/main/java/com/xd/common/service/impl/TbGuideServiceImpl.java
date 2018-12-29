package com.xd.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.xd.common.dao.TbGuideDao;
import com.xd.common.domain.TbGuideDO;
import com.xd.common.service.TbGuideService;



@Service
public class TbGuideServiceImpl implements TbGuideService {
	@Autowired
	private TbGuideDao tbGuideDao;
	
	@Override
	public TbGuideDO get(Integer id){
		return tbGuideDao.get(id);
	}
	
	@Override
	public List<TbGuideDO> list(Map<String, Object> map){
		return tbGuideDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return tbGuideDao.count(map);
	}
	
	@Override
	public int save(TbGuideDO tbGuide){
		return tbGuideDao.save(tbGuide);
	}
	
	@Override
	public int update(TbGuideDO tbGuide){
		return tbGuideDao.update(tbGuide);
	}
	
	@Override
	public int remove(Integer id){
		return tbGuideDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return tbGuideDao.batchRemove(ids);
	}
	
}
