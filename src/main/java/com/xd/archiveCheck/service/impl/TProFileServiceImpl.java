package com.xd.archiveCheck.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.xd.archiveCheck.dao.TProFileDao;
import com.xd.archiveCheck.domain.TProFileDO;
import com.xd.archiveCheck.service.TProFileService;


@Service
public class TProFileServiceImpl implements TProFileService {
    @Autowired
    private TProFileDao tProFileDao;

    @Override
    public TProFileDO get(Long proCode) {
        return tProFileDao.get(proCode);
    }

    @Override
    public List<TProFileDO> list(Map<String, Object> map) {
        return tProFileDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return tProFileDao.count(map);
    }

    @Override
    public int save(TProFileDO tProFile) {
        return tProFileDao.save(tProFile);
    }

    @Override
    public int update(TProFileDO tProFile) {
        return tProFileDao.update(tProFile);
    }

    @Override
    public int remove(Long proCode) {
        return tProFileDao.remove(proCode);
    }

    @Override
    public int batchRemove(Long[] proCodes) {
        return tProFileDao.batchRemove(proCodes);
    }

}
