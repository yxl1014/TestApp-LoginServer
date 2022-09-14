package yxl.testapp.interceptor;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import yxl.testapp.annotation.Check;
import yxl.testapp.controller.UserController;
import yxl.testapp.domain.TestProto;
import yxl.testapp.logs.LogMsg;
import yxl.testapp.logs.LogUtil;
import yxl.testapp.logs.OptionDetails;
import yxl.testapp.service.UserService;
import yxl.testapp.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author yxl
 * @date: 2022/9/13 下午9:34
 */

@Component
public class CheckHandle implements HandlerInterceptor {

    private final static Logger logger = LogUtil.getLogger(CheckHandle.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            response.sendError(1000, OptionDetails.NO_CONTROLLER.getAll());
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (!method.isAnnotationPresent(Check.class)) {
            logger.info(LogUtil.makeOptionDetails(LogMsg.INTERCEPTOR, OptionDetails.NO_CHECK));
            return true;
        }
        logger.info(LogUtil.makeOptionDetails(LogMsg.INTERCEPTOR, OptionDetails.CHECK));
        String token = request.getHeader("token");
        if (token == null) {
            response.sendError(1001, OptionDetails.NO_TOKEN.getAll());
            return false;
        }
        byte[] data = JWTUtil.unsign(token, byte[].class);
        if (data == null) {
            response.sendError(1002, OptionDetails.TOKEN_ERROR.getAll());
            return false;
        }

        TestProto.User user = TestProto.User.parseFrom(data);
        boolean b = userService.checkUser(user);
        if (!b) {
            response.sendError(1003, OptionDetails.TOKEN_EXPIRES.getAll());
            return false;
        }
        logger.info(LogUtil.makeOptionDetails(LogMsg.INTERCEPTOR, OptionDetails.CHECK_OK, user));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
