package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.Company;
import com.sttri.service.ICompanyService;
import com.sttri.util.Util;

public class CompanyAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String rows;            
	private String page;
	private Company company;
	@Autowired
	private ICompanyService companyService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows);
		
		PageView<Company> pageView = new PageView<Company>(row, pages);
		List<Object> param = new ArrayList<Object>();
//		TblUser u = WorkUtil.getCurrUser(request);
		try {
			StringBuffer jpql = new StringBuffer("1=1");
//			param.add(u.getCompany().getId());
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<Company> qr = companyService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"comName\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<Company> clist = null;
		try {
			PrintWriter pw = response.getWriter();
			clist = companyService.getResultList("1=1 ", null);
			if(clist==null || clist.size()==0){
				clist = new ArrayList<Company>();
			}
			pw.print(new JsonView(clist));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public String getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			Company c = null;
			if(!id.equals("")){
				c = companyService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(c));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
