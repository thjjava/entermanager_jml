package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.TblQuestion;
import com.sttri.pojo.TblUser;
import com.sttri.service.IQuestionService;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

public class QuestionAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4438371449795412900L;
	
	private String rows;            
	private String page;
	
	private TblQuestion question;

	@Autowired
	private IQuestionService questionService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows);
		String type = Util.dealNull(request.getParameter("type"));
		PageView<TblQuestion> pageView = new PageView<TblQuestion>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if (!"".equals(type)) {
				jpql.append(" and o.type =?");
				param.add(Integer.parseInt(type));
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblQuestion> qr = this.questionService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"question\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<TblQuestion> ulist = null;
		try {
			PrintWriter pw = response.getWriter();
			ulist = this.questionService.getResultList("1=1 ", null);
			if(ulist==null || ulist.size()==0){
				ulist = new ArrayList<TblQuestion>();
			}
			pw.print(new JsonView(ulist));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void save(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String result = "fail";
			List<TblQuestion> list = this.questionService.getResultList("o.question=?", null, question.getQuestion());
			if(list!=null && list.size()>0){
				result = "has";
			}else{
				TblUser user = WorkUtil.getCurrUser(request);
				question.setId(Util.getUUID(6));
				question.setEditUser(user.getAccount());
				question.setAddTime(Util.dateToStr(new Date()));
				question.setStatus(0);
				this.questionService.save(question);
				result = "success";
			}
			pw.print(result);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			question.setEditTime(Util.dateToStr(new Date()));
			this.questionService.update(question);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			TblQuestion u = null;
			if(!id.equals("")){
				u = this.questionService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(u));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				this.questionService.deletebyids(ids.split("_"));
				PrintWriter pw = response.getWriter();
				pw.print("success");
				pw.flush();
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setInvalid(){
		response.setCharacterEncoding("UTF-8");
		String ids = Util.dealNull(request.getParameter("ids"));
		try {
			PrintWriter pw = response.getWriter();
			if(!"".equals(ids) && null!=ids){
				String[] idArray = ids.split("_");
				for (int i = 0; i < idArray.length; i++) {
					String id = idArray[i];
					TblQuestion question = this.questionService.getById(id);
					if (question != null) {
						int status = question.getStatus()==1?0:1;
						question.setStatus(status);//status=0 正常，=1作废
						this.questionService.update(question);
					}
				}
				pw.print("success");
				pw.flush();
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public TblQuestion getQuestion() {
		return question;
	}

	public void setQuestion(TblQuestion question) {
		this.question = question;
	}
	

}
