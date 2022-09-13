package yxl.testapp.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author yxl
 * @date: 2022/8/30 下午12:04
 */

public class DoFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("filter 进来了--->" + servletRequest.getLocalPort());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
