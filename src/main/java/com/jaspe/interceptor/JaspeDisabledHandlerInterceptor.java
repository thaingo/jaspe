package com.jaspe.interceptor;

import com.jaspe.annotation.DisableJaspe;
import com.jaspe.util.RequestUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JaspeDisabledHandlerInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if(handler instanceof HandlerMethod) {
      HandlerMethod method = (HandlerMethod) handler;
      if (method.getMethodAnnotation(DisableJaspe.class) != null) {
        RequestUtil.disableJaspe(request);
      }
    }
    return true;
  }

  @Override
  public void postHandle(
    HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
  }

  @Override public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

  }
}

