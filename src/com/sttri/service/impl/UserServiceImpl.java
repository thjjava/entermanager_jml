package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.TblUser;
import com.sttri.pojo.UserGroup;
import com.sttri.service.IUserGroupService;
import com.sttri.service.IUserService;
import com.sttri.util.Util;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private CommonDao dao;
	@Autowired
	private IUserGroupService userGroupService;
	@Override
	public void deletebyid(Object id) {
		dao.delete(TblUser.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(TblUser.class, array);
	}

	@Override
	public TblUser getById(Object id) {
		return dao.find(TblUser.class, id);
	}

	@Override
	public List<TblUser> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(TblUser.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<TblUser> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(TblUser.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(TblUser user) {
		dao.save(user);
	}

	@Override
	public void update(TblUser user) {
		dao.update(user);
	}

	@Override
	public void save(TblUser user, String groupId) {
		save(user);
		if(groupId.equals("0"))
			return;
		UserGroup ug = new UserGroup();
		ug.setId(Util.getUUID(6));
		ug.setGroupId(groupId);
		ug.setUserId(user.getId());
		userGroupService.save(ug);
	}

	@Override
	public void update(TblUser user, String groupId) {
		update(user);
		List<UserGroup> uglist = userGroupService.getResultList("o.userId=?", null, user.getId());;
		UserGroup ug = null;
		if(uglist!=null && uglist.size()>0){
			ug = uglist.get(0);
		}else{
			ug = new UserGroup();
			ug.setId(Util.getUUID(6));
			ug.setGroupId(groupId);
			ug.setUserId(user.getId());
			userGroupService.save(ug);
			return;
		}
		if(groupId.equals("0")){
			deletebyid(ug.getId());
			return;
		}
		ug.setGroupId(groupId);
		userGroupService.update(ug);
		
	}

}
