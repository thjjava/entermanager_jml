package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.UserQuestion;
import com.sttri.service.IUserQuestionService;

@Service
public class UserQuestionServiceImpl implements IUserQuestionService {
	@Autowired
	private CommonDao dao;

	@Override
	public List<UserQuestion> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		// TODO Auto-generated method stub
		return dao.getResultList(UserQuestion.class, wherejpql, orderby, queryParams);
	}

	@Override
	public void save(UserQuestion UserQuestion) {
		// TODO Auto-generated method stub
		dao.save(UserQuestion);
	}

	@Override
	public void update(UserQuestion UserQuestion) {
		// TODO Auto-generated method stub
		dao.update(UserQuestion);
	}

	@Override
	public UserQuestion getById(Object id) {
		// TODO Auto-generated method stub
		return dao.find(UserQuestion.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		// TODO Auto-generated method stub
		dao.delete(UserQuestion.class, array);
	}

	@Override
	public void deletebyid(Object id) {
		// TODO Auto-generated method stub
		dao.delete(UserQuestion.class, id);
	}

	@Override
	public QueryResult<UserQuestion> getScrollData(int firstindex,
			int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		// TODO Auto-generated method stub
		return dao.getScrollData(UserQuestion.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

}
