package tech.qiantong.qknow.module.app.annotation.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检查 ClientToken 拦截器
 * @author Ming
 */
@Component
public class AppCheckClientTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            DsCheckClientToken annotation = handlerMethod.getMethodAnnotation(DsCheckClientToken.class);
//            if (annotation != null) {
//                // 执行检查逻辑
//                ClientTokenModel clientTokenModel = SaOAuth2Util.checkClientToken(SaHolder.getRequest().getParam(SaOAuth2Consts.Param.client_token));
//                return clientTokenModel != null;
//            }
//        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在业务处理器处理请求并渲染视图后调用
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求完全结束后调用，即在视图渲染完成后
    }
}
