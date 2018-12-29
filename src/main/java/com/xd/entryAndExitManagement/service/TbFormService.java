package com.xd.entryAndExitManagement.service;

import com.xd.entryAndExitManagement.domain.ReservationDO;
import com.xd.entryAndExitManagement.domain.TbFormDO;

import java.util.List;
import java.util.Map;

/**
 * 填表数据
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-07-31 09:59:34
 */
public interface TbFormService {
	
	TbFormDO get(Integer id);
	
	List<TbFormDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TbFormDO tbForm);
	
	int update(TbFormDO tbForm);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	List<ReservationDO> listReservation(Map<String, Object> map);
	List<ReservationDO> listReservationData(Map<String, Object> map);
	int listReservationDataCount(Map<String, Object> map);
}
