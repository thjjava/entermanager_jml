package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.DevRecordTime;
import com.sttri.service.IDevRecordTimeService;

@Service
public class DevRecordTimeServiceImpl implements IDevRecordTimeService {
	@Autowired
	private CommonDao dao;
	
	@Override
	public void deletebyid(Object id) {
		dao.delete(DevRecordTime.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(DevRecordTime.class, array);
	}

	@Override
	public DevRecordTime getById(Object id) {
		return dao.find(DevRecordTime.class, id);
	}

	@Override
	public List<DevRecordTime> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(DevRecordTime.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<DevRecordTime> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(DevRecordTime.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(DevRecordTime devRecordTime) {
		dao.save(devRecordTime);
	}

	@Override
	public void update(DevRecordTime devRecordTime) {
		dao.update(devRecordTime);
	}

}
