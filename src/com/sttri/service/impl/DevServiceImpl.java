package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.TblDev;
import com.sttri.service.IDevService;

@Service
public class DevServiceImpl implements IDevService {
	@Autowired
	private CommonDao dao;
	@Override
	public void deletebyid(Object id) {
		dao.delete(TblDev.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(TblDev.class, array);
	}

	@Override
	public TblDev getById(Object id) {
		return dao.find(TblDev.class, id);
	}

	@Override
	public List<TblDev> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(TblDev.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<TblDev> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(TblDev.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(TblDev dev) {
		dao.save(dev);
	}

	@Override
	public void update(TblDev dev) {
		dao.update(dev);
	}

	@Override
	public void group(String ids, String groupId) {
		String[] idsStr = ids.split("_");
		List<Object[]> list = null;
		for(int i=0;i<idsStr.length;i++){
			list = dao.getCustomSql("select * from tbl_dev where id='"+idsStr[i].toString()+"'");
			if(list==null||list.size()==0)
				continue;
			dao.getCustomSqlUpdate("update tbl_dev set groupId='"+groupId+"' where id = '"+idsStr[i].toString()+"'");
		}
	}

}
