package com.sttri.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.sttri.bean.QueryResult;
import com.sttri.pojo.CompanyGroup;

public interface ICompanyGroupService {
	/**
     * 根据条件来进行查询
     * @param wherejpql
     * @param orderby
     * @param queryParams
     * @return
     */
	public List<CompanyGroup> getResultList(String wherejpql,LinkedHashMap<String, String> orderby, Object... queryParams);

	public List<CompanyGroup> getLocalSql(String wherejpql);
	/**
	 * 分页信息查询
	 * */
	public QueryResult<CompanyGroup> getScrollData(int firstindex,int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);

	public void save(CompanyGroup companyGroup);

	public void update(CompanyGroup companyGroup);
	
	public CompanyGroup getById(Object id);

	public void deletebyids(Object[] array);

	public void deletebyid(Object id);
}
