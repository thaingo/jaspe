package com.jaspe.view;

import com.jaspe.context.JaspeContext;
import org.springframework.http.HttpStatus;

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
public abstract class JaspeView {

  /**
   *
   * @param value
   * @param jaspeContext
   * @throws Exception
   */
  protected abstract void renderOK(Object value, JaspeContext jaspeContext) throws Exception;

  /**
   *
   * @param value
   * @param jaspeContext
   * @throws Exception
   */
  protected abstract void renderError(Object value, JaspeContext jaspeContext) throws Exception;

  /**
   *
   * @param value
   * @param jaspeContext
   * @throws Exception
   */
  public final void render(Object value, JaspeContext jaspeContext) throws Exception {
    final HttpStatus httpStatus = HttpStatus.valueOf(jaspeContext.getResponse().getStatus());
    if (httpStatus == HttpStatus.OK) {
      renderOK(value, jaspeContext);
    } else {
      renderError(value,jaspeContext);
    }
  }
}
