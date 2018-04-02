package com.sttri.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.sttri.bean.QueryResult;
import com.sttri.pojo.TblQuestion;

public interface IQuestionService {
	/**
     * 根据条件来进行查询
     * @param wherejpql
     * @param orderby
     * @param queryParams
     * @return
     */
	public List<TblQuestion> getResultList(String wherejpql,LinkedHashMap<String, String> orderby, Object... queryParams);
	
	/**
	 * 分页信息查询
	 * */
	public QueryResult<TblQuestion> getScrollData(int firstindex,int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);

	public void save(TblQuestion question);

	public void update(TblQuestion question);
	
	public TblQuestion getById(Object id);

	public void deletebyids(Object[] array);

	public void deletebyid(Object id);
	
	public List<TblQuestion> getRandResultList(String wherejpql,int firstResult,int maxResult);
	
}
