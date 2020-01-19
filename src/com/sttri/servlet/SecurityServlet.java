package com.sttri.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sttri.dao.CommonDao;
import com.sttri.pojo.TblControl;
import com.sttri.pojo.TblUser;


public class SecurityServlet extends HttpServlet implements Filter {
	
	private static final long serialVersionUID = 1L;

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
	  	   HttpServletRequest request=(HttpServletRequest)arg0;   
		   HttpServletResponse response  =(HttpServletResponse) arg1; 
		   response.addHeader("x-frame-options","SAMEORIGIN");
		   String account = (String)request.getSession().getAttribute("Account");
		   String url=request.getRequestURI();   
		   if("".equals(account) || null==account) {
			    WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();    
	            ServletContext servletContext = webApplicationContext.getServletContext();  
	            ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	            CommonDao dao = (CommonDao)ac.getBean("dao");
	            /*
	             * 一键登录地址
	             * http://localhost:8081/entermanager/index.jsp?tenantId=8057222368
	             */
	            String tenantId = request.getParameter("tenantId");
	            if (StringUtils.isNotBlank(tenantId)) {
	            	List<TblUser> users = dao.getResultList(TblUser.class, " o.accountType=? and o.company.tenantId=? ", null,new Object[] {0,tenantId});
	            	if (users != null && users.size() > 0) {
	            		request.getSession().setAttribute("isAdmin", true);
						request.getSession().setAttribute("user", users.get(0));
						request.getSession().setAttribute("Account", users.get(0).getAccount());
						arg2.doFilter(arg0, arg1);   
				        return;  
					}
				}
	            List<TblControl> list = dao.getResultList(TblControl.class, " 1=1 ", null);
	            request.setAttribute("list", list);
	            //判断获取的路径不为空且不是访问登录页面或执行登录操作时跳转   
	            /*if(url!=null && !url.equals("") && ( url.indexOf("Login")<0 && url.indexOf("login")<0 && url.indexOf("welcome")<0) ) {   
	            	response.sendRedirect(request.getContextPath() + "/welcome.jsp");   
	                return ;   
	            }*/
	            //其他平台直接跳转到login.JSP
	            if(url!=null && !url.equals("") && ( url.indexOf("Login")<0 && url.indexOf("login")<0 && url.indexOf("welcome")<0) ) {   
	            	response.sendRedirect(request.getContextPath() + "/login.jsp");   
	                return ;   
	            } 
	        }
            arg2.doFilter(arg0, arg1);   
            return;   
	}
	
	public void init(FilterConfig arg0) throws ServletException {
	
	}
}   

