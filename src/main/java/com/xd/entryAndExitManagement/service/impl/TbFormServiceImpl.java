package com.xd.entryAndExitManagement.service.impl;

import com.xd.entryAndExitManagement.domain.ReservationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.xd.entryAndExitManagement.dao.TbFormDao;
import com.xd.entryAndExitManagement.domain.TbFormDO;
import com.xd.entryAndExitManagement.service.TbFormService;



@Service
public class TbFormServiceImpl implements TbFormService {
	@Autowired
	private TbFormDao tbFormDao;
	
	@Override
	public TbFormDO get(Integer id){
		return tbFormDao.get(id);
	}
	
	@Override
	public List<TbFormDO> list(Map<String, Object> map){
		return tbFormDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return tbFormDao.count(map);
	}
	
	@Override
	public int save(TbFormDO tbForm){
		return tbFormDao.save(tbForm);
	}
	
	@Override
	public int update(TbFormDO tbForm){
		return tbFormDao.update(tbForm);
	}
	
	@Override
	public int remove(Integer id){
		return tbFormDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return tbFormDao.batchRemove(ids);
	}

	@Override
	public List<ReservationDO> listReservation(Map<String, Object> map) {
		return tbFormDao.listReservation(map);
	}

	@Override
	public List<ReservationDO> listReservationData(Map<String, Object> map) {
		return tbFormDao.listReservationData(map);
	}

	@Override
	public int listReservationDataCount(Map<String, Object> map) {
		return tbFormDao.listReservationDataCount(map);
	}

}
