package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.MeetingRecord;
import com.sttri.service.IMeetingRecordService;

@Service
public class MeetingRecordServiceImpl implements IMeetingRecordService {
	@Autowired
	private CommonDao dao;
	
	@Override
	public void deletebyid(Object id) {
		dao.delete(MeetingRecord.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(MeetingRecord.class, array);
	}

	@Override
	public MeetingRecord getById(Object id) {
		return dao.find(MeetingRecord.class, id);
	}

	@Override
	public List<MeetingRecord> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(MeetingRecord.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<MeetingRecord> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(MeetingRecord.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(MeetingRecord MeetingRecord) {
		dao.save(MeetingRecord);
	}

	@Override
	public void update(MeetingRecord MeetingRecord) {
		dao.update(MeetingRecord);
	}

}
