package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.TblQuestion;
import com.sttri.service.IQuestionService;

@Service
public class QuestionServiceImpl implements IQuestionService {
	@Autowired
	private CommonDao dao;
	
	@Override
	public void deletebyid(Object id) {
		dao.delete(TblQuestion.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(TblQuestion.class, array);
	}

	@Override
	public TblQuestion getById(Object id) {
		return dao.find(TblQuestion.class, id);
	}

	@Override
	public List<TblQuestion> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(TblQuestion.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<TblQuestion> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(TblQuestion.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(TblQuestion question) {
		dao.save(question);
	}

	@Override
	public void update(TblQuestion question) {
		dao.update(question);
	}

	@Override
	public List<TblQuestion> getRandResultList(String wherejpql, int firstResult, int maxResult) {
		// TODO Auto-generated method stub
		return dao.getRandResultList(TblQuestion.class, wherejpql, firstResult, maxResult);
	}


}
