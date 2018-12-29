package com.xd.archiveCheck.service.impl;

import com.xd.archiveCheck.dao.TProjectDao;
import com.xd.archiveCheck.domain.TProjectDO;
import com.xd.archiveCheck.service.TProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service
public class TProjectServiceImpl implements TProjectService {
	@Autowired
	private TProjectDao tProjectDao;
	
	@Override
	public TProjectDO get(Long id){
		return tProjectDao.get(id);
	}
	
	@Override
	public List<TProjectDO> list(Map<String, Object> map){
		return tProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return tProjectDao.count(map);
	}
	
	@Override
	public int save(TProjectDO tProject){
		return tProjectDao.save(tProject);
	}
	
	@Override
	public int update(TProjectDO tProject){
		return tProjectDao.update(tProject);
	}
	
	@Override
	public int remove(Long id){
		return tProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return tProjectDao.batchRemove(ids);
	}
	
}
