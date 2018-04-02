package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.MeetingResource;
import com.sttri.service.IMeetingResourceService;

@Service
public class MeetingResourceServiceImpl implements IMeetingResourceService {
	@Autowired
	private CommonDao dao;

	
	@Override
	public void deletebyid(Object id) {
		dao.delete(MeetingResource.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(MeetingResource.class, array);
	}

	@Override
	public MeetingResource getById(Object id) {
		return dao.find(MeetingResource.class, id);
	}

	@Override
	public List<MeetingResource> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(MeetingResource.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<MeetingResource> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(MeetingResource.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(MeetingResource user) {
		dao.save(user);
	}

	@Override
	public void update(MeetingResource user) {
		dao.update(user);
	}

}
