package com.jaspe.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
@Configuration
public class JaspeResponseBodyHandlerInterceptor implements HandlerInterceptor {

  @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    return true;
  }

  @Override public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

  }

  @Override public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

  }
}
