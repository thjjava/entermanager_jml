package com.sttri.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.sttri.bean.QueryResult;
import com.sttri.pojo.UserQuestion;

public interface IUserQuestionService {
	
	/**
	 * 分页信息查询
	 * */
	public QueryResult<UserQuestion> getScrollData(int firstindex,int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);
	
	/**
     * 根据条件来进行查询
     * @param wherejpql
     * @param orderby
     * @param queryParams
     * @return
     */
	public List<UserQuestion> getResultList(String wherejpql,LinkedHashMap<String, String> orderby, Object... queryParams);
	
	public void save(UserQuestion userQuestion);

	public void update(UserQuestion userQuestion);
	
	public UserQuestion getById(Object id);

	public void deletebyids(Object[] array);

	public void deletebyid(Object id);
}
