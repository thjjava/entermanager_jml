package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.CompanyGroup;
import com.sttri.service.ICompanyGroupService;

@Service
public class CompanyGroupServiceImpl implements ICompanyGroupService {
	@Autowired
	private CommonDao dao;
	

	@Override
	public CompanyGroup getById(Object id) {
		return dao.find(CompanyGroup.class, id);
	}

	@Override
	public List<CompanyGroup> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(CompanyGroup.class, wherejpql, orderby, queryParams);
	}

	@Override
	public void deletebyid(Object id) {
		dao.delete(CompanyGroup.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(CompanyGroup.class, array);
	}

	@Override
	public QueryResult<CompanyGroup> getScrollData(int firstindex,
			int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(CompanyGroup.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(CompanyGroup companyGroup) {
		dao.save(companyGroup);
	}

	@Override
	public void update(CompanyGroup companyGroup) {
		dao.update(companyGroup);
	}

}
