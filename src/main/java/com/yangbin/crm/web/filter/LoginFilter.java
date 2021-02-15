package com.yangbin.crm.web.filter;

import com.yangbin.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("进入到登录验证操作");
        HttpServletRequest hsr = (HttpServletRequest) req;
        HttpServletResponse hsrp = (HttpServletResponse) resp;
        User user = (User) hsr.getSession().getAttribute("user");
        String path = hsr.getServletPath();
        if ("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);
        }else {
            if (user == null){
                hsrp.sendRedirect(hsr.getContextPath() + "/login.jsp");
            }else {
                chain.doFilter(req, resp);
            }
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
