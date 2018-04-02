<!-- 
<%@page import="com.yeshine.util.CookiesUtil"%>
<%@page import="com.yeshine.dao.CommonDao"%>
<%@page import="com.yeshine.pojo.VbdAccount"%>
 -->
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>

<%
	/*
	//String account = CookiesUtil.getString("account", null, request);
	VbdAccount at = (VbdAccount)request.getSession().getAttribute("accountObj");
	String aid = "";
	String userName = "";
	if(at!=null){
		aid = at.getId();
		userName = at.getUserName();
	}
	WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()); 
	CommonDao dao=(CommonDao)ac.getBean("dao");
	
	String sql = "select f.id,f.FuncName,f.FuncUrl,f.ParentId,f.FuncIcon from syy_function f left join syy_role_function rf on rf.FuncId = f.ID left join syy_role r on r.ID = rf.RoleId left join syy_account_role ar on ar.RoleId = r.ID left join syy_account a on a.ID = ar.Account where a.ID = '"+aid+"' and f.status=0 order by f.sort";
	List<Object> flist = dao.getCustomSql(sql);
	JSONObject robj = new JSONObject();
	JSONArray arrayone = new JSONArray();
	if(flist!=null && flist.size()>0){
		Object[] objone = null;
		Object[] objtwo = null;
		JSONObject jobjone = null;
		JSONObject jobjtwo = null;
		JSONArray arraytwo = null;
		for(int i=0;i<flist.size();i++){
			objone = (Object[])flist.get(i);
			if(objone[3].toString().equals("0")){
				jobjone = new JSONObject();
				jobjone.put("menuid",objone[0].toString());
				jobjone.put("icon","icon-sys");
				jobjone.put("menuname",objone[1].toString());
				arraytwo = new JSONArray();
				for(int k=0;k<flist.size();k++){
					objtwo = (Object[])flist.get(k);
					if(objtwo[3].toString().equals(objone[0].toString())){
						jobjtwo = new JSONObject();
						jobjtwo.put("menuname",objtwo[1].toString());
						jobjtwo.put("icon","icon-nav");
						jobjtwo.put("url",request.getContextPath()+objtwo[2].toString());
						arraytwo.add(jobjtwo);
					}
				}
				jobjone.put("menus",arraytwo);
				arrayone.add(jobjone);
			}
		}
	}
	robj.put("menus",arrayone);
	System.out.println(robj.toString());
	*/
%>