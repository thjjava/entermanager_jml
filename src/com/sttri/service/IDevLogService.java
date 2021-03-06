package com.sttri.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.sttri.bean.QueryResult;
import com.sttri.pojo.DevLog;

public interface IDevLogService {
	/**
     * 根据条件来进行查询
     * @param wherejpql
     * @param orderby
     * @param queryParams
     * @return
     */
	public List<DevLog> getResultList(String wherejpql,LinkedHashMap<String, String> orderby, Object... queryParams);
	
	public List<DevLog> getResultList(String wherejpql);
	
	/**
	 * 分页信息查询
	 * */
	public QueryResult<DevLog> getScrollData(int firstindex,int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);

	public void save(DevLog devLog);

	public void update(DevLog devLog);
	
	public DevLog getById(Object id);

	public void deletebyids(Object[] array);

	public void deletebyid(Object id);
	
	public Integer loginCount(String sql);
	
}
